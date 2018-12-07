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

import java.util.HashMap;
import java.util.Map;

/**
 * Range builder.
 */
public final class Range extends BodyConvertible {

	public static Range from(Object value) {
		return new Range(value, null, RangeEdges.INCLUDE_LOWER, RangeEdges.INCLUDE_UPPER);
	}

	public static Range range(Object from, Object to) {
		return new Range(from, to, RangeEdges.INCLUDE_LOWER, RangeEdges.INCLUDE_UPPER);
	}

	public static Range range(Object from, Object to, RangeEdges unknownEdge) {
		if (unknownEdge == RangeEdges.NONE) {
			return new Range(from, to, null, null);
		} else if (unknownEdge == RangeEdges.INCLUDE_UPPER) {
			return new Range(from, to, null, unknownEdge);
		} else {
			return new Range(from, to, unknownEdge, null);
		}
	}

	public static Range to(Object value) {
		return new Range(null, value, RangeEdges.INCLUDE_LOWER, RangeEdges.INCLUDE_UPPER);
	}

	@Override
	public Map body() {
		Map map = new HashMap();

		if (from != null) {
			map.put("from", from);
		}

		boolean includeLower = (lowerEdge != null && lowerEdge == RangeEdges.INCLUDE_LOWER);
		boolean includeUpper = (upperEdge != null && upperEdge == RangeEdges.INCLUDE_UPPER);

		map.put("includeLower", includeLower);
		map.put("includeUpper", includeUpper);

		if (to != null) {
			map.put("to", to);
		}

		return map;
	}

	protected final Object from;
	protected final Object to;
	private final RangeEdges lowerEdge;
	private final RangeEdges upperEdge;

	private Range(Object from, Object to, RangeEdges lowerEdge, RangeEdges upperEdge) {
		this.from = from;
		this.to = to;
		this.lowerEdge = lowerEdge;
		this.upperEdge = upperEdge;
	}
}
