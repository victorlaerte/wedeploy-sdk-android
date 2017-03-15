package com.wedeploy.sdk.transport;

import java.util.List;
import java.util.Map;

/**
 * @author Silvio Santos
 */
public class Response {

	public String getBody() {
        return body;
    }

    public String getHeader(String name) {
        List<String> values = headers.get(name);

        if ((values != null) && !values.isEmpty()) {
        	return values.get(values.size() - 1);
        }

        return null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

	public boolean succeeded() {
		return succeeded;
	}

	public static class Builder {
	    String body;
	    Map<String, List<String>> headers;
	    int statusCode;
	    String statusMessage;
	    boolean succeeded;

	    public Builder body(String body) {
		    this.body = body;
		    return this;
	    }

	    public Builder headers(Map<String, List<String>> headers) {
		    this.headers = headers;
		    return this;
	    }

	    public Builder statusCode(int statusCode) {
		    this.statusCode = statusCode;
		    return this;
	    }

	    public Builder statusMessage(String statusMessage) {
		    this.statusMessage = statusMessage;
		    return this;
	    }

		public Builder succeeded(boolean succeeded) {
			this.succeeded = succeeded;
			return this;
		}

	    public Response build() {
	    	return new Response(this);
	    }

    }

    private Response(Builder builder) {
		this.body = builder.body;
		this.headers = builder.headers;
		this.statusCode = builder.statusCode;
		this.statusMessage = builder.statusMessage;
		this.succeeded = builder.succeeded;
    }

	private final String body;
	private final Map<String, List<String>> headers;
	private final int statusCode;
	private final String statusMessage;
	private final boolean succeeded;

}
