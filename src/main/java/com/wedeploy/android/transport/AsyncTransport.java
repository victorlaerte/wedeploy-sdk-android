package com.wedeploy.android.transport;

import com.wedeploy.android.Callback;

/**
 * @author Silvio Santos
 */
public interface AsyncTransport<T> {

	void sendAsync(Request request, Callback callback);

}
