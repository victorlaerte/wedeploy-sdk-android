package com.wedeploy.android.auth;

import android.app.Activity;

import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * This class encapsulates the provider information for executing an OAuth authentication.
 * @see com.wedeploy.android.WeDeployAuth#signIn(Activity, ProviderAuthorization)
 * @author Silvio Santos
 */
public class ProviderAuthorization {

	/**
	 * Gets authorization url based on the provider configuration.
	 * @return The redirect url
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
	 * Gets the provider
	 * @return {@link Provider}
	 */
	public Provider getProvider() {
		return provider;
	}

	/**
	 * Gets the provider's authorization scope
	 * @return The provider scope
	 */
	public String getProviderScope() {
		return providerScope;
	}

	/**
	 * Gets the redirect uri
	 * @return The redirect uri
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * Gets the WeDeploy authorization scope.
	 * @return
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

	public static class Builder {

		/**
		 * Buids a new isntance of ProviderAuthorization.
		 * @return {@link ProviderAuthorization}
		 */
		public ProviderAuthorization build() {
			checkNotNull(provider, "Provider must be specified");
			checkNotNull(redirectUri, "Redirect URI must be specified");

			return new ProviderAuthorization(this);
		}

		/**
		 * Sets the provider to connect to.
		 * @param provider
		 * @return
		 */
		public Builder provider(Provider provider) {
			this.provider = provider;
			return this;
		}

		/**
		 * Sets the provider authorization scope.
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
		 * @param redirectUri The redirect uri.
		 * @return {@link Builder} Returns the object itself, so calls can be chained.
		 */
		public Builder redirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}

		/**
		 * Sets the WeDeploy authorization scope.
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

	public enum Provider {
		GITHUB("github"), GOOGLE("google"), FACEBOOK("facebook");

		public String getProvider() {
			return provider;
		}

		Provider(String provider) {
			this.provider = provider;
		}

		private final String provider;

	}

}
