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
	 * Gets the current used {@link Authorization}. If an authorization was configured for this service
	 * using {@link #authorization}, that one will be returned. Otherwise, the authorization provided
	 * while building the WeDeploy instance with {@link WeDeploy.Builder#authorization(Authorization)} will be returned
	 *
	 * @return The {@link Authorization} used by this Service to authenticate requests with WeDeploy.
	 */
	public Authorization getAuthorization() {
		if (authorization != null) {
			return authorization;
		}

		return weDeploy.getAuthorization();
	}

	/**
	 * Sets the {@link Authorization} used to authenticate requests with WeDeploy. The authorization
	 * provided by this method has a higher priority then the one provided while building the WeDeploy
	 * instance with {@link WeDeploy.Builder#authorization(Authorization)}.
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
			return "http://" + url;
		}
	}

	protected final String url;

	private Authorization authorization;
	private MultiMap<String> headers = new SimpleMultiMap<>();
	private final WeDeploy weDeploy;

}
