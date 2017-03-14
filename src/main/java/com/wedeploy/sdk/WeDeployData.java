package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Silvio Santos
 */
public class WeDeployData {

    public WeDeployData auth(Auth auth) {
        this.auth = auth;
        return this;
    }

	public Call<Response> create(String collection, JSONArray jsonArray) {
		return create(collection, jsonArray.toString());
	}

	public Call<Response> create(String collection, JSONObject jsonObject) {
		return create(collection, jsonObject.toString());
	}

    public Call<Response> delete(String resourcePath) {
        Request request = newAuthenticatedBuilder()
            .path(resourcePath)
            .method(RequestMethod.DELETE)
            .build();

        return newCall(request);
    }

    public Call<Response> get(String resourcePath) {
        Request request = newAuthenticatedBuilder()
            .path(resourcePath)
            .method(RequestMethod.GET)
	        .build();

        return newCall(request);
    }

    public Call<Response> update(String resourcePath, JSONObject jsonObject) {
        Request request = newAuthenticatedBuilder()
            .path(resourcePath)
            .method(RequestMethod.PATCH)
            .body(jsonObject.toString())
            .build();

        return newCall(request);
    }

	public Call<Response> replace(String resourcePath, JSONObject jsonObject) {
		Request request = newAuthenticatedBuilder()
			.path(resourcePath)
			.method(RequestMethod.PUT)
			.body(jsonObject.toString())
			.build();

		return newCall(request);
	}

    private Request.Builder newAuthenticatedBuilder() {
        Request.Builder builder = new Request.Builder()
            .url(url);

        if (auth == null) {
            return builder;
        }

        return auth.authenticate(builder);
    }

	private Call<Response> create(String collection, String json) {
		Request request = newAuthenticatedBuilder()
			.path(collection)
			.method(RequestMethod.POST)
			.body(json)
			.build();

		return newCall(request);
	}

    private Call<Response> newCall(Request request) {
        return new Call<>(request, new OkHttpTransport(), Response.class);
    }

    WeDeployData(String url) {
        this.url = url;
    }

    private Auth auth;
    private String url;

}