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

import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.aggregation.Aggregation;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.wedeploy.android.Constants.AUTHORIZATION;
import static com.wedeploy.android.Constants.DATA_URL;
import static com.wedeploy.android.DataTestHelper.initDataFromFile;
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
	public void fields() throws WeDeployException {
		Response response = weDeploy.data(DATA_URL)
			.fields("message")
			.get("messages")
			.execute();

		String body = response.getBody();
		assertTrue(!body.contains("author") && body.contains("message"));
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

	@Test
	public void wildcard() throws WeDeployException {
		String field = "author";
		String value = "*Hiro*";
		String expectedValue = "Ygor Hiroshi";

		Response response = weDeploy.data(DATA_URL)
			.wildcard(field, value)
			.get("messages")
			.execute();

		assertEquals(200, response.getStatusCode());

		JSONArray jsonArray = new JSONArray(response.getBody());
		JSONObject messageJson = jsonArray.getJSONObject(0);

		assertEquals(expectedValue, messageJson.getString("author"));
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
