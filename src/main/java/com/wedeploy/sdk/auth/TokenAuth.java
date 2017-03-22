package com.wedeploy.sdk.auth;

import android.content.Intent;
import com.wedeploy.sdk.transport.Request;

/**
 * @author Silvio Santos
 */
public class TokenAuth implements Auth {

	public TokenAuth(String token) {
		this.token = token;
	}

	public static Auth getAuthFromIntent(Intent intent) {
		String[] fragment = intent.getData().getFragment().split("access_token=");

		if (fragment.length > 1) {
			return new TokenAuth(fragment[1]);
		}

		return null;
	}

	public String getAuthorizationHeader() {
		return "Bearer " + token;
	}

	@Override
	public Request.Builder authenticate(Request.Builder builder) {
		return builder.header("Authorization", getAuthorizationHeader());
	}

	private String token;

}
