package com.wedeploy.sdk.transport;

import com.wedeploy.sdk.Callback;

/**
 * @author Silvio Santos
 */
public interface AsyncTransport<T> {

	void sendAsync(Request request, Callback<T> callback);

}
