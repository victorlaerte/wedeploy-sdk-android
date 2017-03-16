package com.wedeploy.sdk.auth;

import com.wedeploy.sdk.transport.Request;

/**
 * @author Silvio Santos
 */
public interface Auth {

	String getAuthorizationHeader();

	Request.Builder authenticate(Request.Builder builder);

}
