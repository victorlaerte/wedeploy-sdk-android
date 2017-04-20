package com.wedeploy.sdk.auth;

import com.wedeploy.sdk.transport.Request;

/**
 * @author Silvio Santos
 */
public interface Authorization {

	String getAuthorizationHeader();

	String getToken();

	Request.Builder authenticate(Request.Builder builder);

}
