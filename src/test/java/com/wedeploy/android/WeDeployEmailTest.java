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
