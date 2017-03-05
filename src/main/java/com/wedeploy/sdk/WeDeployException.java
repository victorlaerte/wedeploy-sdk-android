package com.wedeploy.sdk;

/**
 * @author Silvio Santos
 */
public class WeDeployException extends RuntimeException {

	public WeDeployException(String message, Exception e) {
		super(message, e);
	}

}
