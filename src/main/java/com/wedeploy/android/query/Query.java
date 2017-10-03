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

package com.wedeploy.android.query;

import com.wedeploy.android.query.aggregation.Aggregation;
import com.wedeploy.android.query.filter.Filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query builder.
 */
public class Query extends BodyConvertible {

	public Query(Builder builder) {
		this.aggregations = builder.aggregations;
		this.filters = builder.filters;
		this.highlights = builder.highlights;
		this.limit = builder.limit;
		this.offset = builder.offset;
		this.queries = builder.queries;
		this.sort = builder.sort;
		this.type = builder.type;
	}

	public Map<String, Object> body() {
		Map<String, Object> body = new HashMap<>();

		if (type != null) {
			body.put("type", type);
		}

		if (!filters.isEmpty()) {
			body.put("filter", filters);
		}

		if (!sort.isEmpty()) {
			body.put("sort", sort);
		}

		if (limit != null) {
			body.put("limit", limit);
		}

		if (offset != null) {
			body.put("offset", offset);
		}

		if (!queries.isEmpty()) {
			body.put("search", queries);
		}

		if (!highlights.isEmpty()) {
			body.put("highlight", highlights);
		}

		if (!aggregations.isEmpty()) {
			body.put("aggregation", aggregations);
		}

		return body;
	}

	public String getQueryString() {
		return getQueryString(false);
	}

	public String getEncodedQueryString() {
		return getQueryString(true);
	}

	private String getQueryString(boolean encode) {
		Map<String, Object> body = body();

		if (body.isEmpty()) {
			return "";
		}

		StringBuilder queryString = new StringBuilder();

		for (Map.Entry<String, Object> entry : body.entrySet()) {
			try {
				String value = BodyToJsonStringConverter.toString(entry.getValue());

				if (encode) {
					value = URLEncoder.encode(value, "UTF-8");
				}

				queryString.append(entry.getKey());
				queryString.append("=");
				queryString.append(value);
				queryString.append("&");
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Couldn't encode query", e);
			}
		}

		queryString.deleteCharAt(queryString.length() - 1);

		return queryString.toString();
	}

	private final List<Aggregation> aggregations;
	private final List<Filter> filters;
	private final List<String> highlights;
	private final Integer limit;
	private final Integer offset;
	private final List<Filter> queries;
	private final List<Map> sort;
	private final String type;

	public static class Builder {

		List<Aggregation> aggregations = new ArrayList<>();
		List<Filter> filters = new ArrayList<>();
		List<String> highlights = new ArrayList<>();
		Integer limit;
		Integer offset;
		List<Filter> queries = new ArrayList<>();
		List<Map> sort = new ArrayList<>();
		String type;

		public Builder aggregate(Aggregation aggregation) {
			aggregations.add(aggregation);
			return this;
		}

		public Builder aggregate(String name, String field, String operator) {
			return aggregate(Aggregation.of(name, field, operator));
		}

		public Builder count() {
			return type("count");
		}

		public Builder fetch() {
			return type("fetch");
		}

		public Builder filter(Filter filter) {
			filters.add(filter);
			return this;
		}

		public Builder filter(String field, Object value) {
			return filter(Filter.field(field, value));
		}

		public Builder filter(String field, String operator, Object value) {
			return filter(Filter.field(field, operator, value));
		}

		public Builder highlight(String field) {
			highlights.add(field);
			return this;
		}

		public Builder limit(int limit) {
			this.limit = limit;
			return this;
		}

		public Builder offset(int offset) {
			this.offset = offset;
			return this;
		}

		public Builder search() {
			return type("search");
		}

		public Builder search(Filter filter) {
			queries.add(filter);
			return this;
		}

		public Builder search(String text) {
			return search(Filter.match(text));
		}

		public Builder search(String field, String text) {
			return search(Filter.match(field, text));
		}

		public Builder search(String field, String operator, Object value) {
			return search(Filter.field(field, operator, value));
		}

		public Builder sort(String field) {
			return sort(field, "asc");
		}

		public Builder sort(String field, String direction) {
			sort.add(MapWrapper.wrap(field, direction));
			return this;
		}

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Query build() {
			return new Query(this);
		}

	}

}
