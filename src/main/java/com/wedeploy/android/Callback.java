package com.wedeploy.android;

import com.wedeploy.android.transport.Response;

/**
 * This interface should be implemented to be notified when asynchronous requests to WeDeploy
 * finish.
 *
 * @author Silvio Santos
 * @see Call#execute(Callback)
 */
public interface Callback {

	/**
	 * Called when the request was successfully executed and with the {@link Response}
	 * returned from WeDeploy.
	 *
	 * @param response The {@link Response} returned from WeDeploy.
	 */
	void onSuccess(Response response);

	/**
	 * Called when the request could not be executed due to connection issues or because of
	 * an error thrown by WeDeploy
	 *
	 * @param e Exception thrown while executing the request.
	 */
	void onFailure(Exception e);

}
