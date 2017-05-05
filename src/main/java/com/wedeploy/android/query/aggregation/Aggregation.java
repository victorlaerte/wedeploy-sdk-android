package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.BodyConvertible;
import com.wedeploy.android.query.MapWrapper;
import com.wedeploy.android.query.filter.Range;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregation builder.
 */
public class Aggregation extends BodyConvertible {

	Aggregation(String name, String field, String operator) {
		this(name, field, operator, null);
	}

	Aggregation(
		String name, String field, String operator, Object value) {

		this.name = name;
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public static Aggregation avg(String name, String field) {
		return of(name, field, "avg");
	}

	public static Aggregation count(String name, String field) {
		return of(name, field, "count");
	}

	public static DistanceAggregation distance(
		String name, String field, Object location, Range... ranges) {

		return new DistanceAggregation(name, field, location, ranges);
	}

	public static Aggregation extendedStats(String name, String field) {
		return of(name, field, "extendedStats");
	}

	public static Aggregation histogram(
		String name, String field, int interval) {

		return new Aggregation(name, field, "histogram", interval);
	}

	public static Aggregation max(String name, String field) {
		return of(name, field, "max");
	}

	public static Aggregation min(String name, String field) {
		return of(name, field, "min");
	}

	public static Aggregation missing(String name, String field) {
		return of(name, field, "missing");
	}

	public static Aggregation of(String name, String field, String operator) {
		return new Aggregation(name, field, operator);
	}

	public static RangeAggregation range(
		String name, String field, Range... ranges) {

		return new RangeAggregation(name, field, ranges);
	}

	public static Aggregation stats(String name, String field) {
		return of(name, field, "stats");
	}

	public static Aggregation sum(String name, String field) {
		return of(name, field, "sum");
	}

	public static Aggregation terms(String name, String field) {
		return of(name, field, "terms");
	}

	@Override
	public Object body() {
		Map map = new HashMap();

		map.put("name", name);
		map.put("operator", operator);

		if (value != null) {
			map.put("value", value);
		}

		return MapWrapper.wrap(field, map);
	}

	protected final Object value;
	private final String field;
	private final String name;
	private final String operator;

}