package com.wedeploy.sdk;

import com.wedeploy.sdk.transport.Response;

/**
 * @author Silvio Santos
 */
public interface Callback {

	void onSuccess(Response response);

	void onFailure(Exception e);

}
