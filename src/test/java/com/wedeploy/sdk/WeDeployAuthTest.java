package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.auth.TokenAuth;
import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.wedeploy.sdk.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Silvio Santos
 */
public class WeDeployAuthTest {

	@BeforeClass
	public static void setUpBeforeClass() throws WeDeployException {
		deleteUsers();
		USER_ID = createUser(EMAIL, PASSWORD, NAME);
	}

	@Test
	public void signIn() throws Exception {
		Response response = weDeploy.auth(AUTH_URL)
			.signIn(EMAIL, PASSWORD)
			.execute();

		JSONObject jsonBody = new JSONObject(response.getBody());
		String token = jsonBody.getString("access_token");

		assertNotNull(token);
	}

	@Test(expected = IllegalArgumentException.class)
	public void signIn_withNullEmail_shouldThrowException() {
		weDeploy.auth(AUTH_URL)
			.auth(AUTH)
			.signIn(null, PASSWORD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void signIn_withNullPassword_shouldThrowException() {
		weDeploy.auth(AUTH_URL)
			.auth(AUTH)
			.signIn(EMAIL, null);
	}

	@Test
	public void signOut() throws WeDeployException {
		Response response = weDeploy.auth(AUTH_URL)
			.signIn(EMAIL, PASSWORD)
			.execute();

		JSONObject jsonBody = new JSONObject(response.getBody());
		String token = jsonBody.getString("access_token");

		weDeploy.auth(AUTH_URL)
			.auth(new TokenAuth(token))
			.signOut()
			.execute();
	}

	@Test
	public void deleteUser() throws WeDeployException {
		String id = createUser("test@wedeploy.me", "123456", "Test Test");

		weDeploy.auth(AUTH_URL)
			.auth(AUTH)
			.deleteUser(id)
			.execute();
	}

	@Test
	public void updateUser() throws Exception {
		Response response = weDeploy.auth(AUTH_URL)
			.signIn(EMAIL, PASSWORD)
			.execute();

		JSONObject jsonBody = new JSONObject(response.getBody());
		String token = jsonBody.getString("access_token");
		Auth auth = new TokenAuth(token);

		JSONObject fields = new JSONObject();
		fields.put("name", "Silvio Santos 2");

		weDeploy.auth(AUTH_URL)
			.auth(auth)
			.updateUser(USER_ID, fields)
			.execute();

		response = weDeploy.auth(AUTH_URL)
			.auth(auth)
			.getCurrentUser()
			.execute();

		jsonBody = new JSONObject(response.getBody());
		assertEquals("Silvio Santos 2", jsonBody.getString("name"));

		fields.put("name", NAME);
		weDeploy.auth(AUTH_URL)
			.auth(auth)
			.updateUser(USER_ID, fields)
			.execute();
	}

	@Test
	public void sendPasswordResetEmail() throws WeDeployException {
		weDeploy.auth(AUTH_URL)
			.sendPasswordResetEmail(EMAIL)
			.execute();
	}

	private static String createUser(String email, String password, String name)
		throws WeDeployException {

		Response response = weDeploy.auth(AUTH_URL)
			.createUser(email, password, name)
			.execute();

		assertEquals(200, response.getStatusCode());

		JSONObject jsonObject = new JSONObject(response.getBody());

		return jsonObject.getString("id");
	}

	private static void deleteUsers() {
		try {
			Request.Builder builder = new Request.Builder()
				.url(AUTH_URL)
				.method(RequestMethod.DELETE)
				.header("Authorization", "Bearer " + MASTER_TOKEN)
				.path("users");

			OkHttpTransport transport = new OkHttpTransport.Builder().build();

			Call<Response> call = new Call<>(
				builder.build(), transport, transport, Response.class);

			call.execute();
		}
		catch (Exception e) {
		}
	}

	private static WeDeploy weDeploy = new WeDeploy.Builder().build();

}
