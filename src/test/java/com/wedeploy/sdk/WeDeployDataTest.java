package com.wedeploy.sdk;

import com.wedeploy.sdk.transport.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.wedeploy.sdk.Constants.*;
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

	@Test
	public void create() {
		Response response = createMessageObject();
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void delete() throws Exception {
		createMessageObject();

		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.delete("messages/" + id)
			.execute();

		assertEquals(204, response.getStatusCode());
	}

	@Test
	public void replace() throws Exception {
		createMessageObject();

		final String message = "message21";

		JSONObject data = new JSONObject();
		data.put("message", message);

		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.replace("messages/" + id, data)
			.execute();

		assertEquals(204, response.getStatusCode());

		JSONObject updatedMessageObject = getMessageObject(id);
		assertEquals(message, updatedMessageObject.getString("message"));
		assertNull(updatedMessageObject.opt("author"));
	}

	@Test
	public void update() throws Exception {
		createMessageObject();

		final String message = "message21";

		JSONObject data = new JSONObject();
		data.put("message", message);

		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.update("messages/" + id, data)
			.execute();

		assertEquals(204, response.getStatusCode());

		JSONObject updatedMessageObject = getMessageObject(id);
		assertEquals(message, updatedMessageObject.getString("message"));
		assertEquals(AUTHOR, updatedMessageObject.opt("author"));
	}

	@Test
	public void search() throws Exception {
		DataTestHelper.initDataFromFile("messages.json");

		Response response = WeDeploy.data(DATA_URL)
			.where(any("message", "message1", "message5")
				.and(equal("author", "Silvio Santos")))
			.search("messages")
			.execute();

		assertEquals(200, response.getStatusCode());
		assertEquals(1, new JSONObject(response.getBody()).getInt("total"));
	}

	@Test
	public void watch() throws Exception {
		final Object[] createPayload = new Object[1];
		final CountDownLatch latch = new CountDownLatch(1);

		RealTime realTime = WeDeploy.data(DATA_URL)
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

		RealTime realTime = WeDeploy.data(DATA_URL)
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

	public JSONObject getMessageObject(String id) {
		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.get("messages/" + id)
			.execute();

		return new JSONObject(response.getBody());
	}

	private Response createMessageObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("author", AUTHOR);
		jsonObject.put("message", MESSAGE);

		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.create("messages", jsonObject)
			.execute();

		JSONObject data = new JSONObject(response.getBody());
		this.id = data.getString("id");

		return response;
	}

	private void deleteData() {
		WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.delete("")
			.execute();
	}

	private String id;

}
