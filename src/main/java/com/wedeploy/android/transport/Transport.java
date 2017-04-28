package com.wedeploy.android.transport;

/**
 * @author Silvio Santos
 */
public interface Transport<T> {

	T send(Request request);

}
