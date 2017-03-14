package com.wedeploy.sdk.query;

import com.wedeploy.sdk.query.aggregation.Aggregation;
import com.wedeploy.sdk.query.filter.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query builder.
 */
public class Query extends BodyConvertible {

	@Override
	public Object body() {
		return body;
	}

	public static final class Builder {

		public Builder aggregate(Aggregation aggregation) {
			aggregations.add(aggregation);
			return this;
		}

		public Builder aggregate(String name, String field, String operator) {
			return aggregate(Aggregation.of(name, field, operator));
		}

		public Query build() {
			Map<String, Object> body = new HashMap();

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

			return new Query(body);
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
			sort.add(BodyToJsonStringConverter.wrap(field, direction));
			return this;
		}

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		private final List<Aggregation> aggregations = new ArrayList();
		private final List<Filter> filters = new ArrayList();
		private final List<String> highlights = new ArrayList<>();
		private Integer limit;
		private Integer offset;
		private final List<Filter> queries = new ArrayList();
		private final List<Map> sort = new ArrayList();
		private String type;

	}

	protected final Map<String, Object> body;

	private Query(Map<String, Object> body) {
		this.body = body;
	}

}