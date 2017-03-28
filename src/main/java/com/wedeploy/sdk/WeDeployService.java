package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;

/**
 * @author Silvio Santos
 */
public class WeDeployService<T> {

	WeDeployService(WeDeploy weDeploy) {
		this.weDeploy = weDeploy;
	}

	public Auth getAuth() {
		if (auth != null) {
			return auth;
		}

		return weDeploy.getAuth();
	}

	public T auth(Auth auth) {
		this.auth = auth;

		return (T)this;
	}

	protected Request.Builder newAuthenticatedRequestBuilder(String url) {
		Request.Builder builder = new Request.Builder()
			.url(url);

		Auth auth = getAuth();

		if (auth == null) {
			return builder;
		}

		return auth.authenticate(builder);
	}

	protected Call<Response> newCall(Request request) {
		return new Call<>(request, weDeploy.getTransport(), weDeploy.getAsyncTransport(),
			Response.class);
	}

	private Auth auth;
	private final WeDeploy weDeploy;

}
