package com.wedeploy.android.transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Silvio Santos
 */
public class SimpleMultiMap<V> implements MultiMap<V> {

	@Override
	public List<V> get(String key) {
		return Collections.unmodifiableList(map.get(key));
	}

	@Override
	public Set<Map.Entry<String, List<V>>> entrySet() {
		return Collections.unmodifiableSet(map.entrySet());
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public void put(String key, V value) {
		List<V> values = map.get(key);

		if (values == null) {
			values = new ArrayList<>();
			map.put(key, values);
		}

		values.add(value);
	}

	@Override
	public int size() {
		return map.size();
	}

	private Map<String, List<V>> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

}
