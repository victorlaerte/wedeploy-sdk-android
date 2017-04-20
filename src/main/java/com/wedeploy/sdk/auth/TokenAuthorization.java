package com.wedeploy.sdk.auth;

import android.content.Intent;
import com.wedeploy.sdk.transport.Request;

/**
 * @author Silvio Santos
 */
public class TokenAuthorization implements Authorization {

	public TokenAuthorization(String token) {
		this.token = token;
	}

	public static Authorization getAuthorizationFromIntent(Intent intent) {
		String[] fragment = intent.getData().getFragment().split("access_token=");

		if (fragment.length > 1) {
			return new TokenAuthorization(fragment[1]);
		}

		return null;
	}

	public String getAuthorizationHeader() {
		return "Bearer " + token;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public Request.Builder authenticate(Request.Builder builder) {
		return builder.header("Authorization", getAuthorizationHeader());
	}

	private String token;

}
