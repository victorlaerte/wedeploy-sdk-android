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
import android.content.Intent;
import com.wedeploy.android.transport.Request;

/**
 * The TokenAuthorization encapsulates a token provided by WeDeploy. It must be provided to requests
 * to WeDeploy that requires authentication.
 *
 * @author Silvio Santos
 * @see com.wedeploy.android.WeDeployAuth#signIn(String, String)
 * @see com.wedeploy.android.WeDeployAuth#signIn(Activity, ProviderAuthorization)
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
	 * @param intent The intent sent by the Android framework that contains the authorization
	 * token.
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
	 *
	 * @return
	 */
	@Override
	public String getToken() {
		return token;
	}

	/**
	 * Authenticates a request using this instance token.
	 *
	 * @param builder
	 * @return
	 */
	@Override
	public Request.Builder authenticate(Request.Builder builder) {
		return builder.header("Authorization", getAuthorizationHeader());
	}

	private String token;

}
