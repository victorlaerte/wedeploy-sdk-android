package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.filter.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RangeAggregation extends Aggregation {

	RangeAggregation(String name, String field, Range... ranges) {
		super(name, field, "range", new ArrayList());
		((List)this.value).addAll(Arrays.asList(ranges));
	}

	public RangeAggregation range(Object from, Object to) {
		return range(Range.range(from, to));
	}

	public RangeAggregation range(Range range) {
		((List)this.value).add(range);
		return this;
	}

}
