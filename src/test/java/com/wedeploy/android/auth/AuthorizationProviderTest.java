package com.wedeploy.android.auth;

import org.junit.Test;

import static com.wedeploy.android.auth.ProviderAuthorization.Provider.GITHUB;
import static junit.framework.TestCase.assertEquals;

/**
 * @author Silvio Santos
 */
public class AuthorizationProviderTest {

	@Test
	public void getAuthUrl() {
		ProviderAuthorization authProvider = new ProviderAuthorization.Builder()
			.provider(GITHUB)
			.scope("admin")
			.providerScope("email")
			.redirectUri("someredirecturi")
			.build();

		assertEquals("/oauth/authorize?provider=github&scope=admin" +
			"&provider_scope=email&redirect_uri=someredirecturi", authProvider.getAuthUrl());
	}
}
