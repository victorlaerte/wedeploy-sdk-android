package com.wedeploy.android.query;

/**
 * Represents embodied objects.
 */
public abstract class BodyConvertible {

	public abstract Object body();

	public String bodyAsJson() {
		return BodyToJsonStringConverter.toString(body());
	}

}
