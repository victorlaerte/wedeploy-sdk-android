package com.wedeploy.sdk.transport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Silvio Santos
 */
public class ErrorBody {

	public ErrorBody(String body) {
		JSONObject bodyJson = new JSONObject(body);

		statusCode = bodyJson.optInt("code", -1);
		statusMessage = bodyJson.optString("message", "").trim();

		JSONArray errorsJsonArray = bodyJson.getJSONArray("errors");

		for (int i = 0; i < errorsJsonArray.length(); i++) {
			JSONObject errorJson = errorsJsonArray.getJSONObject(i);

			errors.add(new Error(errorJson));
		}
	}

	public List<Error> getErrors() {
		return errors;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	private final List<Error> errors = new LinkedList<>();
	private final int statusCode;
	private final String statusMessage;

	public static class Error {

		public Error(JSONObject errorJson) {
			reason = errorJson.optString("reason", "");
			message = errorJson.optString("message", "");
		}

		public String getMessage() {
			return message;
		}

		public String getReason() {
			return reason;
		}

		private final String message;
		private final String reason;

	}

}
