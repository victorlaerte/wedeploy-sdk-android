package com.wedeploy.android;

/**
 * The RealTime class let's you register for real-time events on WeDeploy.
 *
 * @author Silvio Santos
 * @see WeDeployData#watch(String)
 */
public interface RealTime {

	interface OnEventListener {

		void onEvent(Object... args);

	}

	/**
	 * Closes the real-time connection.
	 */
	void close();

	/**
	 * Registers a listener to be notified when the supplied event occurs.
	 *
	 * @param event The event to listen to.
	 * @param listener The event to listen to.
	 * @return A RealTime object.
	 */
	RealTime on(String event, OnEventListener listener);

}
