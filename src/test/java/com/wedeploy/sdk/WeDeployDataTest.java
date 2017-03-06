package com.wedeploy.sdk;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Silvio Santos
 */
public class WeDeployDataTest {

	private static final String PASSWORD = "123456";
	private static final String USERNAME = "silvio.santos@liferay.com";
	private static final String AUTHOR = "Silvio";
	private static final String MESSAGE = "message20";
	private static String URL = "http://data.silvio.wedeploy.io";
	private String id;

	@After
	public void tearDown() throws Exception {
		if (id != null) {
			deleteObject(id);
		}
	}

	@Test
	public void createAndDelete() throws Exception {
		Response response = createObject();
		assertEquals(200, response.getStatusCode());

		response = deleteObject(id);
		assertEquals(204, response.getStatusCode());

		id = null;
	}

	@Test
	public void update() throws Exception {
		createObject();

		final String message = "message21";

		JSONObject data = new JSONObject();
		data.put("message", message);

		Response response = WeDeploy.data(URL)
			.auth("silvio.santos@liferay.com", "123456")
			.update("messages/" + id, data)
			.execute();

		assertEquals(204, response.getStatusCode());

		String responseBody = WeDeploy.data(URL)
			.auth("silvio.santos@liferay.com", "123456")
			.get("messages/" + id)
			.execute()
			.getBody();

		JSONObject responseData = new JSONObject(responseBody);
		assertEquals(AUTHOR, responseData.get("author"));
		assertEquals(message, responseData.get("message"));
	}

	private Response createObject() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("author", AUTHOR);
		jsonObject.put("message", MESSAGE);

		Response response = WeDeploy.data(URL)
			.auth(USERNAME, PASSWORD)
			.create("messages", jsonObject)
			.execute();

		assertEquals(200, response.getStatusCode());

		JSONObject data = new JSONObject(response.getBody());
		this.id = data.getString("id");

		return response;
	}

	private Response deleteObject(String id) throws Exception {
		Response response = WeDeploy.data(URL)
			.auth("silvio.santos@liferay.com", "123456")
			.delete("messages/" + id)
			.execute();

		assertEquals(204, response.getStatusCode());

		return response;
	}

}