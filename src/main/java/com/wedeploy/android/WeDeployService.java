package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;

/**
 * @author Silvio Santos
 */
public class WeDeployService<T> {

	WeDeployService(WeDeploy weDeploy) {
		this.weDeploy = weDeploy;
	}

	public Authorization getAuthorization() {
		if (authorization != null) {
			return authorization;
		}

		return weDeploy.getAuthorization();
	}

	public T authorization(Authorization authorization) {
		this.authorization = authorization;

		return (T)this;
	}

	protected Request.Builder newAuthenticatedRequestBuilder(String url) {
		Request.Builder builder = new Request.Builder()
			.url(url);

		Authorization authorization = getAuthorization();

		if (authorization == null) {
			return builder;
		}

		return authorization.authenticate(builder);
	}

	protected Call<Response> newCall(Request request) {
		return new Call<>(request, weDeploy.getTransport(), weDeploy.getAsyncTransport(),
			Response.class);
	}

	private Authorization authorization;
	private final WeDeploy weDeploy;

}
