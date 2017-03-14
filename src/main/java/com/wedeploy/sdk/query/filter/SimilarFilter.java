package com.wedeploy.sdk.query.filter;

import com.wedeploy.sdk.query.Util;

import java.util.Arrays;
import java.util.Map;

/**
 * More regex this filter.
 */
public final class SimilarFilter extends Filter {

	public SimilarFilter maxDf(int value) {
		mapValue.put("maxDf", value);
		return this;
	}

	public SimilarFilter minDf(int value) {
		mapValue.put("minDf", value);
		return this;
	}

	public SimilarFilter minTf(int value) {
		mapValue.put("minTf", value);
		return this;
	}

	public SimilarFilter stopWords(String...words) {
		mapValue.put("stopWords", Arrays.asList(words));
		return this;
	}

	protected SimilarFilter(String field, String query) {
		super(field, "similar", Util.wrap("query", query));
		this.mapValue = (Map)this.value;
	}

	private final Map mapValue;

}