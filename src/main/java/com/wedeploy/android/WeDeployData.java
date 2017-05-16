package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.transport.SocketIORealTime;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.aggregation.Aggregation;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.wedeploy.android.transport.RequestMethod.*;
import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * @author Silvio Santos
 */
public class WeDeployData extends BaseWeDeployService<WeDeployData> {

	WeDeployData(WeDeploy weDeploy, String url) {
		super(weDeploy, url);
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

		Request request = newAuthenticatedRequestBuilder()
			.path(resourcePath)
			.method(DELETE)
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	public Call<Response> get(String resourcePath) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");

		Request.Builder builder = newAuthenticatedRequestBuilder()
			.path(resourcePath)
			.query(getOrCreateQueryBuilder().build())
			.method(GET);

		resetQueryBuilder();

		return newCall(builder.build());
	}

	public Call<Response> update(String resourcePath, JSONObject data) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");
		checkNotNull(data, "data JSONObject must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path(resourcePath)
			.method(PATCH)
			.body(data.toString())
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	public Call<Response> replace(String resourcePath, JSONObject data) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");
		checkNotNull(data, "data JSONObject must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path(resourcePath)
			.method(PUT)
			.body(data.toString())
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	public Call<Response> search(String resourcePath) {
		checkNotNull(resourcePath, "Document/Field/Collection path must be specified");

		Query.Builder queryBuilder = getOrCreateQueryBuilder().search();

		Request.Builder builder = newAuthenticatedRequestBuilder()
			.path(resourcePath)
			.query(queryBuilder.build())
			.method(GET);

		resetQueryBuilder();

		return newCall(builder.build());
	}

	public RealTime watch(String collection) {
		checkNotNull(collection, "Collection must be specified");

		String queryString = getOrCreateQueryBuilder().build().getQueryString();

		SocketIORealTime.Builder builder = new SocketIORealTime.Builder(url)
			.forceNew(true)
			.path(collection)
			.query(queryString);

		Authorization authorization = getAuthorization();

		if (authorization != null) {
			builder.header("Authorization", authorization.getAuthorizationHeader());
		}

		resetQueryBuilder();

		return builder.build();
	}

	public WeDeployData aggregate(Aggregation aggregation) {
		checkNotNull(aggregation, "Aggregation must be specified");

		getOrCreateQueryBuilder().aggregate(aggregation);

		return this;
	}

	public WeDeployData count() {
		getOrCreateQueryBuilder().type("count");

		return this;
	}

	public WeDeployData highlight(String field) {
		getOrCreateQueryBuilder().highlight(field);

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

	public WeDeployData orderBy(String field) {
		orderBy(field, SortOrder.ASCENDING);

		return this;
	}

	public WeDeployData orderBy(String field, SortOrder order) {
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

	protected Query.Builder getOrCreateQueryBuilder() {
		if (queryBuilder == null) {
			queryBuilder = new Query.Builder();
		}

		return queryBuilder;
	}

	private Call<Response> create(String collection, String dataJson) {
		checkNotNull(collection, "Collection must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path(collection)
			.method(POST)
			.body(dataJson)
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	private void resetQueryBuilder() {
		queryBuilder = null;
	}

	private Query.Builder queryBuilder;

}
