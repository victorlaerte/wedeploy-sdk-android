package com.wedeploy.android;

import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.Request;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Silvio Santos
 */
public class BaseWeDeployServiceTest {

	@Test
	public void header_withSomeValues() {
		BaseWeDeployService service = new BaseWeDeployService(new WeDeploy.Builder().build());
		service.header("name1", "value1");
		service.header("name2", "value2");
		service.header("name3", "value3");
		service.header("name3", "value4");

		Request.Builder builder = service.newAuthenticatedRequestBuilder("http://someurl.com");
		Request request = builder.build();

		MultiMap<String> headers = request.getHeaders();
		assertEquals("value1", headers.get("name1").get(0));
		assertEquals("value2", headers.get("name2").get(0));
		assertEquals("value3", headers.get("name3").get(0));
		assertEquals("value4", headers.get("name3").get(1));
	}

}
