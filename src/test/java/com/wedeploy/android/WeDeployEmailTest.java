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

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.auth.TokenAuthorization;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.Response;
import org.junit.Assert;
import org.junit.Test;

import static com.wedeploy.android.Constants.AUTHORIZATION;
import static com.wedeploy.android.Constants.EMAIL_URL;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Silvio Santos
 */
public class WeDeployEmailTest {

	@Test
	public void email_withGlobalAuthorization() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder()
			.authorization(AUTHORIZATION)
			.build();

		Authorization authorization = weDeploy.email(EMAIL_URL).getAuthorization();

		Assert.assertTrue(authorization == AUTHORIZATION);
	}

	@Test
	public void email_withScopedAuthorization() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder()
			.authorization(AUTHORIZATION)
			.build();

		Authorization scopedAuthorization = new TokenAuthorization("1");

		Authorization authorization = weDeploy.email(EMAIL_URL)
			.authorization(scopedAuthorization)
			.getAuthorization();

		Assert.assertTrue(scopedAuthorization == authorization);
	}

	@Test
	public void sendAndCheckStatus() throws WeDeployException {
		WeDeployEmail email = weDeploy.email(EMAIL_URL);

		Response response = email.authorization(AUTHORIZATION)
			.from("test@wedeploy.me")
			.cc("test@wedeploy.me")
			.to("test@wedeploy.me")
			.message("Hello from WeDeploy!")
			.replyTo("test@wedeploy.me")
			.subject("WeDeploy email test!")
			.send()
			.execute();

		assertTrue(email.getOrCreateRequestBuilder().build().getForms().isEmpty());

		String id = response.getBody();

		email.authorization(AUTHORIZATION)
			.checkEmailStatus(id)
			.execute();

		assertTrue(email.getOrCreateRequestBuilder().build().getForms().isEmpty());
	}

	private WeDeploy weDeploy = new WeDeploy.Builder().build();

}
