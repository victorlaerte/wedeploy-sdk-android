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

	public WeDeployAuth auth(String url) {
		return new WeDeployAuth(url);
	}

	public WeDeployData data(String url) {
		return new WeDeployData(url);
	}

	public WeDeployEmail email(String url) {
		return new WeDeployEmail(url);
	}

	private final AsyncTransport asyncTransport;
	private final Auth auth;
	private final Transport transport;

	public static class Builder {

		public void asyncTransport(AsyncTransport transport) {
			this.asyncTransport = transport;
		}

		public void auth(Auth auth) {
			this.auth = auth;
		}

		public void transport(Transport transport) {
			this.transport = transport;
		}

		public WeDeploy build() {
			if (transport == null) {
				transport = new OkHttpTransport();
			}

			if (asyncTransport == null) {
				asyncTransport = new OkHttpTransport();
			}

			return new WeDeploy(this);
		}

		private AsyncTransport asyncTransport;
		private Auth auth;
		private Transport transport;
	}

}
