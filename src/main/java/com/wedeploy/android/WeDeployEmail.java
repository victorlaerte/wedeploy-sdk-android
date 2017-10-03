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

import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Response;

import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * The WeDeployEmail service enables you to send customized emails to your users
 * and check on their status.
 * <p>
 * This class is not thread safe. In order to avoid concurrency issues, you must create a new
 * instance by calling {@link WeDeploy#email(String)} for every request you want to do on
 * {@link WeDeployEmail}.
 *
 * @author Silvio Santos
 */
public class WeDeployEmail extends BaseWeDeployService<WeDeployEmail> {

	/**
	 * Constructs a {@link WeDeployEmail} instance.
	 *
	 * @param weDeploy A WeDeploy instance.
	 * @param url The WeDeploy Data service url.
	 */
	WeDeployEmail(WeDeploy weDeploy, String url) {
		super(weDeploy, url);
	}

	/**
	 * Set from attribute on params to be sent on email request.
	 *
	 * @param from The sender email address.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail from(String from) {
		checkNotNull(from, "From email must not be null");
		getOrCreateRequestBuilder().form("from", from);

		return this;
	}

	/**
	 * Set cc attribute on params to be sent on email request.
	 *
	 * @param cc CC recipient email address. Multiple addresses should be defined in multiple
	 * parameters.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail cc(String cc) {
		checkNotNull(cc, "CC email must not be null");
		getOrCreateRequestBuilder().form("cc", cc);

		return this;
	}

	/**
	 * Set bcc attribute on params to be sent on email request.
	 *
	 * @param bcc BCC recipient email address. Multiple addresses should be defined in multiple
	 * parameters.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail bcc(String bcc) {
		checkNotNull(bcc, "BCC email must not be null");
		getOrCreateRequestBuilder().form("bcc", bcc);

		return this;
	}

	/**
	 * Set message attribute on params to be sent on email request.
	 *
	 * @param message HTML content of your email message. Up to 5MB.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail message(String message) {
		checkNotNull(message, "Message must not be null");
		getOrCreateRequestBuilder().form("message", message);

		return this;
	}

	/**
	 * Set priority attribute on params to be sent on email request.
	 *
	 * @param priority Used by email clients to define a message's importance.
	 * From 1 to 5 where '1' is highest and '5' is the lowest priority.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail priority(String priority) {
		checkNotNull(priority, "Priority must not be null");
		getOrCreateRequestBuilder().form("priority", priority);

		return this;
	}

	/**
	 * Set replyTo attribute on params to be sent on email request.
	 *
	 * @param replyTo Append a reply-to address to your email message.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail replyTo(String replyTo) {
		checkNotNull(replyTo, "ReplyTo must not be null");
		getOrCreateRequestBuilder().form("replyTo", replyTo);

		return this;
	}

	/**
	 * Set to attribute on params to be sent on email request.
	 *
	 * @param to Recipient email address. Multiple addresses should be defined in multiple
	 * parameters.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail to(String to) {
		checkNotNull(to, "'To' must not be null");
		getOrCreateRequestBuilder().form("to", to);

		return this;
	}

	/**
	 * Set subject attribute on params to be sent on email request.
	 *
	 * @param subject Subject of your email. Up to 1MB.
	 * @return {@link WeDeployEmail} Returns the {@link WeDeployEmail} object itself,
	 * so calls can be chained.
	 */
	public WeDeployEmail subject(String subject) {
		checkNotNull(subject, "Subject must not be null");
		getOrCreateRequestBuilder().form("subject", subject);

		return this;
	}

	/**
	 * Builds and encapsulates the request into a Call object.
	 *
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
	public Call<Response> send() {
		Request request = requestBuilder.url(url)
			.path("emails")
			.method(RequestMethod.POST)
			.build();

		resetRequestBuilder();

		return newCall(request);
	}

	/**
	 * Checks the status of an email.
	 *
	 * @param emailId The id of the email you want to verify the status.
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
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
			requestBuilder = newAuthenticatedRequestBuilder();
		}

		return requestBuilder;
	}

	private void resetRequestBuilder() {
		requestBuilder = null;
	}

	private Request.Builder requestBuilder;

}
