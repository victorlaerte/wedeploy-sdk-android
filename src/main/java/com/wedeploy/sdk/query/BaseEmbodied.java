package com.wedeploy.sdk.query;

/**
 * @author Silvio Santos
 */
public abstract class BaseEmbodied implements Embodied {

	public String bodyAsJson() {
		return Util.toString(body());
	}

}