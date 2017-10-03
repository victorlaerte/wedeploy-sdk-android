/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Liferay, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.wedeploy.android.query.filter;

import com.wedeploy.android.query.BodyConvertible;
import com.wedeploy.android.query.MapWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Filter builder.
 */
public class Filter extends BodyConvertible {

	protected Filter(String operator, Object value) {
		this.field = null;
		this.operator = operator;
		this.value = value;
	}

	protected Filter(String field, String operator, Object value) {
		if (isComposite(operator)) {
			throw new IllegalArgumentException(
				"\"" + operator + "\" is a composite filter operator. " +
					"Please use Filter.composite(operator, filters) for that.");
		}

		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	public static Filter any(String field, Iterable values) {
		return field(field, "any", values);
	}

	public static Filter any(String field, Object... values) {
		return field(field, "any", Arrays.asList(values));
	}

	public static Filter boundingBox(String field, Geo.BoundingBox box) {
		return Filter.field(field, "gp", box.points());
	}

	public static Filter boundingBox(
		String field, Object upperLeft, Object lowerRight) {

		return boundingBox(field, Geo.boundingBox(upperLeft, lowerRight));
	}

	public static Filter composite(String operator, Filter... filters) {
		Filter compositeFilter = new Filter(operator, new ArrayList<>());

		if (filters != null) {
			for (Filter filter : filters) {
				compositeFilter.addToComposite(filter);
			}
		}

		return compositeFilter;
	}

	public static Filter distance(String field, Geo.Circle circle) {
		return distance(field, circle.center(), Range.to(circle.radius()));
	}

	public static Filter distance(String field, Object location, Range range) {
		Map map = new HashMap();

		map.put("location", location);

		if (range.from != null) {
			map.put("min", range.from);
		}

		if (range.to != null) {
			map.put("max", range.to);
		}

		return Filter.field(field, "gd", map);
	}

	public static Filter distance(
		String field, Object location, String distance) {

		return distance(field, location, Range.to(distance));
	}

	public static Filter equal(String field, Object value) {
		return field(field, "=", value);
	}

	public static Filter exists(String field) {
		return Filter.field(field, "exists", null);
	}

	public static Filter field(String field, Object value) {
		return field(field, "=", value);
	}

	public static Filter field(String field, String operator, Object value) {
		return new Filter(field, operator, value);
	}

	public static FuzzyFilter fuzzy(String query) {
		return fuzzy(ALL, query);
	}

	public static FuzzyFilter fuzzy(String query, Integer fuzziness) {
		return fuzzy(ALL, query, fuzziness);
	}

	public static FuzzyFilter fuzzy(String field, String query) {
		return new FuzzyFilter(field, "fuzzy", query);
	}

	public static FuzzyFilter fuzzy(
		String field, String query, Integer fuzziness) {

		return new FuzzyFilter(field, "fuzzy", query, fuzziness);
	}

	public static Filter gt(String field, Object value) {
		return field(field, ">", value);
	}

	public static Filter gte(String field, Object value) {
		return field(field, ">=", value);
	}

	public static Filter lt(String field, Object value) {
		return field(field, "<", value);
	}

	public static Filter lte(String field, Object value) {
		return field(field, "<=", value);
	}

	public static Filter match(String query) {
		return match(ALL, query);
	}

	public static Filter match(String field, String query) {
		return Filter.field(field, "match", query);
	}

	public static Filter missing(String field) {
		return Filter.field(field, "missing", null);
	}

	public static Filter none(String field, Iterable values) {
		return field(field, "none", values);
	}

	public static Filter none(String field, Object... values) {
		return field(field, "none", Arrays.asList(values));
	}

	public static Filter not(Filter filter) {
		return Filter.composite("not", filter);
	}

	public static Filter not(String field, Object value) {
		return not(Filter.field(field, value));
	}

	public static Filter not(String field, String operator, Object value) {
		return not(Filter.field(field, operator, value));
	}

	public static Filter notEqual(String field, Object value) {
		return field(field, "!=", value);
	}

	public static Filter phrase(String query) {
		return phrase(ALL, query);
	}

	public static Filter phrase(String field, String query) {
		return Filter.field(field, "phrase", query);
	}

	public static Filter polygon(String field, Object... points) {
		return Filter.field(field, "gp", Arrays.asList(points));
	}

	public static Filter prefix(String query) {
		return prefix(ALL, query);
	}

	public static Filter prefix(String field, String query) {
		return Filter.field(field, "prefix", query);
	}

	public static Filter range(String field, Object min, Object max) {
		return Filter.field(field, "range", Range.range(min, max));
	}

	public static Filter range(String field, Range range) {
		return Filter.field(field, "range", range);
	}

	public static Filter regex(String field, String value) {
		return field(field, "~", value);
	}

	public static GeoShapeFilter shape(String field, Object... shapes) {
		return new GeoShapeFilter(field, shapes);
	}

	public static SimilarFilter similar(String query) {
		return similar(ALL, query);
	}

	public static SimilarFilter similar(String field, String query) {
		return new SimilarFilter(field, query);
	}

	public Filter and(Filter filter) {
		if (isComposite(this.operator)) {
			return this.addToComposite("and", filter);
		}

		return Filter.composite("and", this, filter);
	}

	public Filter and(String field, Object value) {
		return and(Filter.field(field, value));
	}

	public Filter and(String field, String operator, Object value) {
		return and(Filter.field(field, operator, value));
	}

	@Override
	public Object body() {
		if (isComposite(operator)) {
			List valueList = (List)value;

			if (valueList.size() == 1) {
				return MapWrapper.wrap(operator, valueList.get(0));
			}

			return MapWrapper.wrap(operator, value);
		}

		Map map = new HashMap();

		map.put("operator", operator);

		if (value != null) {
			map.put("value", value);
		}

		return MapWrapper.wrap(field, map);
	}

	public Filter or(Filter filter) {
		if (isComposite(this.operator)) {
			return this.addToComposite("or", filter);
		}

		return Filter.composite("or", this, filter);
	}

	public Filter or(String field, Object value) {
		return or(Filter.field(field, value));
	}

	public Filter or(String field, String operator, Object value) {
		return or(Filter.field(field, operator, value));
	}

	protected Filter addToComposite(Filter filter) {
		((List)this.value).add(filter);
		return this;
	}

	protected Filter addToComposite(String newOperator, Filter filter) {
		if (this.operator.equals(newOperator)) {
			return addToComposite(filter);
		}

		return Filter.composite(newOperator, this, filter);
	}

	private boolean isComposite(String filter) {
		if ((filter != null) && COMPOSITE_FILTERS.contains(filter)) {
			return true;
		}

		return false;
	}

	protected final String field;
	protected final String operator;
	protected final Object value;
	private static final String ALL = "*";

	private static final Set<String> COMPOSITE_FILTERS = new HashSet<>(
		Arrays.asList("and", "or", "not"));

}
