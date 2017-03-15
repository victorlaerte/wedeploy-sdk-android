package com.wedeploy.sdk.transport;

/**
 * @author Silvio Santos
 */
public interface Transport<T> {

	T send(Request request);

}
