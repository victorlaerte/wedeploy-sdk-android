package com.wedeploy.sdk.query;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

public class UtilTest {

	@Test
	public void testToString_withArray() {
		Assert.assertEquals("[1,2]", Util.toString(new int[] {1, 2}));
		Assert.assertEquals(
			"[1,\"a\",true,{\"key\":\"value\"}]",
			Util.toString(
				new Object[] {1, "a", true, Util.wrap("key", "value")}));
	}

	@Test
	public void testToString_withCollection() {
		Assert.assertEquals("[]", Util.toString(Arrays.asList()));
		Assert.assertEquals("[]", Util.toString(new HashSet(0)));
		Assert.assertEquals(
			"[1,\"a\",true,{\"key\":\"value\"}]",
			Util.toString(
				Arrays.asList(1, "a", true, Util.wrap("key", "value"))));
	}

	@Test
	public void testToString_withEmbodied() {
		Embodied embodied = new BaseEmbodied() {

			@Override
			public Object body() {
				return "body";
			}

		};

		Assert.assertEquals("\"body\"", Util.toString(embodied));
	}

	@Test
	public void testToString_withMap() {
		Assert.assertEquals("{}", Util.toString(Collections.emptyMap()));
		Assert.assertEquals("{\"key\":1}", Util.toString(Util.wrap("key", 1)));
	}

	@Test
	public void testToString_withObject() {
		Object value = Util.wrap("key", 1);
		Assert.assertEquals("{\"key\":1}", Util.toString(value));
		value = "a\"\\\n\r\f\t\b/";
		Assert.assertEquals(
			"\"a\\\"\\\\\\n\\r\\f\\t\\b\\/\"", Util.toString(value));
		value = 1;
		Assert.assertEquals("1", Util.toString(value));
		value = Arrays.asList();
		Assert.assertEquals("[]", Util.toString(value));
		value = new int[0];
		Assert.assertEquals("[]", Util.toString(value));
		value = (new BaseEmbodied() {

			@Override
			public Object body() {
				return "body";
			}

		});

		Assert.assertEquals("\"body\"", Util.toString(value));
	}

	@Test
	public void testToString_withPrimitive() {
		Assert.assertEquals("1", Util.toString(1));
		Assert.assertEquals("true", Util.toString(true));
		Assert.assertEquals("0.5", Util.toString(0.5d));
	}

	@Test
	public void testToString_withString() {
		Assert.assertEquals(
			"\"a\\\"\\\\\\n\\r\\f\\t\\b\\/\"",
			Util.toString("a\"\\\n\r\f\t\b/"));
	}

	@Test
	public void testUtil_constructorDummyCoverage() {
		new Util();
	}

	@Test
	public void testWrap() {
		String name = "name";
		Object value = 1;
		Map map = Util.wrap(name, value);
		Assert.assertNotNull(map);
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(name, map.keySet().iterator().next());
		Assert.assertEquals(value, map.get(name));
	}

}