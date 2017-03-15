package com.wedeploy.sdk.exception;

/**
 * @author Silvio Santos
 */
public class WeDeployException extends RuntimeException {

	public WeDeployException(String message) {
		super(message);
	}

	public WeDeployException(String message, Exception e) {
		super(message, e);
	}

}
