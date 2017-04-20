package com.wedeploy.sdk.transport;

/**
 * @author Silvio Santos
 */
public enum RequestMethod {

	GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

	public String getValue() {
		return value;
	}

	RequestMethod(String value) {
		this.value = value;
	}

	private String value;

}
