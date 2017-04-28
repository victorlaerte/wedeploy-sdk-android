package com.wedeploy.android.util;

import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.Response;

/**
 * @author Silvio Santos
 */
public class Validator {

	public static void checkNotNull(Object object, String message) {
		if (object == null) throw new IllegalArgumentException(message);
	}

	public static void checkResponseCode(Response response) throws WeDeployException {
		if (!response.succeeded()) throw new WeDeployException(response);
	}

}
