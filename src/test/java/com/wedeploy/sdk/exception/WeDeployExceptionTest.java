package com.wedeploy.sdk.exception;

import com.wedeploy.sdk.transport.Response;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Silvio Santos
 */
@SuppressWarnings("ThrowableNotThrown")
public class WeDeployExceptionTest {

	@Test
	public void message_withNotFoundError() {
		Response response = new Response.Builder()
			.body("{\n" +
				"code: 404,\n" +
				"message: \"Not Found\",\n" +
				"errors: [\n" +
				"{\n" +
				"reason: \"notFound\",\n" +
				"message: \"The requested operation failed because a resource associated with " +
				"the" +
				" " +
				"request could not be found.\"\n" +
				"}\n" +
				"]\n" +
				"}")
			.build();

		WeDeployException exception = new WeDeployException(response);
		Assert.assertEquals("HTTP 404 - Not Found. Reason: notFound. Message: The requested operation failed" +
				" because a resource associated with the request could not be found.",
			exception.getMessage());
	}

	@Test
	public void message_withRequiredFieldError() {
		Response response = new Response.Builder()
			.body("{\n" +
				"\t\"code\": 400,\n" +
				"\t\"message\": \"Bad Request\",\n" +
				"\t\"errors\": [\n" +
				"\t\t{\n" +
				"\t\t\t\"reason\": \"required\",\n" +
				"\t\t\t\"message\": \"name\"\n" +
				"\t\t}\n" +
				"\t]\n" +
				"}")
			.build();

		WeDeployException exception = new WeDeployException(response);
		Assert.assertEquals("HTTP 400 - Bad Request. Reason: required. Message: name",
			exception.getMessage());
	}

}
