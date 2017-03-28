package com.wedeploy.sdk;

import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.transport.Response;
import org.junit.Test;

import static com.wedeploy.sdk.Constants.AUTH;
import static com.wedeploy.sdk.Constants.EMAIL_URL;

/**
 * @author Silvio Santos
 */
public class WeDeployEmailTest {

	@Test
	public void sendAndCheckStatus() throws WeDeployException {
		Response response = weDeploy.email(EMAIL_URL)
			.auth(AUTH)
			.from("test@wedeploy.me")
			.cc("test@wedeploy.me")
			.to("test@wedeploy.me")
			.message("Hello from WeDeploy!")
			.replyTo("test@wedeploy.me")
			.subject("WeDeploy email test!")
			.send()
			.execute();

		String id = response.getBody();

		weDeploy.email(EMAIL_URL)
			.auth(AUTH)
			.checkEmailStatus(id)
			.execute();
	}

	private WeDeploy weDeploy = new WeDeploy.Builder().build();

}
