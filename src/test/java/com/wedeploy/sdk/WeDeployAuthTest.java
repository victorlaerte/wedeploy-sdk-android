package com.wedeploy.sdk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Silvio Santos
 */
public class WeDeployAuthTest {

	private static final String PASSWORD = "123456";
	private static final String USERNAME = "silvio.santos@liferay.com";
	private static final String URL = "http://auth.silvio.wedeploy.io";

	@Test
	public void signIn() throws Exception {
		User user = WeDeploy.auth(URL).signIn(USERNAME, PASSWORD);

		assertEquals("Silvio Santos", user.getName());
		assertEquals(USERNAME, user.getEmail());
	}

}
