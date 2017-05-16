package com.wedeploy.android.transport;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Silvio Santos
 */
public class RequestTest {

	@Test
	public void request_withForms() {
		Request request = new Request.Builder()
			.form("1", "2")
			.build();

		assertEquals("2", request.getForms().get("1").get(0));
	}

	@Test
	public void request_withBody() {
		Request request = new Request.Builder()
			.body("1")
			.build();

		assertEquals("1", request.getBody());
	}

	@Test(expected = IllegalArgumentException.class)
	public void request_withFormsAndBody_shouldThrowException() {
		new Request.Builder()
			.form("1", "2")
			.body("1")
			.build();
	}

}
