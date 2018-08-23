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

package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.filter.Range;
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
	public void testAggregation_nested() throws Exception {
		JSONAssert.assertEquals(
			"{\"field\":{\"name\":\"name\"," +
				"\"aggregation\":[" +
					"{\"field\":{\"name\":\"name\",\"operator\":\"avg\"}}]," +
				"\"operator\":\"terms\"}}",
			Aggregation.terms("name", "field").aggregate(
				Aggregation.avg("name", "field")).bodyAsJson(), true);
	}

	@Test(expected = IllegalStateException.class)
	public void testAggregation_nested_circular_references() throws Exception {
		Aggregation agg = Aggregation.terms("name", "field");
		agg.aggregate(agg);
		agg.bodyAsJson();
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

}
