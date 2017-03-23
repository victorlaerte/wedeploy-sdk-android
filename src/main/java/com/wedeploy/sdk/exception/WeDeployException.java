package com.wedeploy.sdk.exception;

/**
 * @author Silvio Santos
 */
public class WeDeployException extends Exception {

	public WeDeployException(String message) {
		super(message);
	}

	public WeDeployException(String message, Exception e) {
		super(message, e);
	}

}
