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
