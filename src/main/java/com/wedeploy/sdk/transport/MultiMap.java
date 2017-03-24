package com.wedeploy.sdk.transport;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Silvio Santos
 */
public interface MultiMap<V> {

	int size();

	Set<Map.Entry<String, List<V>>> entrySet();

	boolean isEmpty();

	List<V> get(String key);

	void put(String key, V value);

}
