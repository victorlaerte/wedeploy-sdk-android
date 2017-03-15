package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.TokenAuth;

/**
 * @author Silvio Santos
 */
public class Constants {

	public static final String MASTER_TOKEN = "dummyMasterToken";
	public static final TokenAuth AUTH = new TokenAuth(MASTER_TOKEN);

	public static final String AUTH_URL = "http://auth.test.wedeploy.me";
	public static final String DATA_URL = "http://data.test.wedeploy.me";

	public static String USER_ID;
	public static final String NAME = "Silvio Santos";
	public static final String PASSWORD = "test";
	public static final String USERNAME = "silvio.santos@liferay.com";

	public static final String AUTHOR = NAME;
	public static final String MESSAGE = "message";

}
