package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.transport.AsyncTransport;
import com.wedeploy.sdk.transport.Transport;

/**
 * @author Silvio Santos
 */
public class WeDeploy {

	WeDeploy(Builder builder) {
		auth = builder.auth;
		asyncTransport = builder.asyncTransport;
		transport = builder.transport;
	}

	public Auth getAuth() {
		return auth;
	}

	public AsyncTransport getAsyncTransport() {
		return asyncTransport;
	}

	public Transport getTransport() {
		return transport;
	}

	public WeDeployAuth auth(String url) {
		return new WeDeployAuth(this, url);
	}

	public WeDeployData data(String url) {
		return new WeDeployData(this, url);
	}

	public WeDeployEmail email(String url) {
		return new WeDeployEmail(this, url);
	}

	private final AsyncTransport asyncTransport;
	private final Auth auth;
	private final Transport transport;

	public static class Builder {

		public Builder asyncTransport(AsyncTransport transport) {
			this.asyncTransport = transport;

			return this;
		}

		public Builder auth(Auth auth) {
			this.auth = auth;

			return this;
		}

		public Builder transport(Transport transport) {
			this.transport = transport;

			return this;
		}

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
		private Auth auth;
		private Transport transport;
	}

}
