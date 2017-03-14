package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import com.wedeploy.sdk.query.BodyToJsonStringConverter;
import com.wedeploy.sdk.query.Query;
import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

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
	    Request.Builder builder = newAuthenticatedBuilder();
	    Query query = getOrCreateQueryBuilder().build();

	    builder.path(resourcePath)
            .method(RequestMethod.GET);

        for (Map.Entry<String, Object> entry : query.body().entrySet()) {
            builder.param(
            	entry.getKey(),
	            BodyToJsonStringConverter.toString(entry.getValue()));
        }

	    return newCall(builder.build());
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

	public WeDeployData aggregate(Aggregation aggregation) {
		getOrCreateQueryBuilder().aggregate(aggregation);

		return this;
	}

	public WeDeployData limit(int limit) {
		getOrCreateQueryBuilder().limit(limit);

		return this;
	}

	public WeDeployData offset(int offset) {
		getOrCreateQueryBuilder().offset(offset);

		return this;
	}

	public WeDeployData where(Filter filter) {
    	getOrCreateQueryBuilder().filter(filter);

    	return this;
	}

	private Query.Builder getOrCreateQueryBuilder() {
		if (queryBuilder == null) {
			queryBuilder = new Query.Builder();
		}

		return queryBuilder;
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
    private Query.Builder queryBuilder;
    private String url;

}