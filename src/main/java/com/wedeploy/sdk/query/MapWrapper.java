package com.wedeploy.sdk.query;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Silvio Santos
 */
public class MapWrapper {

	/**
	 * Wraps a <code>(key, value)</code> pair into a {@link Map}.
	 *
	 * @param key the map key
	 * @param value the value associated to the key
	 * @return a map with a entry <code>(key, value)</code>
	 */
	public static Map wrap(String key, Object value) {
		Map map = new HashMap();

		map.put(key, value);
		return map;
	}

}
