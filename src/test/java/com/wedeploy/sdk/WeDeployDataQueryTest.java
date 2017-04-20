package com.wedeploy.sdk;

import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.query.SortOrder;
import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.wedeploy.sdk.Constants.AUTHORIZATION;
import static com.wedeploy.sdk.Constants.DATA_URL;
import static com.wedeploy.sdk.DataTestHelper.initDataFromFile;
import static org.junit.Assert.*;

/**
 * @author Silvio Santos
 */
public class WeDeployDataQueryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws WeDeployException {
		initDataFromFile("messages.json");
	}

	@Test
	public void aggregate() throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.aggregate(Aggregation.count("messagesCount", "message"))
			.get("messages")
			.execute();

		JSONObject jsonObject = new JSONObject(response.getBody());

		assertEquals(15, jsonObject.getJSONObject("aggregations")
			.getInt("messagesCount"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void aggregate_withNullValue_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.aggregate(null)
			.get("messages")
			.execute();
	}

	@Test
	public void count() throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.count()
			.get("messages")
			.execute();

		assertEquals("15", response.getBody());
	}

	@Test
	public void highlight() throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.highlight("message")
			.search("messages")
			.execute();

		JSONObject jsonObject = new JSONObject(response.getBody());

		assertTrue(jsonObject.has("highlights"));
	}

	@Test
	public void limit() throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.limit(1)
			.get("messages")
			.execute();

		int total = new JSONArray(response.getBody()).length();

		assertEquals(1, total);
	}

	@Test
	public void offset() throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.offset(10)
			.get("messages")
			.execute();

		int total = new JSONArray(response.getBody()).length();

		assertEquals(5, total);
	}

	@Test
	public void orderBy_ascending() throws WeDeployException {
		orderBy(SortOrder.ASCENDING);
	}

	@Test
	public void orderBy_descending() throws WeDeployException {
		orderBy(SortOrder.DESCENDING);
	}

	@Test(expected = IllegalArgumentException.class)
	public void orderBy_withNullField_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.orderBy(null)
			.get("messages")
			.execute();
	}

	@Test(expected = IllegalArgumentException.class)
	public void orderBy_withNullSortOrder_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.orderBy("field", null)
			.get("messages")
			.execute();
	}

	@Test
	public void where() throws WeDeployException {
		final String filterMessage = "message1";
		Filter filter = Filter.prefix("message1");

		Response response = weDeploy.data(DATA_URL)
			.where(filter)
			.get("messages")
			.execute();

		assertEquals(200, response.getStatusCode());

		JSONArray jsonArray = new JSONArray(response.getBody());
		JSONObject messageJson = jsonArray.getJSONObject(0);

		assertEquals(filterMessage, messageJson.getString("message"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void where_withNullFilter_shouldThrowException() throws WeDeployException {
		weDeploy.data(DATA_URL)
			.where(null)
			.get("messages")
			.execute();
	}

	private void orderBy(SortOrder order) throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.orderBy("message", order)
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

	private WeDeploy weDeploy = new WeDeploy.Builder().build();

}
