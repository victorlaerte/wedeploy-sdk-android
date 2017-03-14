package com.wedeploy.sdk.query;

import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class AggregationTest {

	@Test
	public void testAggregation_avg() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"avg\",\"name\":\"name\"}}",
			Aggregation.avg("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_count() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"count\",\"name\":\"name\"}}",
			Aggregation.count("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_distance() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"geoDistance\",\"name\":\"name\"," +
				"\"value\":{\"location\":\"0,0\",\"unit\":\"km\"," +
				"\"ranges\":[" +
					"{\"from\":0},{\"to\":0},{\"to\":0}," +
					"{\"from\":0,\"to\":1},{\"from\":1}" +
			"]}}}",
			Aggregation.distance(
				"name", "field", "0,0", Range.from(0), Range.to(0))
				.range(Range.to(0))
				.range(0, 1)
				.range(Range.from(1))
				.unit("km").bodyAsJson(),
			true);
	}

	@Test
	public void testAggregation_extendedStats() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"extendedStats\",\"name\":\"name\"}}",
			Aggregation.extendedStats("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_histogram() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{" +
				"\"operator\":\"histogram\",\"name\":\"name\",\"value\":10}}",
			Aggregation.histogram("name", "field", 10).bodyAsJson(), true);
	}

	@Test
	public void testAggregation_max() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"max\",\"name\":\"name\"}}",
			Aggregation.max("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_min() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"min\",\"name\":\"name\"}}",
			Aggregation.min("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_missing() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"missing\",\"name\":\"name\"}}",
			Aggregation.missing("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_range() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"range\",\"name\":\"name\"," +
				"\"value\":[" +
					"{\"from\":0},{\"to\":0},{\"to\":0}," +
					"{\"from\":0,\"to\":1},{\"from\":1}" +
			"]}}",
			Aggregation.range("name", "field", Range.from(0), Range.to(0))
				.range(Range.to(0))
				.range(0, 1)
				.range(Range.from(1)).bodyAsJson(),
			true);
	}

	@Test
	public void testAggregation_stats() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"stats\",\"name\":\"name\"}}",
			Aggregation.stats("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_sum() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"sum\",\"name\":\"name\"}}",
			Aggregation.sum("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testAggregation_terms() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"operator\":\"terms\",\"name\":\"name\"}}",
			Aggregation.terms("name", "field").bodyAsJson(), true);
	}

	@Test
	public void testToString() {
		Aggregation aggregation = Aggregation.avg("name", "field");

		Query query = new Query.Builder()
			.aggregate(aggregation)
			.build();

		Assert.assertEquals(query.toString(), aggregation.toString());
	}

}