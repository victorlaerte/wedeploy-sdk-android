package com.wedeploy.sdk;

/**
 * @author Silvio Santos
 */
public interface Callback<T> {

	void onSuccess(T response);

	void onFailure(Exception e);

}
