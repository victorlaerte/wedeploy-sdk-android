package com.wedeploy.android.transport;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Silvio Santos
 */
public class SimpleMultiMapTest {

	@Test
	public void get() {
		MultiMap<String> multiMap = new SimpleMultiMap<>();
		multiMap.put("key", "value");

		assertEquals("value", multiMap.get("key").get(0));
	}

	@Test
	public void isEmpty() {
		MultiMap<String> multiMap = new SimpleMultiMap<>();
		assertTrue(multiMap.isEmpty());

		multiMap.put("key", "value");
		assertFalse(multiMap.isEmpty());
	}

	@Test
	public void put() {
		MultiMap<String> multiMap = new SimpleMultiMap<>();
		multiMap.put("key", "value");
		multiMap.put("key", "value2");
		multiMap.put("key1", "value");

		assertEquals(2, multiMap.size());
		assertEquals("value", multiMap.get("key").get(0));
		assertEquals("value2", multiMap.get("key").get(1));
	}

}
