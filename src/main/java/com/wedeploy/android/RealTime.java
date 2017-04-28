package com.wedeploy.android;

/**
 * Real-time.
 */
public interface RealTime {

	interface OnEventListener {

		void onEvent(Object... args);

	}

	/**
	 * Closes real-time connection.
	 */
	void close();

	/**
	 * Registers a callback of type `event` with `listener`.
	 */
	RealTime on(String event, OnEventListener listener);

}
