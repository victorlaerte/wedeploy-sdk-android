package com.wedeploy.sdk;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Silvio Santos
 */
public class WeDeployAuthTest {

	private static final String ID = "207267826573577323";
	private static final String NAME = "Silvio Santos";
	private static final String PASSWORD = "123456";
	private static final String USERNAME = "silvio.santos@liferay.com";
	private static final String URL = "http://auth.silvio.wedeploy.io";

	@Test
	public void signIn() throws Exception {
		User user = WeDeploy.auth(URL).signIn(USERNAME, PASSWORD);

		assertEquals("Silvio Santos", user.getName());
		assertEquals(USERNAME, user.getEmail());
	}

	@Test
	public void updateUser() throws Exception {
		Map<String, String> fields = new HashMap<>();
		fields.put("name", "Silvio Santos 2");

		WeDeploy.auth(URL)
			.updateUser(ID, fields);

		User user = WeDeploy.auth(URL)
			.getCurrentUser();

		assertEquals("Silvio Santos 2", user.getName());

		fields.put("name", NAME);
		WeDeploy.auth(URL)
			.updateUser(ID, fields);
	}

}
