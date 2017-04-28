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
