package com.wedeploy.android.auth;

import android.app.Activity;
import android.content.Intent;
import com.wedeploy.android.WeDeployData;
import com.wedeploy.android.transport.Request;

/**
 * The TokenAuthorization encapsulates a token provided by WeDeploy. It must be provided to requests
 * to WeDeploy that requires authentication.
 *
 * @see com.wedeploy.android.WeDeployAuth#signIn(String, String)
 * @see com.wedeploy.android.WeDeployAuth#signIn(Activity, ProviderAuthorization)
 *
 * @author Silvio Santos
 */
public class TokenAuthorization implements Authorization {

	/**
	 * Constructs a {@link TokenAuthorization} instance.
	 *
	 * @param token A token provided by WeDeploy.
	 */
	public TokenAuthorization(String token) {
		this.token = token;
	}

	/**
	 * Gets an Authorization from a {@link Intent} returned after successfully executing
	 * {@link com.wedeploy.android.WeDeployAuth#signIn(Activity, ProviderAuthorization)}
	 *
	 * @param intent The intent sent by the Android framework that contains the authorization token.
	 * @return The authorization object.
	 */
	public static Authorization getAuthorizationFromIntent(Intent intent) {
		String[] fragment = intent.getData().getFragment().split("access_token=");

		if (fragment.length > 1) {
			return new TokenAuthorization(fragment[1]);
		}

		return null;
	}

	/**
	 * Returns the authorization header associated with this token.
	 *
	 * @return The authorization header value.
	 */
	public String getAuthorizationHeader() {
		return "Bearer " + token;
	}

	/**
	 * Returns the token associated with this instance.
	 * @return
	 */
	@Override
	public String getToken() {
		return token;
	}

	/**
	 * Authenticates a request using this instance token.
	 * @param builder
	 * @return
	 */
	@Override
	public Request.Builder authenticate(Request.Builder builder) {
		return builder.header("Authorization", getAuthorizationHeader());
	}

	private String token;

}
