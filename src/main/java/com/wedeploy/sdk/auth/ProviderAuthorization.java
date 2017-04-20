package com.wedeploy.sdk.auth;

import static com.wedeploy.sdk.util.Validator.checkNotNull;

/**
 * @author Silvio Santos
 */
public class ProviderAuthorization {

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

	public Provider getProvider() {
		return provider;
	}

	public String getProviderScope() {
		return providerScope;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

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

		public ProviderAuthorization build() {
			checkNotNull(provider, "Provider must be specified");
			checkNotNull(redirectUri, "Redirect URI must be specified");

			return new ProviderAuthorization(this);
		}

		public Builder provider(Provider provider) {
			this.provider = provider;
			return this;
		}

		public Builder providerScope(String providerScope) {
			this.providerScope = providerScope;
			return this;
		}

		public Builder redirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}

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
