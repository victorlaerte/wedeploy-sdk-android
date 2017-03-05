package com.wedeploy.sdk.internal;

import com.wedeploy.sdk.Request;

/**
 * @author Silvio Santos
 */
public interface Transport<T> {

    T send(Request request);

}
