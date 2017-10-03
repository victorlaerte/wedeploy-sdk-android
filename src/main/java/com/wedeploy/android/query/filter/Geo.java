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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Geo builder.
 */
public abstract class Geo<T> extends BodyConvertible {

	public static BoundingBox boundingBox(Object upperLeft, Object lowerRight) {
		return new BoundingBox(upperLeft, lowerRight);
	}

	public static Circle circle(Object center, String radius) {
		return new Circle(center, radius);
	}

	public static Line line(Object... points) {
		return new Line(points);
	}

	public static Point point(double lat, double lon) {
		return new Point(lat, lon);
	}

	public static Polygon polygon(Object... points) {
		return new Polygon(points);
	}

	@Override
	public Object body() {
		return body;
	}

	protected final T body;

	private Geo(T body) {
		this.body = body;
	}

	public static final class BoundingBox extends Geo<Map> {

		protected BoundingBox(Object upperLeft, Object lowerRight) {
			super(new HashMap<>());
			body.put("type", "envelope");
			body.put("coordinates", Arrays.asList(upperLeft, lowerRight));
		}

		public List<Object> points() {
			return (List)body.get("coordinates");
		}

	}

	public static final class Circle extends Geo<Map> {

		protected Circle(Object center, String radius) {
			super(new HashMap<>());
			body.put("type", "circle");
			body.put("coordinates", center);
			body.put("radius", radius);
		}

		public Object center() {
			return body.get("coordinates");
		}

		public String radius() {
			return (String)body.get("radius");
		}

	}

	public static final class Line extends Geo<Map> {

		protected Line(Object... points) {
			super(new HashMap<>());
			body.put("type", "linestring");
			body.put("coordinates", Arrays.asList(points));
		}

	}

	public static final class Point extends Geo<List> {

		protected Point(double lat, double lon) {
			super(Arrays.asList(lon, lat));
		}

	}

	public static final class Polygon extends Geo<Map> {

		protected Polygon(Object... points) {
			super(new HashMap<>());
			lines = new ArrayList();
			lines.add(Arrays.asList(points));

			body.put("type", "polygon");
			body.put("coordinates", lines);
		}

		public Polygon hole(Object... points) {
			lines.add(Arrays.asList(points));
			return this;
		}

		private final List lines;

	}

}
