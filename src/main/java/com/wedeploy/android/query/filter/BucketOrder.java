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
import com.wedeploy.android.query.SortOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Victor Oliveira
 */
public class BucketOrder extends BodyConvertible {

	public static BucketOrder count(SortOrder sortOrder) {
		return new BucketOrder("_count", sortOrder);
	}

	public static BucketOrder key(SortOrder sortOrder) {
		return new BucketOrder("_key", sortOrder);
	}

	public static BucketOrder path(String path, SortOrder sortOrder) {
		return new BucketOrder(path, sortOrder, true);
	}

	@Override
	public Map body() {
		Map<String, Object> map = new HashMap<>();

		if (key != null) {
			map.put("key", key);
		}

		if (sortOrder != null) {
			map.put("asc", sortOrder == SortOrder.ASCENDING);
		}

		return map;
	}

	private BucketOrder(String key, SortOrder sortOrder) {
		this(key, sortOrder, false);
	}

	private BucketOrder(String key, SortOrder sortOrder, boolean isPath) {
		this.key = key;
		this.sortOrder = sortOrder;
		this.isPath = isPath;
	}

	public boolean isPath() {
		return isPath;
	}

	private boolean isPath = false;
	private String key;
	private SortOrder sortOrder;
}
