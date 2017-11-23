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
import com.wedeploy.android.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.wedeploy.android.Constants.*;
import static com.wedeploy.android.TestHelper.deleteData;
import static com.wedeploy.android.query.filter.Filter.any;
import static com.wedeploy.android.query.filter.Filter.equal;
import static org.junit.Assert.*;

/**
 * @author Silvio Santos
 */
public class WeDeployDataTest {

	@Before
	public void setUp() {
		deleteData();
	}

	@Test(expected = IllegalArgumentException.class)
	public void weDeployData_withNullUrl_shouldThrowException() {
		new WeDeployData(weDeploy, null);
	}

	@Test
	public void data_withGlobalAuth() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder()
			.authorization(AUTHORIZATION)
			.build();

		Authorization authorization = weDeploy.data(DATA_URL).getAuthorization();

		assertTrue(authorization == AUTHORIZATION);
	}

	@Test
	public void data_withScopedAuth() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder()
			.authorization(AUTHORIZATION)
			.build();

		Authorization scopedAuthorization = new TokenAuthorization("1");

		Authorization authorization = weDeploy.data(DATA_URL)
			.authorization(scopedAuthorization)
			.getAuthorization();

		assertTrue(scopedAuthorization == authorization);
	}

	@Test
	public void create() {
		Response response = createMessageObject();
		assertEquals(200, response.getStatusCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void create_withNullData_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.create("resourcePath", (JSONObject)null)
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void create_withNullCollection_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.create(null, new JSONArray())
			.execute();
	}

	@Test
	public void delete() throws WeDeployException {
		createMessageObject();

		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.delete("messages/" + id)
			.execute();

		assertEquals(204, response.getStatusCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void delete_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.delete(null)
			.execute();
	}

	@Test
	public void get() throws WeDeployException {
		createMessageObject();

		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.get("messages")
			.execute();

		assertEquals(200, response.getStatusCode());

		response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.get("messages/" + id)
			.execute();

		assertEquals(200, response.getStatusCode());

		response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.get(String.format("messages/%s/author", id))
			.execute();

		assertEquals(200, response.getStatusCode());
		assertEquals("\"" + AUTHOR + "\"", response.getBody());
	}

	@Test(expected = IllegalArgumentException.class)
	public void get_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.get(null)
			.execute();
	}

	@Test
	public void replace() throws WeDeployException {
		createMessageObject();

		final String message = "message21";

		JSONObject data = new JSONObject();
		data.put("message", message);

		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.replace("messages/" + id, data)
			.execute();

		assertEquals(204, response.getStatusCode());

		JSONObject updatedMessageObject = getMessageObject(id);
		assertEquals(message, updatedMessageObject.getString("message"));
		assertNull(updatedMessageObject.opt("author"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void replace_withNullData_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.replace("resourcePath", null)
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void replace_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.replace(null, new JSONObject())
			.execute();
	}

	@Test
	public void update() throws WeDeployException {
		createMessageObject();

		final String message = "message21";

		JSONObject data = new JSONObject();
		data.put("message", message);

		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.update("messages/" + id, data)
			.execute();

		assertEquals(204, response.getStatusCode());

		JSONObject updatedMessageObject = getMessageObject(id);
		assertEquals(message, updatedMessageObject.getString("message"));
		assertEquals(AUTHOR, updatedMessageObject.opt("author"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void update_withNullData_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.update("resourcePath", null)
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void update_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.update(null, new JSONObject())
			.execute();
	}

	@Test
	public void search() throws WeDeployException {
		TestHelper.initDataFromFile("messages.json");

		Response response = weDeploy.data(DATA_URL)
			.where(any("message", "message1", "message5")
				.and(equal("author", "Silvio Santos")))
			.search("messages")
			.execute();

		assertEquals(200, response.getStatusCode());
		assertEquals(1, new JSONObject(response.getBody()).getInt("total"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void search_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.search(null)
			.execute();
	}

	@Test
	public void request_shouldNotReusePreviousQuery() {
		WeDeployData data = weDeploy.data(DATA_URL);

		data.limit(1)
			.get("messages");

		assertTrue(data.getOrCreateQueryBuilder().build().body().isEmpty());
	}

	@Test
	public void watch() throws Exception {
		final Object[] createPayload = new Object[1];
		final CountDownLatch latch = new CountDownLatch(1);

		RealTime realTime = weDeploy.data(DATA_URL)
			.where(equal("message", MESSAGE))
			.watch("messages");

		realTime
			.on("connect", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {
					createMessageObject();
				}
			})
			.on("create", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {
					createPayload[0] = args[0];

					latch.countDown();
				}
			});

		latch.await(5000, TimeUnit.MILLISECONDS);
		assertNotNull(createPayload[0]);

		realTime.close();
	}

	@Test
	public void watch_withFilter() throws Exception {
		final Object[] changesPayload = new Object[1];
		final CountDownLatch latch = new CountDownLatch(1);

		RealTime realTime = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.watch("messages");

		realTime
			.on("connect", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {
					createMessageObject();
					createMessageObject();
					createMessageObject();
				}
			})
			.on("changes", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... payload) {
					changesPayload[0] = payload;

					latch.countDown();
				}
			})
			.on("fail", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... payload) {
					fail();
				}
			});

		latch.await(5000, TimeUnit.MILLISECONDS);
		assertNotNull(changesPayload[0]);

		realTime.close();
	}

	@Test(expected = IllegalArgumentException.class)
	public void search_withNullCollection_shouldThrowException() {
		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.watch(null);
	}

	private Response createMessageObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("author", AUTHOR);
		jsonObject.put("message", MESSAGE);

		Response response = null;

		try {
			response = weDeploy.data(DATA_URL)
				.authorization(AUTHORIZATION)
				.create("messages", jsonObject)
				.execute();
		}
		catch (WeDeployException e) {
			fail(e.getMessage());
		}

		JSONObject data = new JSONObject(response.getBody());
		this.id = data.getString("id");

		return response;
	}

	private JSONObject getMessageObject(String id) {
		Response response = null;
		try {
			response = weDeploy.data(DATA_URL)
				.authorization(AUTHORIZATION)
				.get("messages/" + id)
				.execute();
		}
		catch (WeDeployException e) {
			fail(e.getMessage());
		}

		return new JSONObject(response.getBody());
	}

	private String id;
	private WeDeploy weDeploy = new WeDeploy.Builder().build();

}
