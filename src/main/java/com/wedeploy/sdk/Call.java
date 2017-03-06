package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.Transport;

/**
 * @author Silvio Santos
 */
public class Call {

    Call(Request request, Transport transport) {
        this.request = request;
        this.transport = transport;
    }

    public Response execute() {
        return transport.send(request);
    }

    private Request request;
    private Transport<Response> transport = new OkHttpTransport();

}