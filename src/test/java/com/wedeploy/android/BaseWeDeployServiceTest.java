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
