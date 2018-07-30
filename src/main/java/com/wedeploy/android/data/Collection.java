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

package com.wedeploy.android.data;

import com.wedeploy.android.WeDeployData;
import com.wedeploy.android.query.BodyConvertible;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents a {@link WeDeployData} collection.
 *
 * <pre><code>
 * CollectionFieldMap genreFieldTypeMap = new CollectionFieldMap();
 * innerCollection.put("name", CollectionFieldType.STRING);
 * innerCollection.put("description", CollectionFieldType.STRING);
 *
 * CollectionFieldMap movieMap = new CollectionFieldMap();
 * mapping.put("title", CollectionFieldType.STRING);
 * mapping.put("genre", genreFieldTypeMap);
 *
 * Collection collection = new Collection("movies", movieMap);
 * </code></pre>
 *
 * @author Victor Oliveira
 */
public class Collection extends BodyConvertible {

	/**
	 * Constructs a {@link Collection} instance.
	 *
	 * @param name The collection name.
	 * @param mapping The collection field type value. This value can contain.
	 * field names and their types mapping but also nested collections field types.
	 */
	public Collection(String name, CollectionFieldTypeValue mapping) {
		this.name = name;
		this.mapping = mapping;
	}

	/**
	 * Gets the {@link Collection} name.
	 *
	 * @return {@link String} collection name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the {@link Collection} name.
	 *
	 * @param name Collection name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the {@link Collection} field type value mapping.
	 *
	 * @return {@link String} collection name.
	 */
	public CollectionFieldTypeValue getMapping() {
		return mapping;
	}

	/**
	 * Sets the {@link Collection} field type value mapping.
	 *
	 * @param map the field type value map.
	 */
	public void setMapping(CollectionFieldTypeValue map) {
		this.mapping = map;
	}

	/**
	 * Gets the embodied object representation.
	 *
	 * @return {@link Map} key, value representation.
	 */
	@Override
	public Map<String, Object> body() {

		Map<String, Object> map = new TreeMap<>();

		map.put("name", name);
		map.put("mapping", mapping);

		return map;
	}

	private String name;
	private CollectionFieldTypeValue mapping;
}
