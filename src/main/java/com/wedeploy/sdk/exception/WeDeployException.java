package com.wedeploy.sdk.exception;

import com.wedeploy.sdk.transport.Response;

/**
 * @author Silvio Santos
 */
public class WeDeployException extends Exception {

	public WeDeployException(Response response) {
		super(getMessage(response));
	}

	public WeDeployException(String message, Throwable cause) {
		super(message, cause);
	}

	private static String getMessage(Response response) {
		String body = response.getBody();

		if (body.startsWith("{")) {
			return body;
		}

		return response.getStatusMessage();
	}

}
