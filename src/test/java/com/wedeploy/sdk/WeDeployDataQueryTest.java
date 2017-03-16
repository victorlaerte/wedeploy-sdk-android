package com.wedeploy.sdk;

import com.wedeploy.sdk.query.SortOrder;
import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.wedeploy.sdk.Constants.AUTH;
import static com.wedeploy.sdk.Constants.DATA_URL;
import static com.wedeploy.sdk.DataTestHelper.initDataFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

	@Test(expected = IllegalArgumentException.class)
	public void aggregate_withNullValue_shouldThrowException() {
		WeDeploy.data(DATA_URL)
			.aggregate(null)
			.get("messages")
			.execute();
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
	public void sort_ascending() throws Exception {
		sort(SortOrder.ASCENDING);
	}

	@Test
	public void sort_descending() throws Exception {
		sort(SortOrder.DESCENDING);
	}

	@Test(expected = IllegalArgumentException.class)
	public void sort_withNullField_shouldThrowException() {
		WeDeploy.data(DATA_URL)
			.sort(null)
			.get("messages")
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void sort_withNullSortOrder_shouldThrowException() {
		WeDeploy.data(DATA_URL)
			.sort("field", null)
			.get("messages")
			.execute();
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

	@Test(expected = IllegalArgumentException.class)
	public void where_withNullFilter_shouldThrowException() {
		WeDeploy.data(DATA_URL)
			.where(null)
			.get("messages")
			.execute();
	}

	private void sort(SortOrder order) {
		Response response = WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.sort("message", order)
			.get("messages")
			.execute();

		JSONArray messagesArray = new JSONArray(response.getBody());

		for (int i = 0; i < messagesArray.length() - 1; i++) {
			String message1 = messagesArray.getJSONObject(i).getString("message");
			String message2 = messagesArray.getJSONObject(i + 1).getString("message");

			int compare = message1.compareTo(message2);

			if ((compare > 0) && (order == SortOrder.ASCENDING) ||
				(compare < 0) && (order == SortOrder.DESCENDING)) {

				fail();
			}
		}
	}

}
