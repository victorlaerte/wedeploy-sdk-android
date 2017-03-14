package com.wedeploy.sdk.query;

import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class QueryTest {

	@Test
	public void testQuery_empty() throws Exception {
		Query query = new Query.Builder()
			.fetch()
			.build();

		JSONAssert.assertEquals(
			"{\"type\":\"fetch\"}", query.bodyAsJson(), true);
	}

	@Test
	public void testQuery_withAggregation() throws Exception {
		Query query = new Query.Builder()
			.aggregate("a", "f", "min")
			.aggregate(Aggregation.missing("m", "f"))
			.build();

		JSONAssert.assertEquals(
			"{\"aggregation\":[" +
				"{\"f\":{\"operator\":\"min\",\"name\":\"a\"}}," +
				"{\"f\":{\"operator\":\"missing\",\"name\":\"m\"}}," +
				"]}",
			query.bodyAsJson(), true);
	}

	@Test
	public void testQuery_withFilters() throws Exception {
		String body = new Query.Builder()
			.filter("field", 1)
			.build()
			.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"filter\":[{\"field\":{\"operator\":\"=\",\"value\":1}}]}", body,
			true);

		body = new Query.Builder()
			.filter("field", ">", 1)
			.build()
			.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"filter\":[{\"field\":{\"operator\":\">\",\"value\":1}}]}", body,
			true);

		body = new Query.Builder()
				.filter("field1", 1)
				.filter("field1", 1)
				.filter("field2", "regex", "value")
				.filter(Filter.field("field3", 0.55))
				.filter(Filter.field("field4", "pre", "str"))
				.filter(Filter.not("field5", 1))
				.filter(Filter.not("field7", "!=", 1))
				.filter(Filter.not(Filter.field("field8", 1)))
				.build()
				.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"filter\":[" +
				"{\"field1\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"field1\":{\"operator\":\"=\",\"value\":1}}," +
				"{\"field2\":{\"operator\":\"regex\",\"value\":\"value\"}}," +
				"{\"field3\":{\"operator\":\"=\",\"value\":0.55}}," +
				"{\"field4\":{\"operator\":\"pre\",\"value\":\"str\"}}," +
				"{\"not\":{\"field5\":{\"operator\":\"=\",\"value\":1}}}," +
				"{\"not\":{\"field7\":{\"operator\":\"!=\",\"value\":1}}}," +
				"{\"not\":{\"field8\":{\"operator\":\"=\",\"value\":1}}}" +
			"]}", body, true);
	}

	@Test
	public void testQuery_withHighlight() throws Exception {
		Query search = new Query.Builder()
			.highlight("field1")
			.highlight("field2")
			.highlight("field3")
			.build();

		JSONAssert.assertEquals(
			"{\"highlight\":[\"field1\",\"field2\",\"field3\"]}",
			search.bodyAsJson(), true);
	}

	@Test
	public void testQuery_withLimitAndOffset() throws Exception {
		String body = new Query.Builder()
				.limit(1)
				.offset(2)
				.build()
				.bodyAsJson();

		JSONAssert.assertEquals("{\"limit\":1,\"offset\":2}", body, true);
	}

	@Test
	public void testQuery_withSearch() throws Exception {
		Query query = new Query.Builder()
			.search(Filter.equal("field", "value"))
			.build();

		JSONAssert.assertEquals(
			"{\"search\":[{" +
				"\"field\":{\"operator\":\"=\",\"value\":\"value\"}}]}",
			query.bodyAsJson(), true);

		query = new Query.Builder()
			.search("query")
			.build();

		JSONAssert.assertEquals(
			"{\"search\":[{" +
				"\"*\":{\"operator\":\"match\",\"value\":\"query\"}}]}",
			query.bodyAsJson(), true);

		query = new Query.Builder()
			.search("field", "query")
			.build();

		JSONAssert.assertEquals(
			"{\"search\":[{" +
				"\"field\":{\"operator\":\"match\",\"value\":\"query\"}}]}",
			query.bodyAsJson(), true);

		query = new Query.Builder()
			.search("field", "=", "query")
			.build();

		JSONAssert.assertEquals(
			"{\"search\":[{" +
				"\"field\":{\"operator\":\"=\",\"value\":\"query\"}}]}",
			query.bodyAsJson(), true);

		query = new Query.Builder().search("query")
			.search("query")
			.search("field", "value")
			.search("field", "=", "value")
			.search(Filter.field("field", "value"))
			.build();

		JSONAssert.assertEquals(
			"{\"search\":[" +
				"{\"*\":{\"operator\":\"match\",\"value\":\"query\"}}," +
				"{\"*\":{\"operator\":\"match\",\"value\":\"query\"}}," +
				"{\"field\":{\"operator\":\"match\",\"value\":\"value\"}}," +
				"{\"field\":{\"operator\":\"=\",\"value\":\"value\"}}," +
				"{\"field\":{\"operator\":\"=\",\"value\":\"value\"}},]}",
			query.bodyAsJson(), true);
	}

	@Test
	public void testQuery_withSort() throws Exception {
		String body = new Query.Builder()
				.sort("field")
				.build()
				.bodyAsJson();

		JSONAssert.assertEquals("{\"sort\":[{\"field\":\"asc\"}]}", body, true);

		body = new Query.Builder()
				.sort("field1")
				.sort("field2", "asc")
				.sort("field3", "desc")
				.build()
				.bodyAsJson();

		JSONAssert.assertEquals(
			"{\"sort\":[" +
				"{\"field1\":\"asc\"}," +
				"{\"field2\":\"asc\"}," +
				"{\"field3\":\"desc\"}" +
			"]}", body, true);
	}

	@Test
	public void testQuery_withType() throws Exception {
		Query query = new Query.Builder()
			.type("type")
			.build();

		JSONAssert.assertEquals(
			"{\"type\":\"type\"}", query.bodyAsJson(), true);

		query = new Query.Builder()
			.count()
			.build();

		JSONAssert.assertEquals(
			"{\"type\":\"count\"}", query.bodyAsJson(), true);

		query = new Query.Builder()
			.fetch()
			.build();

		JSONAssert.assertEquals(
			"{\"type\":\"fetch\"}", query.bodyAsJson(), true);
	}

}