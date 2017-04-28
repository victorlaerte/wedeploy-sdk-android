package com.wedeploy.android.exception;

import com.wedeploy.android.transport.Response;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Silvio Santos
 */
@SuppressWarnings("ThrowableNotThrown")
public class WeDeployExceptionTest {

	@Test
	public void message_withNotFoundError() {
		String errorJson = "{\n" +
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
			"}";

		Response response = new Response.Builder()
			.body(errorJson)
			.build();

		WeDeployException exception = new WeDeployException(response);
		Assert.assertEquals(errorJson, exception.getMessage());
	}

	@Test
	public void message_withRequiredFieldError() {
		String errorJson = "<head>somehtml<head>";

		Response response = new Response.Builder()
			.body(errorJson)
			.statusMessage("Not Found")
			.build();

		WeDeployException exception = new WeDeployException(response);
		Assert.assertEquals("Not Found", exception.getMessage());
	}

}
