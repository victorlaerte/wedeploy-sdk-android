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

package com.wedeploy.android.auth;

import android.app.Activity;

import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * This class encapsulates the provider information for executing an OAuth authentication.
 *
 * @author Silvio Santos
 * @see com.wedeploy.android.WeDeployAuth#signIn(Activity, ProviderAuthorization)
 */
public class ProviderAuthorization {

	/**
	 * Gets authorization url based on the provider configuration.
	 *
	 * @return {@link String} The redirect url.
	 */
	public String getAuthUrl() {
		StringBuilder sb = new StringBuilder()
			.append("/oauth/authorize?")
			.append("provider=")
			.append(provider.getProvider());

		if (scope != null) {
			sb.append("&scope=")
				.append(scope);
		}

		if (providerScope != null) {
			sb.append("&provider_scope=")
				.append(providerScope);
		}

		return sb.append("&redirect_uri=")
			.append(redirectUri)
			.toString();
	}

	/**
	 * Gets the provider.
	 *
	 * @return {@link Provider}.
	 */
	public Provider getProvider() {
		return provider;
	}

	/**
	 * Gets the provider's authorization scope.
	 *
	 * @return {@link String} The provider scope.
	 */
	public String getProviderScope() {
		return providerScope;
	}

	/**
	 * Gets the redirect uri.
	 *
	 * @return {@link String} The redirect uri.
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * Gets the WeDeploy authorization scope.
	 *
	 * @return {@link String} scope.
	 */
	public String getScope() {
		return scope;
	}

	private ProviderAuthorization(ProviderAuthorization.Builder builder) {
		this.provider = builder.provider;
		this.providerScope = builder.providerScope;
		this.redirectUri = builder.redirectUri;
		this.scope = builder.scope;
	}

	private final Provider provider;
	private final String providerScope;
	private final String redirectUri;
	private final String scope;

	/**
	 * {@link ProviderAuthorization} Builder.
	 */
	public static class Builder {

		/**
		 * Buids a new isntance of ProviderAuthorization.
		 *
		 * @return {@link ProviderAuthorization}.
		 */
		public ProviderAuthorization build() {
			checkNotNull(provider, "Provider must be specified");
			checkNotNull(redirectUri, "Redirect URI must be specified");

			return new ProviderAuthorization(this);
		}

		/**
		 * Sets the provider to connect to.
		 *
		 * @param provider Enumerable for third party provider.
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder provider(Provider provider) {
			this.provider = provider;
			return this;
		}

		/**
		 * Sets the provider authorization scope.
		 *
		 * @param providerScope The provider scope. Separate by space for multiple scopes,
		 * e.g. "scope1 scope2".
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder providerScope(String providerScope) {
			this.providerScope = providerScope;
			return this;
		}

		/**
		 * Sets the redirect uri.
		 *
		 * @param redirectUri The redirect uri.
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder redirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}

		/**
		 * Sets the WeDeploy authorization scope.
		 *
		 * @param scope The WeDeploy scope. Separate by space for multiple scopes,
		 * e.g. "scope1 scope2".
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder scope(String scope) {
			this.scope = scope;
			return this;
		}

		private Provider provider;
		private String providerScope;
		private String redirectUri;
		private String scope;

	}

	/**
	 * Available third party auth providers
	 * <li>{@link #GITHUB}</li>
	 * <li>{@link #GOOGLE}</li>
	 * <li>{@link #FACEBOOK}</li>
	 *
	 * @author Victor Oliveira
	 */
	public enum Provider {

		/**
		 * Login with github.
		 */
		GITHUB("github"),

		/**
		 * Login with google.
		 */
		GOOGLE("google"),

		/**
		 * Login with facebook.
		 */
		FACEBOOK("facebook");

		/**
		 * Gets the provider' raw value.
		 *
		 * @return {@link String} Returns the string raw value for the provider.
		 */
		public String getProvider() {
			return provider;
		}

		Provider(String provider) {
			this.provider = provider;
		}

		private final String provider;

	}

}
