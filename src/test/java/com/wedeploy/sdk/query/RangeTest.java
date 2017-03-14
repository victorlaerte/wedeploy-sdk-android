package com.wedeploy.sdk.query;

import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class RangeTest {

	@Test
	public void testRange() throws Exception {
		JSONAssert.assertEquals(
			"{\"from\":1}", Range.from(1).bodyAsJson(), true);
		JSONAssert.assertEquals("{\"to\":1}", Range.to(1).bodyAsJson(), true);
		JSONAssert.assertEquals(
			"{\"from\":1,\"to\":2}", Range.range(1, 2).bodyAsJson(), true);
	}

	@Test
	public void testToString() {
		Range range = Range.range(1, 2);

		Assert.assertEquals(range.bodyAsJson(), range.toString());
	}

}