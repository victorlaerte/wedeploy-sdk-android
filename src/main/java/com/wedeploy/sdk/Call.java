package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.Transport;

/**
 * @author Silvio Santos
 */
public class Call<T> {

    Call(Request request, Transport transport, Class<T> clazz) {
        this.request = request;
        this.transport = transport;
        this.clazz = clazz;
    }

    public T execute() {
        Response response = transport.send(request);

        if (clazz.isInstance(response)) {
            return clazz.cast(response);
        }

        throw new WeDeployException("Unable to convert response to " + clazz.getSimpleName());
    }

    private Class<T> clazz;
    private Request request;
    private Transport<Response> transport = new OkHttpTransport();

}