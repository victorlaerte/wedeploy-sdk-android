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

package com.wedeploy.android.query.filter;

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
