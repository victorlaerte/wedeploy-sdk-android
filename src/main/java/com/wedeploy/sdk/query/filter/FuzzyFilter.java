package com.wedeploy.sdk.query.filter;

import com.wedeploy.sdk.query.Util;

import java.util.Map;

/**
 * Fuzzy filter.
 */
public final class FuzzyFilter extends Filter {

	public FuzzyFilter fuzziness(int fuzziness) {
		this.mapValue.put("fuzziness", fuzziness);
		return this;
	}

	protected FuzzyFilter(String field, String operator, String query) {
		super(field, operator, Util.wrap("query", query));
		this.mapValue = (Map)this.value;
	}

	protected FuzzyFilter(
		String field, String operator, String query, Integer fuzziness) {

		this(field, operator, query);

		if (fuzziness != null) {
			fuzziness(fuzziness);
		}
	}

	private final Map mapValue;

}