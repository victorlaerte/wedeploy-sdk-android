package com.wedeploy.android;

import com.wedeploy.android.auth.TokenAuthorization;

/**
 * @author Silvio Santos
 */
public class Constants {

	public static final String MASTER_TOKEN = "d3147c3c-ec20-4e65-9f9d-e6464c04e299";
	public static final TokenAuthorization AUTHORIZATION = new TokenAuthorization(MASTER_TOKEN);

	public static final String AUTH_URL = "https://auth-apiandroid.wedeploy.sh";
	public static final String DATA_URL = "https://data-apiandroid.wedeploy.sh";
	public static final String EMAIL_URL = "https://email-apiandroid.wedeploy.sh";

	public static final String NAME = "Silvio Santos";
	public static final String EMAIL = "silvio.santos@liferay.com";
	public static final String PASSWORD = "test";

	public static final String AUTHOR = NAME;
	public static final String MESSAGE = "message";
	public static String USER_ID;

}
