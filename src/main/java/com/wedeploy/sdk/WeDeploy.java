package com.wedeploy.sdk;

/**
 * @author Silvio Santos
 */
public class WeDeploy {

	public static Auth auth;

	public static WeDeployAuth auth(String url) {
		return new WeDeployAuth(url);
	}

	public static WeDeployData data(String url) {
		return new WeDeployData(url)
			.auth(auth);
	}

}
