package com.wedeploy.sdk.query.aggregation;

import com.wedeploy.sdk.query.filter.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceAggregation extends Aggregation {

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

	DistanceAggregation(
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