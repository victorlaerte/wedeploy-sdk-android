package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.TokenAuthorization;

/**
 * @author Silvio Santos
 */
public class Constants {

	public static final String MASTER_TOKEN = "dummyMasterToken";
	public static final TokenAuthorization AUTHORIZATION = new TokenAuthorization(MASTER_TOKEN);

	public static final String AUTH_URL = "http://auth.test.wedeploy.me";
	public static final String DATA_URL = "http://data.test.wedeploy.me";
	public static final String EMAIL_URL = "http://email.test.wedeploy.me";

	public static final String NAME = "Silvio Santos";
	public static final String EMAIL = "silvio.santos@liferay.com";
	public static final String PASSWORD = "test";

	public static final String AUTHOR = NAME;
	public static final String MESSAGE = "message";
	public static String USER_ID;

}
