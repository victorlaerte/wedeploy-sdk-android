package com.wedeploy.android;

import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.Transport;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

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
