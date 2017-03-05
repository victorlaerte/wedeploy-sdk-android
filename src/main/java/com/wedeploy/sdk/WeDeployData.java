package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import okhttp3.Credentials;
import org.json.JSONObject;

/**
 * @author Silvio Santos
 */
public class WeDeployData {

    public WeDeployData auth(String email, String password) {
        builder.header("Authorization", Credentials.basic(email, password));
        return this;
    }

    public Call create(String collection, JSONObject jsonObject) {
        Request request = builder.path(collection)
            .method(RequestMethod.POST)
            .body(jsonObject.toString())
            .build();

        return new Call(request, new OkHttpTransport());
    }

    public Call delete(String resourcePath) {
        Request request = builder.path(resourcePath)
            .method(RequestMethod.DELETE)
            .build();

        return new Call(request, new OkHttpTransport());
    }

    public Call get(String resourcePath) {
        Request request = builder.path(resourcePath)
            .method(RequestMethod.GET)
            .build();

        return new Call(request, new OkHttpTransport());
    }

    public Call update(String resourcePath, JSONObject jsonObject) {
        Request request = builder.path(resourcePath)
            .method(RequestMethod.PATCH)
            .body(jsonObject.toString())
            .build();

        return new Call(request, new OkHttpTransport());
    }

    WeDeployData(String url) {
        builder = new Request.Builder().url(url);
    }

    private Request.Builder builder;

}