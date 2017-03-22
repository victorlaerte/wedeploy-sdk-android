package com.wedeploy.sdk.auth;

import org.junit.Test;

import static com.wedeploy.sdk.auth.AuthProvider.Provider.GITHUB;
import static junit.framework.TestCase.assertEquals;

/**
 * @author Silvio Santos
 */
public class AuthProviderTest {

	@Test
	public void getAuthUrl() {
		AuthProvider authProvider = new AuthProvider.Builder()
			.provider(GITHUB)
			.scope("admin")
			.providerScope("email")
			.redirectUri("someredirecturi")
			.build();

		assertEquals("/oauth/authorize?provider=github&scope=admin" +
			"&provider_scope=email&redirect_uri=someredirecturi", authProvider.getAuthUrl());
	}
}
