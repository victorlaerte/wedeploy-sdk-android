package com.wedeploy.android;

import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;

import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * @author Silvio Santos
 */
public class WeDeployEmail extends WeDeployService<WeDeployEmail> {

	WeDeployEmail(WeDeploy weDeploy, String url) {
		super(weDeploy);

		this.url = url;
	}

	public WeDeployEmail from(String from) {
		checkNotNull(from, "From email must not be null");
		getOrCreateRequestBuilder().form("from", from);

		return this;
	}

	public WeDeployEmail cc(String cc) {
		checkNotNull(cc, "CC email must not be null");
		getOrCreateRequestBuilder().form("cc", cc);

		return this;
	}

	public WeDeployEmail bcc(String bcc) {
		checkNotNull(bcc, "BCC email must not be null");
		getOrCreateRequestBuilder().form("bcc", bcc);

		return this;
	}

	public WeDeployEmail message(String message) {
		checkNotNull(message, "Message must not be null");
		getOrCreateRequestBuilder().form("message", message);

		return this;
	}

	public WeDeployEmail priority(String priority) {
		checkNotNull(priority, "Priority must not be null");
		getOrCreateRequestBuilder().form("priority", priority);

		return this;
	}

	public WeDeployEmail replyTo(String replyTo) {
		checkNotNull(replyTo, "ReplyTo must not be null");
		getOrCreateRequestBuilder().form("replyTo", replyTo);

		return this;
	}

	public WeDeployEmail to(String to) {
		checkNotNull(to, "'To' must not be null");
		getOrCreateRequestBuilder().form("to", to);

		return this;
	}

	public WeDeployEmail subject(String subject) {
		checkNotNull(subject, "Subject must not be null");
		getOrCreateRequestBuilder().form("subject", subject);

		return this;
	}

	public Call<Response> send() {
		Request request = requestBuilder.url(url)
			.path("emails")
			.method(RequestMethod.POST)
			.build();

		resetRequestBuilder();

		return newCall(request);
	}

	public Call<Response> checkEmailStatus(String emailId) {
		checkNotNull(emailId, "emailId must be specified");

		Request request = getOrCreateRequestBuilder()
			.url(url)
			.path("emails/" + emailId + "/status")
			.method(RequestMethod.GET)
			.build();

		resetRequestBuilder();

		return newCall(request);
	}

	protected Request.Builder getOrCreateRequestBuilder() {
		if (requestBuilder == null) {
			requestBuilder = newAuthenticatedRequestBuilder(url);
		}

		return requestBuilder;
	}

	private void resetRequestBuilder() {
		requestBuilder = null;
	}

	private Request.Builder requestBuilder;
	private final String url;

}
