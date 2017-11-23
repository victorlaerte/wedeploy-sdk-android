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
import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.Transport;
import org.junit.Before;
import org.junit.Test;

import static com.wedeploy.android.Constants.AUTHORIZATION;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Silvio Santos
 */
public class WeDeployServiceTest {

	@Before
	public void setUp() {
		weDeploy = new WeDeploy.Builder()
			.transport(new Transport() {
				@Override
				public Object send(Request request) {
					WeDeployServiceTest.this.request = request;

					return new Response.Builder()
						.succeeded(true)
						.statusMessage("OK")
						.statusCode(200)
						.build();
				}
			})
			.build();
	}

	@Test
	public void request_withCredentials() throws WeDeployException {
		MultiMap<String> headers =
			TestHelper.getHeaders("http://someurl.io", AUTHORIZATION, true);

		assertTrue(headers.containsKey("Authorization"));
	}

	@Test
	public void request_withoutCredentials() throws WeDeployException {
		MultiMap<String> headers =
			TestHelper.getHeaders("http://someurl.io", AUTHORIZATION, false);

		assertFalse(headers.containsKey("Authorization"));
	}

	@Test
	public void request_withParamPathAndBody() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.param("param1", "value1")
			.param("param2", "value2")
			.path("/a/url/path")
			.body("body")
			.post()
			.execute();

		assertEquals("value1", request.getParams().get("param1"));
		assertEquals("value2", request.getParams().get("param2"));
		assertEquals("/a/url/path", request.getPath());
		assertEquals("body", request.getBody());

		assertEquals(RequestMethod.POST, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void request_withForm() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.form("param1", "value1")
			.post()
			.execute();

		assertEquals("value1", request.getForms().get("param1").get(0));

		assertEquals(RequestMethod.POST, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void delete() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.delete()
			.execute();

		assertEquals(RequestMethod.DELETE, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void get() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.get()
			.execute();

		assertEquals(RequestMethod.GET, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void patch() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.patch()
			.execute();

		assertEquals(RequestMethod.PATCH, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void post() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.post()
			.execute();

		assertEquals(RequestMethod.POST, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void put() throws WeDeployException {
		Response response = weDeploy.url("http://someurl.io")
			.put()
			.execute();

		assertEquals(RequestMethod.PUT, request.getMethod());
		assertEquals(200, response.getStatusCode());
	}

	private Request request;
	private WeDeploy weDeploy;

}
