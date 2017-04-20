package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.auth.TokenAuth;
import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.transport.Response;
import org.junit.Assert;
import org.junit.Test;

import static com.wedeploy.sdk.Constants.AUTH;
import static com.wedeploy.sdk.Constants.DATA_URL;
import static com.wedeploy.sdk.Constants.EMAIL_URL;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Silvio Santos
 */
public class WeDeployEmailTest {

	@Test
	public void email_withGlobalAuth() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder()
			.auth(AUTH)
			.build();

		Auth auth = weDeploy.email(EMAIL_URL).getAuth();

		Assert.assertTrue(auth == AUTH);
	}

	@Test
	public void email_withScopedAuth() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder()
			.auth(AUTH)
			.build();

		Auth scopedAuth = new TokenAuth("1");

		Auth auth = weDeploy.email(EMAIL_URL)
			.auth(scopedAuth)
			.getAuth();

		Assert.assertTrue(scopedAuth == auth);
	}

	@Test
	public void sendAndCheckStatus() throws WeDeployException {
		WeDeployEmail email = weDeploy.email(EMAIL_URL);

		Response response = email.auth(AUTH)
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

		email.auth(AUTH)
			.checkEmailStatus(id)
			.execute();

		assertTrue(email.getOrCreateRequestBuilder().build().getForms().isEmpty());
	}

	private WeDeploy weDeploy = new WeDeploy.Builder().build();

}
