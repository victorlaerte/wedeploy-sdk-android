package com.wedeploy.sdk;

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
