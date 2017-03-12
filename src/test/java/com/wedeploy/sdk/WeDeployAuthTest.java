package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.wedeploy.sdk.Constants.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Silvio Santos
 */
public class WeDeployAuthTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		deleteUsers();
		createUser();
	}

	@Test
	public void signIn() throws Exception {
		User user = weDeployAuth
			.signIn(USERNAME, PASSWORD);

		assertEquals(NAME, user.getName());
		assertEquals(USERNAME, user.getEmail());
	}

	@Test
	public void updateUser() throws Exception {
		Map<String, String> fields = new HashMap<>();
		fields.put("name", "Silvio Santos 2");

		weDeployAuth
			.updateUser(USER_ID, fields);

		User user = WeDeploy.auth(AUTH_URL)
			.getCurrentUser();

		assertEquals("Silvio Santos 2", user.getName());

		fields.put("name", NAME);
		weDeployAuth.updateUser(USER_ID, fields);
	}

	private static void createUser() {
		User user = WeDeploy.auth(AUTH_URL)
			.createUser(USERNAME, PASSWORD, NAME);

		USER_ID = user.getId();
	}

	private static void deleteUsers() {
		Request.Builder builder = new Request.Builder()
			.url(AUTH_URL)
			.method(RequestMethod.DELETE)
			.header("Authorization", "Bearer " + MASTER_TOKEN)
			.path("users");

		Call<Response> call = new Call<>(
			builder.build(), new OkHttpTransport(), Response.class);

		call.execute();
	}

	private WeDeployAuth weDeployAuth = WeDeploy.auth(AUTH_URL);

}
