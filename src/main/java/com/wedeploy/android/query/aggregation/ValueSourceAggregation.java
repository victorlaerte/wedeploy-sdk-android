package com.wedeploy.android.query.aggregation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Victor Oliveira
 */
public class ValueSourceAggregation extends Aggregation {

	ValueSourceAggregation(String name, String field, String operator) {
		super(name, field, operator);
	}

	ValueSourceAggregation(String name, String field, String operator,
		Object value) {
		super(name, field, operator, value);
	}

	public ValueSourceAggregation script(String script) {
		options.put("script", script);

		return this;
	}

	@Override
	public Map body() {
		Map body = super.body();
		Map fieldMap = (Map) body.get(getField());

		if (!options.isEmpty()) {
			fieldMap.put("options", options);
		}

		return body;
	}

	private Map<String, Object> options = new HashMap<>();
}
