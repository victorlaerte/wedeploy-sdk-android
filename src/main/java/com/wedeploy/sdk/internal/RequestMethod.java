package com.wedeploy.sdk.internal;

/**
 * @author Silvio Santos
 */
public enum RequestMethod {

	GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

	RequestMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	private String value;

}
