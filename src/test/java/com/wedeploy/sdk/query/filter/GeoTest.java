package com.wedeploy.sdk.query.filter;

import com.wedeploy.sdk.query.filter.Geo;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class GeoTest {

	@Test
	public void testGeo_withBbox() throws Exception {
		JSONAssert.assertEquals(
			"{\"type\":\"envelope\",\"coordinates\":[\"0,0\",\"0,0\"]}",
			Geo.boundingBox("0,0", "0,0").bodyAsJson(), true);
	}

	@Test
	public void testGeo_withCircle() throws Exception {
		JSONAssert.assertEquals(
			"{\"type\":\"circle\",\"coordinates\":\"0,0\",\"radius\":\"1m\"}",
			Geo.circle("0,0", "1m").bodyAsJson(), true);
	}

	@Test
	public void testGeo_withLine() throws Exception {
		JSONAssert.assertEquals(
			"{\"type\":\"linestring\",\"coordinates\":[\"0,0\",\"0,0\"]}",
			Geo.line("0,0", "0,0").bodyAsJson(), true);
	}

	@Test
	public void testGeo_withPoint() throws Exception {
		JSONAssert.assertEquals("[0,0]", Geo.point(0, 0).bodyAsJson(), true);
	}

	@Test
	public void testGeo_withPolygon() throws Exception {
		JSONAssert.assertEquals(
			"{\"type\":\"polygon\"," +
				"\"coordinates\":[[\"0,0\",\"0,0\"],[\"0,0\",\"0,0\"]]}",
			Geo.polygon("0,0", "0,0").hole("0,0", "0,0").bodyAsJson(), true);
	}

}