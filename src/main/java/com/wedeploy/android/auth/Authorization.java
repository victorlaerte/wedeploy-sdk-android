package com.wedeploy.android.auth;

import com.wedeploy.android.transport.Request;

/**
 * @author Silvio Santos
 */
public interface Authorization {

	String getAuthorizationHeader();

	String getToken();

	Request.Builder authenticate(Request.Builder builder);

}
