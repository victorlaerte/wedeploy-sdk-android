package com.wedeploy.sdk.query.aggregation;

import com.wedeploy.sdk.query.BaseEmbodied;
import com.wedeploy.sdk.query.Query;
import com.wedeploy.sdk.query.filter.Range;
import com.wedeploy.sdk.query.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregation builder.
 */
public class Aggregation extends BaseEmbodied {

	public static Aggregation avg(String name, String field) {
		return of(name, field, "avg");
	}

	public static Aggregation count(String name, String field) {
		return of(name, field, "count");
	}

	public static DistanceAggregation distance(
		String name, String field, Object location, Range...ranges) {

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
		String name, String field, Range...ranges) {

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

		return Util.wrap(field, map);
	}

	@Override
	public String toString() {
		return Util.toString(new Query.Builder().aggregate(this).build());
	}

	public static final class DistanceAggregation extends Aggregation {

		public DistanceAggregation range(Object from, Object to) {
			return range(Range.range(from, to));
		}

		public DistanceAggregation range(Range range) {
			this.ranges.add(range);
			return this;
		}

		public DistanceAggregation unit(String unit) {
			((Map)value).put("unit", unit);
			return this;
		}

		private DistanceAggregation(
			String name, String field, Object location, Range...ranges) {

			super(name, field, "geoDistance", new HashMap());

			Map map = (Map)value;
			this.ranges = new ArrayList();
			this.ranges.addAll(Arrays.asList(ranges));

			map.put("location", location);
			map.put("ranges", this.ranges);
		}

		private final List<Object> ranges;

	}

	public static final class RangeAggregation extends Aggregation {

		public RangeAggregation range(Object from, Object to) {
			return range(Range.range(from, to));
		}

		public RangeAggregation range(Range range) {
			((List)this.value).add(range);
			return this;
		}

		private RangeAggregation(String name, String field, Range...ranges) {
			super(name, field, "range", new ArrayList());
			((List)this.value).addAll(Arrays.asList(ranges));
		}

	}

	protected final Object value;

	private Aggregation(String name, String field, String operator) {
		this(name, field, operator, null);
	}

	private Aggregation(
		String name, String field, String operator, Object value) {

		this.name = name;
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	private final String field;
	private final String name;
	private final String operator;

}