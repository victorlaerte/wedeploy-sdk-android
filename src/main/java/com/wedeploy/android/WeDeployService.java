package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.SimpleMultiMap;

import java.util.List;
import java.util.Map;

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

	public T header(String name, String value) {
		headers.put(name, value);

		return (T)this;
	}

	protected Request.Builder newAuthenticatedRequestBuilder(String url) {
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

	private Authorization authorization;
	private MultiMap<String> headers = new SimpleMultiMap<>();
	private final WeDeploy weDeploy;

}
