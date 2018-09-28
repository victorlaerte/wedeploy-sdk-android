package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.SortOption;
import com.wedeploy.android.transport.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a pipeline {@link Aggregation} that sorts the buckets
 * of its parent aggregation.
 *
 * @author André Miranda
 */
public class BucketSortAggregation extends Aggregation {

	BucketSortAggregation(String name, String field, int size, int from,
		boolean skipGaps, SortOption... sort) {

		super(name, field, "sort", sort);

		this.from = from;
		this.size = size;
		this.skipGaps = skipGaps;
	}

	/**
	 * Gets a map that represents the {@link Request} body for this
	 * {@link Aggregation}.
	 *
	 * @return {@link Map}
	 */
	@Override
	public Map body() {
		Map body = super.body();
		Map fieldMap = (Map) body.get(getField());

		Map<String, String> options = new HashMap<>();
		options.put("gap_policy", skipGaps ? "skip" : "insert_zeros");

		fieldMap.put("from", from);
		fieldMap.put("size", size);
		fieldMap.put("options", options);

		return body;
	}

	private int from;
	private int size;
	private boolean skipGaps;
}
