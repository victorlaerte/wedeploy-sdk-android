/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Liferay, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.wedeploy.android;

import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.aggregation.Aggregation;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.SocketIORealTime;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.wedeploy.android.transport.RequestMethod.*;
import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * The WeDeployData service enables you to store data securely to a NoSQL cloud database, make
 * complex queries instantly, and consume information in real-time.
 * <p>
 * This class is not thread safe. In order to avoid concurrency issues, you must create a new
 * instance by calling {@link WeDeploy#data(String)} for every request you want to do on
 * {@link WeDeployData}.
 *
 * @author Silvio Santos
 */
public class WeDeployData extends BaseWeDeployService<WeDeployData> {

	/**
	 * Constructs a {@link WeDeployData} instance.
	 *
	 * @param weDeploy A WeDeploy instance.
	 * @param url The WeDeploy Data service url.
	 */
	WeDeployData(WeDeploy weDeploy, String url) {
		super(weDeploy, url);
	}

	/**
	 * Creates multiple objects and saves them to WeDeploy data. If
	 * there's a validation registered in the collection and the request is
	 * successful, the array of objects is returned.
	 * The data {@link JSONArray} describes the attributes on the objects that are to be created.
	 * <p>
	 * <pre><code>
	 * WeDeployData data = weDeploy.data("http://demodata.wedeploy.io");
	 *
	 * JSONObject movie1JsonObject = new JSONObject()
	 *      .put("title", "Star Wars: Episode II - Attack of the Clones");
	 *
	 * JSONObject movie2JsonObject = new JSONObject()
	 *      .put("title", "Star Wars: Episode III - Revenge of the Sith");
	 *
	 * JSONArray dataJsonArray = new JSONArray()
	 *      .put(movie1JsonObject)
	 *      .put(movie2JsonObject);
	 *
	 * response = weDeploy.data(DATA_URL)
	 *      .create("movies", moviesJsonArray)
	 *      .execute();
	 *
	 * </code></pre>
	 *
	 * @param collection Collection (key) used to create the new data.
	 * @param data Attributes on the object that is to be created.
	 * @return {@link Call}
	 */
	public Call<Response> create(String collection, JSONArray data) {
		checkNotNull(data, "data JSONArray must be specified");

		return create(collection, data.toString());
	}

	/**
	 * Creates an object and saves it to WeDeploy data. If
	 * there's a validation registered in the collection and the request is
	 * successful, the object is returned.
	 * The data {@link JSONObject} describes the attributes on the objects that are to be created.
	 * <p>
	 * <pre><code>
	 * WeDeployData data = weDeploy.data("http://demodata.wedeploy.io");
	 *
	 * JSONObject movieJsonObject = new JSONObject()
	 *      .put("title", "Star Wars: Episode II - Attack of the Clones");
	 *
	 * response = weDeploy.data(DATA_URL)
	 *      .create("movies", movieJsonObject)
	 *      .execute();
	 *
	 * </code></pre>
	 *
	 * @param collection Collection (key) used to create the new data.
	 * @param data Attributes on the object that is to be created.
	 * @return {@link Call}
	 */
	public Call<Response> create(String collection, JSONObject data) {
		checkNotNull(data, "data JSONObject must be specified");

		return create(collection, data.toString());
	}

	/**
	 * Deletes a [document/field/collection].
	 *
	 * @param key used to delete the document/field/collection.
	 * @return {@link Call}
	 */
	public Call<Response> delete(String key) {
		checkNotNull(key, "Document/Field/Collection key must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path(key)
			.method(DELETE)
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	/**
	 * Retrieves data from a [document/field/collection].
	 *
	 * @param key used to get the document/field/collection.
	 * @return {@link Call}
	 */
	public Call<Response> get(String key) {
		checkNotNull(key, "Document/Field/Collection key must be specified");

		Request.Builder builder = newAuthenticatedRequestBuilder()
			.path(key)
			.query(getOrCreateQueryBuilder().build())
			.method(GET);

		resetQueryBuilder();

		return newCall(builder.build());
	}

	/**
	 * Updates the attributes of a document based on the passed-in {@link JSONObject} and saves
	 * the record.
	 * <p>
	 * <pre><code>
	 * WeDeployData data = weDeploy.data("http://demodata.wedeploy.io");
	 *
	 * JSONObject movieJsonObject = new JSONObject()
	 *      .put("title", "Star Wars: Episode I");
	 *
	 * data.update("movies/1019112353", movieJsonObject)
	 *      .execute();
	 * </code></pre>
	 *
	 * @param key used to update the document.
	 * @param data Attributes on the object that is to be updated.
	 * @return {@link Call}
	 */
	public Call<Response> update(String key, JSONObject data) {
		checkNotNull(key, "Document/Field/Collection key must be specified");
		checkNotNull(data, "data JSONObject must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path(key)
			.method(PATCH)
			.body(data.toString())
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	/**
	 * Replaces the attributes of a document based on the passed-in {@link JSONObject} and saves
	 * the record.
	 * <p>
	 * <pre><code>
	 * WeDeployData data = weDeploy.data("http://demodata.wedeploy.io");
	 *
	 * JSONObject movieJsonObject = new JSONObject()
	 *      .put("title", "Star Wars: Episode I");
	 *
	 * data.replace("movies/1019112353", movieJsonObject)
	 *      .execute();
	 * </code></pre>
	 *
	 * @param key used to update the document.
	 * @param data Attributes on the object that is to be replaced.
	 * @return {@link Call}
	 */
	public Call<Response> replace(String key, JSONObject data) {
		checkNotNull(key, "Document/Field/Collection path must be specified");
		checkNotNull(data, "data JSONObject must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path(key)
			.method(PUT)
			.body(data.toString())
			.build();

		resetQueryBuilder();

		return newCall(request);
	}

	/**
	 * Retrieve data from a [document/field/collection] and put it in a search format.
	 *
	 * @param key used to search the document/field/collection.
	 * @return {@link Call}
	 */
	public Call<Response> search(String key) {
		checkNotNull(key, "Document/Field/Collection key must be specified");

		Query.Builder queryBuilder = getOrCreateQueryBuilder().search();

		Request.Builder builder = newAuthenticatedRequestBuilder()
			.path(key)
			.query(queryBuilder.build())
			.method(GET);

		resetQueryBuilder();

		return newCall(builder.build());
	}

	/**
	 * Creates new RealTime instance to continuously receive updated query results in real-time.
	 *
	 * @param collection key/collection used to find organized data.
	 * @return {@link RealTime} RealTime instance. Server events can be listened on it.
	 */
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

	/**
	 * Adds an aggregation to this request {@link Query} instance.
	 *
	 * @param aggregation The {@link Aggregation} instance.
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData aggregate(Aggregation aggregation) {
		checkNotNull(aggregation, "Aggregation must be specified");

		getOrCreateQueryBuilder().aggregate(aggregation);

		return this;
	}

	/**
	 * Makes this request return the count of objects instead of returning the objects themselves.
	 *
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData count() {
		getOrCreateQueryBuilder().type("count");

		return this;
	}

	/**
	 * Adds a highlight entry to this request instance.
	 *
	 * @param field The field's name.
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData highlight(String field) {
		getOrCreateQueryBuilder().highlight(field);

		return this;
	}

	/**
	 * Sets the limit for this request's {@link Query}.
	 *
	 * @param limit The max amount of entries that this request should return.
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData limit(int limit) {
		getOrCreateQueryBuilder().limit(limit);

		return this;
	}

	/**
	 * Sets the offset for this request's {@link Query}.
	 *
	 * @param offset The index of the first entry that should be returned by this query.
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData offset(int offset) {
		getOrCreateQueryBuilder().offset(offset);

		return this;
	}

	/**
	 * Adds an ascending sort query to this request's body.
	 *
	 * @param field The field that the query should be sorted by.
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData orderBy(String field) {
		orderBy(field, SortOrder.ASCENDING);

		return this;
	}

	/**
	 * Adds a sort query to this request's body.
	 *
	 * @param field The field that the query should be sorted by.
	 * @param order The direction the sort operation should use.
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
	public WeDeployData orderBy(String field, SortOrder order) {
		checkNotNull(field, "Field must be specified");
		checkNotNull(field, "SortOrder must be specified");

		if (order == null) {
			throw new IllegalArgumentException("SortOrder can't be null");
		}

		getOrCreateQueryBuilder().sort(field, order.getValue());

		return this;
	}

	/**
	 * Adds a filter to this request's {@link Query}.
	 *
	 * @param filter a {@link Filter} instance
	 * @return {@link WeDeployData} Returns the object itself, so calls can be chained.
	 */
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
