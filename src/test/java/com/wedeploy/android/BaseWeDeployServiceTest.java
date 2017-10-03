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

import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.Request;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Silvio Santos
 */
public class BaseWeDeployServiceTest {

	@Before
	public void setUp() {
		WeDeploy weDeploy = new WeDeploy.Builder().build();
		service = new BaseWeDeployService(weDeploy, "http://someurl.com") {
		};
	}

	@Test
	public void header_withSomeValues() {
		service.header("name1", "value1");
		service.header("name2", "value2");
		service.header("name3", "value3");
		service.header("name3", "value4");

		Request.Builder builder = service.newAuthenticatedRequestBuilder();
		Request request = builder.build();

		MultiMap<String> headers = request.getHeaders();
		assertEquals("value1", headers.get("name1").get(0));
		assertEquals("value2", headers.get("name2").get(0));
		assertEquals("value3", headers.get("name3").get(0));
		assertEquals("value4", headers.get("name3").get(1));
	}

	@Test
	public void url_withoutScheme() {
		WeDeploy weDeploy = new WeDeploy.Builder().build();
		BaseWeDeployService service = new BaseWeDeployService(weDeploy, "someurl.com") {
		};

		Request.Builder builder = service.newAuthenticatedRequestBuilder();
		Request request = builder.build();

		assertEquals("https://someurl.com", request.getUrl());
	}

	@Test
	public void url_withHttpScheme() {
		WeDeploy weDeploy = new WeDeploy.Builder().build();
		BaseWeDeployService service = new BaseWeDeployService(weDeploy, "http://someurl.com") {
		};

		Request.Builder builder = service.newAuthenticatedRequestBuilder();
		Request request = builder.build();

		assertEquals("http://someurl.com", request.getUrl());
	}

	@Test
	public void url_withHttpsScheme() {
		WeDeploy weDeploy = new WeDeploy.Builder().build();
		BaseWeDeployService service = new BaseWeDeployService(weDeploy, "https://someurl.com") {
		};

		Request.Builder builder = service.newAuthenticatedRequestBuilder();
		Request request = builder.build();

		assertEquals("https://someurl.com", request.getUrl());
	}

	private BaseWeDeployService service;

}
