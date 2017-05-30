package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.transport.OkHttpTransport;
import com.wedeploy.android.transport.AsyncTransport;
import com.wedeploy.android.transport.Transport;

/**
 * WeDeploy is a factory for creating {@link WeDeployAuth}, {@link WeDeployData},
 * {@link WeDeployEmail} and {@link WeDeployService} instances.
 *
 * Use {@link Builder} to create WeDeploy instances.
 *
 *  <pre><code>
 *  WeDeploy weDeploy = new WeDeploy.Builder().build();
 *
 *  weDeploy
 *      .data("http://datademo.wedeploy.io")
 *      .create("movies", movieJsonObject)
 *      .execute();
 *  </code></pre>
 *
 * @author Silvio Santos
 */
public class WeDeploy {

	WeDeploy(Builder builder) {
		authorization = builder.authorization;
		asyncTransport = builder.asyncTransport;
		transport = builder.transport;
	}

	/**
	 * Gets the default Authorization.
	 *
	 * @return the current global {@link Authorization} or null, if none was set.
	 */
	public Authorization getAuthorization() {
		return authorization;
	}

	/**
	 * Gets the AsyncTransport. If none was set while building this instance, it will return a {@link OkHttpTransport}
	 *
	 * @return {@link AsyncTransport}
	 */
	public AsyncTransport getAsyncTransport() {
		return asyncTransport;
	}

	/**
	 * Gets the Transport. If none was set while building this instance, it will return a {@link OkHttpTransport}
	 *
	 * @return {@link AsyncTransport}
	 */
	public Transport getTransport() {
		return transport;
	}

	/**
	 * Creates a {@link WeDeployAuth} instance.
	 * @param url The WeDeploy Auth url
	 * @return a new {@link WeDeployAuth} instance.
	 */
	public WeDeployAuth auth(String url) {
		return new WeDeployAuth(this, url);
	}

	/**
	 * Creates a {@link WeDeployData} instance.
	 * @param url The WeDeploy Data url
	 * @return a new {@link WeDeployData} instance.
	 */
	public WeDeployData data(String url) {
		return new WeDeployData(this, url);
	}

	/**
	 * Creates a {@link WeDeployEmail} instance.
	 * @param url The WeDeploy Email url
	 * @return a new {@link WeDeployEmail} instance.
	 */
	public WeDeployEmail email(String url) {
		return new WeDeployEmail(this, url);
	}

	/**
	 * Creates a {@link WeDeployService} instance.
	 * @param url The WeDeploy Service url
	 * @return a new {@link WeDeployService} instance.
	 */
	public WeDeployService url(String url) {
		return new WeDeployService(this, url);
	}

	private final AsyncTransport asyncTransport;
	private final Authorization authorization;
	private final Transport transport;

	public static class Builder {

		/**
		 * Sets the default {@link AsyncTransport}.
		 *
		 * The default configuration is implemented using OkHttp.
		 *
		 * @param transport
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder asyncTransport(AsyncTransport transport) {
			this.asyncTransport = transport;

			return this;
		}

		/**
		 * Sets a global {@link Authorization} to authenticate all requests to WeDeploy.
		 * This authorization will be overridden anytime you call {@link WeDeployAuth#authorization},
		 * {@link WeDeployData#authorization}, {@link WeDeployEmail#authorization} or {@link WeDeployService#authorization}.
		 *
		 * @param authorization
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder authorization(Authorization authorization) {
			this.authorization = authorization;

			return this;
		}

		/**
		 * Sets the default {@link Transport}.
		 *
		 * The default configuration is implemented using OkHttp.
		 *
		 * @param transport
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder transport(Transport transport) {
			this.transport = transport;

			return this;
		}

		/**
		 * Builds a new WeDeploy instance.
		 *
		 * @return {@link WeDeploy}
		 */
		public WeDeploy build() {
			if (transport == null) {
				transport = new OkHttpTransport.Builder().build();
			}

			if (asyncTransport == null) {
				if (transport instanceof OkHttpTransport) {
					asyncTransport = (OkHttpTransport)transport;
				}
				else {
					asyncTransport = new OkHttpTransport.Builder().build();
				}
			}

			return new WeDeploy(this);
		}

		private AsyncTransport asyncTransport;
		private Authorization authorization;
		private Transport transport;
	}

}
