/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Liferay, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.auth.TokenAuthorization;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.OkHttpTransport;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.wedeploy.android.Constants.*;
import static org.junit.Assert.*;

/**
 * @author Silvio Santos
 */
public class WeDeployAuthTest {

	@BeforeClass
	public static void setUpBeforeClass() throws WeDeployException {
		deleteUsers();
		USER_ID = createUser(EMAIL, PASSWORD, NAME);
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

	@Test
	public void auth_withCredentials() throws WeDeployException {
		MultiMap<String> headers = TestHelper.getHeaders(AUTH_URL, AUTHORIZATION, true);

		Assert.assertTrue(headers.containsKey("Authorization"));
	}

	@Test
	public void auth_withoutCredentials() throws WeDeployException {
		MultiMap<String> headers = TestHelper.getHeaders(AUTH_URL, AUTHORIZATION, false);

		assertFalse(headers.containsKey("Authorization"));
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
			.authorization(AUTHORIZATION)
			.signIn(null, PASSWORD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void signIn_withNullPassword_shouldThrowException() {
		weDeploy.auth(AUTH_URL)
			.authorization(AUTHORIZATION)
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
			.authorization(new TokenAuthorization(token))
			.signOut()
			.execute();
	}

	@Test
	public void deleteUser() throws WeDeployException {
		String id = createUser("testdelete@wedeploy.me", "123456", "Test Test");

		weDeploy.auth(AUTH_URL)
			.authorization(AUTHORIZATION)
			.deleteUser(id)
			.execute();
	}

	@Test
	public void getAllUsers() throws WeDeployException {
		List<String> ids = new ArrayList<>();
		ids.add(USER_ID);
		ids.add(createUser("testgetall@wedeploy.me", "123456", "Test Test"));
		ids.add(createUser("testgetall2@wedeploy.me", "123456", "Test Test2"));

		Response response = weDeploy.auth(AUTH_URL)
			.authorization(AUTHORIZATION)
			.getAllUsers()
			.execute();

		JSONArray jsonArray = new JSONArray(response.getBody());

		assertEquals(3, jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");

			assertTrue(ids.contains(id));
		}
	}

	@Test
	public void updateUser() throws Exception {
		Response response = weDeploy.auth(AUTH_URL)
			.signIn(EMAIL, PASSWORD)
			.execute();

		JSONObject jsonBody = new JSONObject(response.getBody());
		String token = jsonBody.getString("access_token");
		Authorization authorization = new TokenAuthorization(token);

		JSONObject fields = new JSONObject();
		fields.put("name", "Silvio Santos 2");

		weDeploy.auth(AUTH_URL)
			.authorization(authorization)
			.updateUser(USER_ID, fields)
			.execute();

		response = weDeploy.auth(AUTH_URL)
			.authorization(authorization)
			.getCurrentUser()
			.execute();

		jsonBody = new JSONObject(response.getBody());
		assertEquals("Silvio Santos 2", jsonBody.getString("name"));

		fields.put("name", NAME);
		weDeploy.auth(AUTH_URL)
			.authorization(authorization)
			.updateUser(USER_ID, fields)
			.execute();
	}

	@Test
	public void sendPasswordResetEmail() throws WeDeployException {
		weDeploy.auth(AUTH_URL)
			.sendPasswordResetEmail(EMAIL)
			.execute();
	}

	private static WeDeploy weDeploy = new WeDeploy.Builder().build();

}
