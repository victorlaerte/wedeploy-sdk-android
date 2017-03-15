package com.wedeploy.sdk.auth;

import com.wedeploy.sdk.transport.Request;

/**
 * @author Silvio Santos
 */
public class TokenAuth implements Auth {

	public TokenAuth(String token) {
		this.token = token;
	}

	@Override
	public Request.Builder authenticate(Request.Builder builder) {
		return builder.header("Authorization", "Bearer " + token);
	}

	private String token;
}
