package com.wedeploy.sdk.exception;

import com.wedeploy.sdk.transport.ErrorBody;
import com.wedeploy.sdk.transport.Response;

import java.util.List;

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
		ErrorBody errorBody;

		try {
			errorBody = new ErrorBody(response.getBody());
		}
		catch (Exception e) {
			return "Error while executing request";
		}

		StringBuilder sb = new StringBuilder()
			.append("HTTP ")
			.append(errorBody.getStatusCode())
			.append(" - ")
			.append(errorBody.getStatusMessage())
			.append(".");

		List<ErrorBody.Error> errors = errorBody.getErrors();

		if (!errors.isEmpty()) {
			sb.append(" Reason: ");
			sb.append(errors.get(0).getReason());
			sb.append(". Message: ");
			sb.append(errors.get(0).getMessage());
		}

		return sb.toString();
	}

}
