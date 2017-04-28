package com.wedeploy.android;

import com.wedeploy.android.transport.Response;

/**
 * @author Silvio Santos
 */
public interface Callback {

	void onSuccess(Response response);

	void onFailure(Exception e);

}
