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

package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.BodyConvertible;
import com.wedeploy.android.query.MapWrapper;
import com.wedeploy.android.query.filter.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

	public Aggregation aggregate(Aggregation... aggregations) {
		if (_aggregations == null) {
			_aggregations = new ArrayList<>();
		}

		Collections.addAll(_aggregations, aggregations);

		return this;
	}

	@Override
	public Object body() {
		Map<String, Object> map = new HashMap<>();

		map.put("name", name);
		map.put("operator", operator);

		if (value != null) {
			map.put("value", value);
		}

		if (_aggregations != null && !_aggregations.isEmpty()) {
			List<Object> bodies = new ArrayList<>(_aggregations.size());

			for (Aggregation aggregation : _aggregations) {
				bodies.add(aggregation.body());
			}

			map.put("aggregation", bodies);
		}

		return MapWrapper.wrap(field, map);
	}

	protected final Object value;
	private final String field;
	private final String name;
	private final String operator;
	private List<Aggregation> _aggregations;

}
