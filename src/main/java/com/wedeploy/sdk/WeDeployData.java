package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import org.json.JSONObject;

/**
 * @author Silvio Santos
 */
public class WeDeployData {

    public WeDeployData auth(Auth auth) {
        this.auth = auth;
        return this;
    }

    public Call create(String collection, JSONObject jsonObject) {
        Request request = newAuthenticatedBuilder()
            .path(collection)
            .method(RequestMethod.POST)
            .body(jsonObject.toString())
            .build();

        return new Call(request, new OkHttpTransport());
    }

    public Call delete(String resourcePath) {
        Request request = newAuthenticatedBuilder()
            .path(resourcePath)
            .method(RequestMethod.DELETE)
            .build();

        return new Call(request, new OkHttpTransport());
    }

    public Call get(String resourcePath) {
        Request request = newAuthenticatedBuilder()
            .path(resourcePath)
            .method(RequestMethod.GET)
            .build();

        return new Call(request, new OkHttpTransport());
    }

    public Call update(String resourcePath, JSONObject jsonObject) {
        Request request = newAuthenticatedBuilder()
            .path(resourcePath)
            .method(RequestMethod.PATCH)
            .body(jsonObject.toString())
            .build();

        return new Call(request, new OkHttpTransport());
    }

    private Request.Builder newAuthenticatedBuilder() {
        Request.Builder builder = new Request.Builder()
            .url(url);

        if (auth == null) {
            return builder;
        }

        return auth.authenticate(builder);
    }

    WeDeployData(String url) {
        this.url = url;
    }

    private Auth auth;
    private String url;

}