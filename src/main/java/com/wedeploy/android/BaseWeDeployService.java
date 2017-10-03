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

package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.SimpleMultiMap;

import java.util.List;
import java.util.Map;

import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * @author Silvio Santos
 */
public abstract class BaseWeDeployService<T> {

	/**
	 * Constructs a {@link BaseWeDeployService<T>} instance.
	 *
	 * @param weDeploy A WeDeploy instance.
	 * @param url The WeDeploy service url.
	 */
	BaseWeDeployService(WeDeploy weDeploy, String url) {
		checkNotNull(url, "Service url must be specified");

		this.weDeploy = weDeploy;
		this.url = addSchemeIfNeeded(url);
	}

	/**
	 * Gets the current used {@link Authorization}. If an authorization was configured for this
	 * service using {@link #authorization}, that one will be returned. Otherwise, the
	 * authorization provided while building the WeDeploy instance with
	 * {@link WeDeploy.Builder#authorization(Authorization)} will be returned
	 *
	 * @return The {@link Authorization} used by this Service to authenticate requests with
	 * WeDeploy.
	 */
	public Authorization getAuthorization() {
		if (authorization != null) {
			return authorization;
		}

		return weDeploy.getAuthorization();
	}

	/**
	 * Sets the {@link Authorization} used to authenticate requests with WeDeploy. The
	 * authorization provided by this method has a higher priority then the one provided while
	 * building the WeDeploy instance with {@link WeDeploy.Builder#authorization(Authorization)}.
	 *
	 * @param authorization
	 * @return {@link this} Returns the object itself, so calls can be chained.
	 */
	public T authorization(Authorization authorization) {
		this.authorization = authorization;

		return (T)this;
	}

	/**
	 * Sets a request header for this request.
	 *
	 * @param name The header name.
	 * @param value The header value.
	 * @return {@link this} Returns the object itself, so calls can be chained.
	 */
	public T header(String name, String value) {
		headers.put(name, value);

		return (T)this;
	}

	protected Request.Builder newAuthenticatedRequestBuilder() {
		Request.Builder builder = new Request.Builder()
			.url(url);

		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			for (String value : entry.getValue()) {
				builder.header(entry.getKey(), value);
			}
		}

		Authorization authorization = getAuthorization();

		if (authorization == null) {
			return builder;
		}

		return authorization.authenticate(builder);
	}

	protected Call<Response> newCall(Request request) {
		return new Call<>(
			request, weDeploy.getTransport(), weDeploy.getAsyncTransport(), Response.class);
	}

	private String addSchemeIfNeeded(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return url;
		}
		else {
			return "https://" + url;
		}
	}

	protected final String url;

	private Authorization authorization;
	private MultiMap<String> headers = new SimpleMultiMap<>();
	private final WeDeploy weDeploy;

}
