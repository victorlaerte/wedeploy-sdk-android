package com.wedeploy.sdk;

import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.wedeploy.sdk.Constants.*;
import static com.wedeploy.sdk.DataTestHelper.deleteData;
import static com.wedeploy.sdk.query.filter.Filter.any;
import static com.wedeploy.sdk.query.filter.Filter.equal;
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
	public void create() {
		Response response = createMessageObject();
		assertEquals(200, response.getStatusCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void create_withNullData_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
			.create("resourcePath", (JSONObject)null)
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void create_withNullCollection_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
			.create(null, new JSONArray())
			.execute();
	}

	@Test
	public void delete() throws WeDeployException {
		createMessageObject();

		Response response = weDeploy.data(DATA_URL)
			.auth(AUTH)
			.delete("messages/" + id)
			.execute();

		assertEquals(204, response.getStatusCode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void delete_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
			.delete(null)
			.execute();
	}

	@Test
	public void get() throws WeDeployException {
		createMessageObject();

		Response response = weDeploy.data(DATA_URL)
			.auth(AUTH)
			.get("messages")
			.execute();

		assertEquals(200, response.getStatusCode());

		response = weDeploy.data(DATA_URL)
			.auth(AUTH)
			.get("messages/" + id)
			.execute();

		assertEquals(200, response.getStatusCode());

		response = weDeploy.data(DATA_URL)
			.auth(AUTH)
			.get(String.format("messages/%s/author", id))
			.execute();

		assertEquals(200, response.getStatusCode());
		assertEquals("\"" + AUTHOR + "\"", response.getBody());
	}

	@Test(expected = IllegalArgumentException.class)
	public void get_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
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
			.auth(AUTH)
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
			.auth(AUTH)
			.replace("resourcePath", null)
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void replace_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
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
			.auth(AUTH)
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
			.auth(AUTH)
			.update("resourcePath", null)
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void update_withNullResourcePath_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
			.update(null, new JSONObject())
			.execute();
	}

	@Test
	public void search() throws WeDeployException {
		DataTestHelper.initDataFromFile("messages.json");

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
			.auth(AUTH)
			.search(null)
			.execute();
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
	}

	@Test
	public void watch_withFilter() throws Exception {
		final Object[] changesPayload = new Object[1];
		final CountDownLatch latch = new CountDownLatch(1);

		RealTime realTime = weDeploy.data(DATA_URL)
			.auth(AUTH)
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
	}

	@Test(expected = IllegalArgumentException.class)
	public void search_withNullCollection_shouldThrowException() {
		weDeploy.data(DATA_URL)
			.auth(AUTH)
			.watch(null);
	}

	public JSONObject getMessageObject(String id) {
		Response response = null;
		try {
			response = weDeploy.data(DATA_URL)
				.auth(AUTH)
				.get("messages/" + id)
				.execute();
		}
		catch (WeDeployException e) {
			fail(e.getMessage());
		}

		return new JSONObject(response.getBody());
	}

	private Response createMessageObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("author", AUTHOR);
		jsonObject.put("message", MESSAGE);

		Response response = null;

		try {
			response = weDeploy.data(DATA_URL)
				.auth(AUTH)
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

	private String id;
	private WeDeploy weDeploy = new WeDeploy.Builder().build();

}
