package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.internal.SocketIORealTime;
import com.wedeploy.sdk.query.Query;
import com.wedeploy.sdk.query.SortOrder;
import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.wedeploy.sdk.internal.RequestMethod.*;
import static com.wedeploy.sdk.util.Validator.checkNotNull;

/**
 * @author Silvio Santos
 */
public class WeDeployData extends WeDeployService<WeDeployData> {

	WeDeployData(WeDeploy weDeploy, String url) {
		super(weDeploy);

		checkNotNull(url, "Data url must be specified");

		this.url = url;
	}

	public WeDeployData auth(Auth auth) {
		this.auth = auth;
		return this;
	}

	public Call<Response> create(String collection, JSONArray data) {
		checkNotNull(data, "data JSONArray must be specified");

		return create(collection, data.toString());
	}

	public Call<Response> create(String collection, JSONObject data) {
		checkNotNull(data, "data JSONObject must be specified");

		return create(collection, data.toString());
	}

	public Call<Response> delete(String resourcePath) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");

		Request request = newAuthenticatedBuilder()
			.path(resourcePath)
			.method(DELETE)
			.build();

		return newCall(request);
	}

	public Call<Response> get(String resourcePath) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");

		Request.Builder builder = newAuthenticatedBuilder()
			.path(resourcePath)
			.query(getOrCreateQueryBuilder().build())
			.method(GET);

		return newCall(builder.build());
	}

	public Call<Response> update(String resourcePath, JSONObject data) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");
		checkNotNull(data, "data JSONObject must be specified");

		Request request = newAuthenticatedBuilder()
			.path(resourcePath)
			.method(PATCH)
			.body(data.toString())
			.build();

		return newCall(request);
	}

	public Call<Response> replace(String resourcePath, JSONObject data) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");
		checkNotNull(data, "data JSONObject must be specified");

		Request request = newAuthenticatedBuilder()
			.path(resourcePath)
			.method(PUT)
			.body(data.toString())
			.build();

		return newCall(request);
	}

	public Call<Response> search(String resourcePath) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");

		Query.Builder queryBuilder = getOrCreateQueryBuilder().search();

		Request.Builder builder = newAuthenticatedBuilder()
			.path(resourcePath)
			.query(queryBuilder.build())
			.method(GET);

		return newCall(builder.build());
	}

	public RealTime watch(String collection) {
		checkNotNull(collection, "Collection must be specified");

		String queryString = getOrCreateQueryBuilder().build().getQueryString();

		SocketIORealTime.Builder builder = new SocketIORealTime.Builder(url)
			.forceNew(true)
			.path(collection)
			.query(queryString);

		if (auth != null) {
			builder.header("Authorization", auth.getAuthorizationHeader());
		}

		return builder.build();
	}

	public WeDeployData aggregate(Aggregation aggregation) {
		checkNotNull(aggregation, "Aggregation must be specified");

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
		checkNotNull(field, "Field must be specified");
		checkNotNull(field, "SortOrder must be specified");

		if (order == null) {
			throw new IllegalArgumentException("SortOrder can't be null");
		}

		getOrCreateQueryBuilder().sort(field, order.getValue());

		return this;
	}

	public WeDeployData where(Filter filter) {
		checkNotNull(filter, "Filter must be specified");

		getOrCreateQueryBuilder().filter(filter);

		return this;
	}

	private Call<Response> create(String collection, String dataJson) {
		checkNotNull(collection, "Collection must be specified");

		Request request = newAuthenticatedBuilder()
			.path(collection)
			.method(POST)
			.body(dataJson)
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

	private Auth auth;
	private Query.Builder queryBuilder;
	private final String url;

}
