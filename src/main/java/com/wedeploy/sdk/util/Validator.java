package com.wedeploy.sdk.util;

/**
 * @author Silvio Santos
 */
public class Validator {

	public static void checkNotNull(Object object, String message) {
		if (object == null) throw new IllegalArgumentException(message);
	}

}
