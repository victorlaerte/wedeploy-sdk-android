package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.SocketIORealTime;
import com.wedeploy.sdk.query.BodyToJsonStringConverter;
import com.wedeploy.sdk.query.Query;
import com.wedeploy.sdk.query.SortOrder;
import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import com.wedeploy.sdk.util.URLUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.wedeploy.sdk.internal.RequestMethod.*;

/**
 * @author Silvio Santos
 */
public class WeDeployData {

	WeDeployData(String url) {
		this.url = url;
	}

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
			.method(DELETE)
			.build();

		return newCall(request);
	}

	public Call<Response> get(String resourcePath) {
		Request.Builder builder = newAuthenticatedBuilder()
			.path(resourcePath)
			.query(getOrCreateQueryBuilder().build())
			.method(GET);

		return newCall(builder.build());
	}

	public Call<Response> update(String resourcePath, JSONObject jsonObject) {
		Request request = newAuthenticatedBuilder()
			.path(resourcePath)
			.method(PATCH)
			.body(jsonObject.toString())
			.build();

		return newCall(request);
	}

	public Call<Response> replace(String resourcePath, JSONObject jsonObject) {
		Request request = newAuthenticatedBuilder()
			.path(resourcePath)
			.method(PUT)
			.body(jsonObject.toString())
			.build();

		return newCall(request);
	}

	public Call<Response> search(String resourcePath) {
		Query.Builder queryBuilder = getOrCreateQueryBuilder().search();

		Request.Builder builder = newAuthenticatedBuilder()
			.path(resourcePath)
			.query(queryBuilder.build())
			.method(GET);

		return newCall(builder.build());
	}

	public RealTime watch(String collection) {
		if (!collection.startsWith("/")) {
			collection = "/" + collection;
		}

		String queryString = getOrCreateQueryBuilder().build().getEncodedQuery();

		Map<String, String> query = new HashMap<>();
		query.put("url", URLUtil.joinPathAndQuery(collection, queryString));

		Map<String, String> headers = new HashMap<>();

		if (auth != null) {
			headers.put("Authorization", auth.getAuthorizationHeader());
		}

		Map<String, Object> options = new HashMap<>();
		options.put("forceNew", true);
		options.put("path", collection);
		options.put("query", query);
		options.put("headers", headers);

		try {
			return new SocketIORealTime(url, options);
		}
		catch (Exception e) {
			throw new WeDeployException(e.getMessage());
		}
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

	public WeDeployData sort(String field) {
		sort(field, SortOrder.ASCENDING);

		return this;
	}

	public WeDeployData sort(String field, SortOrder order) {
		if (order == null) {
			throw new IllegalArgumentException("SortOrder can't be null");
		}

		getOrCreateQueryBuilder().sort(field, order.getValue());

		return this;
	}

	public WeDeployData where(Filter filter) {
		getOrCreateQueryBuilder().filter(filter);

		return this;
	}

	private Call<Response> create(String collection, String json) {
		Request request = newAuthenticatedBuilder()
			.path(collection)
			.method(POST)
			.body(json)
			.build();

		return newCall(request);
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

	private Call<Response> newCall(Request request) {
		return new Call<>(request, new OkHttpTransport(), Response.class);
	}

	private Auth auth;
	private Query.Builder queryBuilder;
	private final String url;

}
