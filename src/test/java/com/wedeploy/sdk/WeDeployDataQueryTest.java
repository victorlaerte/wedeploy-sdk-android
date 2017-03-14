package com.wedeploy.sdk;

import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.wedeploy.sdk.Constants.AUTH;
import static com.wedeploy.sdk.Constants.DATA_URL;
import static com.wedeploy.sdk.DataTestHelper.initDataFromFile;
import static org.junit.Assert.assertEquals;

/**
 * @author Silvio Santos
 */
public class WeDeployDataQueryTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		initDataFromFile("messages.json");
	}

	@Test
	public void aggregate() {
		Response response = WeDeploy.data(DATA_URL)
			.aggregate(Aggregation.count("messagesCount", "message"))
			.get("messages")
			.execute();

		JSONObject jsonObject = new JSONObject(response.getBody());

		assertEquals(15, jsonObject.getJSONObject("aggregations")
			.getInt("messagesCount"));
	}

	@Test
	public void limit() throws Exception {
		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.limit(1)
			.get("messages")
			.execute();

		int total = new JSONArray(response.getBody()).length();

		assertEquals(1, total);
	}

	@Test
	public void offset() throws Exception {
		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.offset(10)
			.get("messages")
			.execute();

		int total = new JSONArray(response.getBody()).length();

		assertEquals(5, total);
	}

	@Test
	public void where() {
		final String filterMessage = "message1";
		Filter filter = Filter.prefix("message1");

		Response response = WeDeploy.data(DATA_URL)
			.where(filter)
			.get("messages")
			.execute();

		assertEquals(200, response.getStatusCode());

		JSONArray jsonArray = new JSONArray(response.getBody());
		JSONObject messageJson = jsonArray.getJSONObject(0);

		assertEquals(filterMessage, messageJson.getString("message"));
	}

}
