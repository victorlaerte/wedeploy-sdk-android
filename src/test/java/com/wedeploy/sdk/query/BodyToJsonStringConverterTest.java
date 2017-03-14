package com.wedeploy.sdk.query;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

public class BodyToJsonStringConverterTest {

	@Test
	public void testToString_withArray() {
		Assert.assertEquals("[1,2]", BodyToJsonStringConverter.toString(new int[] {1, 2}));
		Assert.assertEquals(
			"[1,\"a\",true,{\"key\":\"value\"}]",
			BodyToJsonStringConverter.toString(
				new Object[] {1, "a", true, MapWrapper.wrap("key", "value")}));
	}

	@Test
	public void testToString_withCollection() {
		Assert.assertEquals("[]", BodyToJsonStringConverter.toString(Arrays.asList()));
		Assert.assertEquals("[]", BodyToJsonStringConverter.toString(new HashSet(0)));
		Assert.assertEquals(
			"[1,\"a\",true,{\"key\":\"value\"}]",
			BodyToJsonStringConverter.toString(
				Arrays.asList(1, "a", true, MapWrapper.wrap("key", "value"))));
	}

	@Test
	public void testToString_withEmbodied() {
		BodyConvertible bodyConvertible = new BodyConvertible() {

			@Override
			public Object body() {
				return "body";
			}

		};

		Assert.assertEquals("\"body\"", BodyToJsonStringConverter.toString(bodyConvertible));
	}

	@Test
	public void testToString_withMap() {
		Assert.assertEquals("{}", BodyToJsonStringConverter.toString(Collections.emptyMap()));
		Assert.assertEquals("{\"key\":1}", BodyToJsonStringConverter.toString(MapWrapper.wrap("key", 1)));
	}

	@Test
	public void testToString_withObject() {
		Object value = MapWrapper.wrap("key", 1);
		Assert.assertEquals("{\"key\":1}", BodyToJsonStringConverter.toString(value));
		value = "a\"\\\n\r\f\t\b/";
		Assert.assertEquals(
			"\"a\\\"\\\\\\n\\r\\f\\t\\b\\/\"", BodyToJsonStringConverter.toString(value));
		value = 1;
		Assert.assertEquals("1", BodyToJsonStringConverter.toString(value));
		value = Arrays.asList();
		Assert.assertEquals("[]", BodyToJsonStringConverter.toString(value));
		value = new int[0];
		Assert.assertEquals("[]", BodyToJsonStringConverter.toString(value));
		value = (new BodyConvertible() {

			@Override
			public Object body() {
				return "body";
			}

		});

		Assert.assertEquals("\"body\"", BodyToJsonStringConverter.toString(value));
	}

	@Test
	public void testToString_withPrimitive() {
		Assert.assertEquals("1", BodyToJsonStringConverter.toString(1));
		Assert.assertEquals("true", BodyToJsonStringConverter.toString(true));
		Assert.assertEquals("0.5", BodyToJsonStringConverter.toString(0.5d));
	}

	@Test
	public void testToString_withString() {
		Assert.assertEquals(
			"\"a\\\"\\\\\\n\\r\\f\\t\\b\\/\"",
			BodyToJsonStringConverter.toString("a\"\\\n\r\f\t\b/"));
	}

	@Test
	public void testUtil_constructorDummyCoverage() {
		new BodyToJsonStringConverter();
	}

	@Test
	public void testWrap() {
		String name = "name";
		Object value = 1;
		Map map = MapWrapper.wrap(name, value);
		Assert.assertNotNull(map);
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(name, map.keySet().iterator().next());
		Assert.assertEquals(value, map.get(name));
	}

}