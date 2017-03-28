package com.wedeploy.sdk.internal;

import com.wedeploy.sdk.Callback;
import com.wedeploy.sdk.transport.AsyncTransport;
import com.wedeploy.sdk.transport.MultiMap;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import com.wedeploy.sdk.transport.Transport;
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
			.addPathSegments(request.getPath())
			.encodedQuery(request.getEncodedQuery());

		for (Map.Entry<String, String> param : request.params().entrySet()) {
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

	private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static OkHttpClient client = new OkHttpClient();

}
