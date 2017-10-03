/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Liferay, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.wedeploy.android.transport;

import com.wedeploy.android.Callback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OkHttpTransport implements Transport<Response>, AsyncTransport {

	public OkHttpClient getClient() {
		return client;
	}

	@Override
	public Response send(Request request) {
		try {
			okhttp3.Response okHttpResponse = newOkHttpCall(request)
				.execute();

			return toResponse(okHttpResponse);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void sendAsync(Request request, final Callback callback) {
		Call call = newOkHttpCall(request);
		call.enqueue(new okhttp3.Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				callback.onFailure(e);
			}

			@Override
			public void onResponse(Call call, okhttp3.Response okHttpResponse) throws IOException {
				callback.onSuccess(toResponse(okHttpResponse));
			}
		});
	}

	private okhttp3.Request getOkHttpRequest(Request request) {
		HttpUrl url = getUrl(request);
		RequestBody body = getRequestBody(request);
		String method = request.getMethod().getValue();

		okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
			.url(url)
			.method(method, body);

		for (Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
			for (String value : entry.getValue()) {
				builder.addHeader(entry.getKey(), value);
			}
		}

		return builder.build();
	}

	private RequestBody getRequestBody(Request request) {
		RequestMethod method = request.getMethod();
		String body = request.getBody();

		if (!HttpMethod.requiresRequestBody(method.getValue())) {
			return null;
		}

		MultiMap<String> forms = request.getForms();

		if (!forms.isEmpty()) {
			FormBody.Builder builder = new FormBody.Builder();

			for (Map.Entry<String, List<String>> entry : forms.entrySet()) {
				for (String value : entry.getValue()) {
					builder.add(entry.getKey(), value);
				}
			}

			return builder.build();
		}

		return RequestBody.create(JSON, body);

	}

	private HttpUrl getUrl(Request request) {
		HttpUrl.Builder builder = HttpUrl.parse(request.getUrl())
			.newBuilder()
			.addPathSegments(request.getPath());

		String encodedQuery = request.getEncodedQuery();

		if ((encodedQuery != null) && !encodedQuery.isEmpty()) {
			builder.encodedQuery(encodedQuery);
		}

		for (Map.Entry<String, String> param : request.getParams().entrySet()) {
			builder.addQueryParameter(param.getKey(), param.getValue());
		}

		return builder.build();
	}

	private okhttp3.Call newOkHttpCall(Request request) {
		okhttp3.Request okHttpRequest = getOkHttpRequest(request);

		return client.newCall(okHttpRequest);
	}

	private Response toResponse(okhttp3.Response okHttpResponse) throws IOException {
		return new Response.Builder()
			.body(okHttpResponse.body().string())
			.headers(okHttpResponse.headers().toMultimap())
			.statusCode(okHttpResponse.code())
			.statusMessage(okHttpResponse.message())
			.succeeded(okHttpResponse.isSuccessful())
			.build();
	}

	private OkHttpTransport(Builder builder) {
		this.client = builder.client;
	}

	private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private final OkHttpClient client;

	public static class Builder {

		public Builder client(OkHttpClient client) {
			this.client = client;

			return this;
		}

		public OkHttpTransport build() {
			if (client == null) {
				client = new OkHttpClient();
			}

			return new OkHttpTransport(this);
		}

		private OkHttpClient client;

	}

}
