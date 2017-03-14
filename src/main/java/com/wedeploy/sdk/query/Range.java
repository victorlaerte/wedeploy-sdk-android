package com.wedeploy.sdk.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Range builder.
 */
public final class Range extends BaseEmbodied {

	public static Range from(Object value) {
		return new Range(value, null);
	}

	public static Range range(Object from, Object to) {
		return new Range(from, to);
	}

	public static Range to(Object value) {
		return new Range(null, value);
	}

	@Override
	public Map body() {
		Map map = new HashMap();

		if (from != null) {
			map.put("from", from);
		}

		if (to != null) {
			map.put("to", to);
		}

		return map;
	}

	@Override
	public String toString() {
		return bodyAsJson();
	}

	protected final Object from;
	protected final Object to;

	private Range(Object from, Object to) {
		this.from = from;
		this.to = to;
	}

}