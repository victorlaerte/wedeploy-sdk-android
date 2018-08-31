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

package com.wedeploy.android.query.aggregation;

import com.wedeploy.android.query.filter.BucketOrder;
import com.wedeploy.android.transport.Request;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a Terms {@link Aggregation}.
 * You can filter results that match any of the provided terms
 *
 * @author Victor Oliveira
 */
public class TermsAggregation extends Aggregation {

	TermsAggregation(String name, String field) {
		super(name, field, "terms");
	}

	TermsAggregation(String name, String field, int size) {
		super(name, field, "terms");
		this.size = size;
	}

	TermsAggregation(
		String name, String field, int size, BucketOrder... bucketOrder) {

		super(name, field, "terms");
		this.size = size;
		this.bucketOrders = new ArrayList<>(Arrays.asList(bucketOrder));
	}

	/**
	 * Adds {@link BucketOrder} instance to the current Aggregation.
	 * @param bucketOrder The bucket order to be added to this aggregation
	 * @return {@link TermsAggregation}
	 */
	public TermsAggregation addBucketOrder(BucketOrder... bucketOrder) {
		if (bucketOrder == null) {
			return this;
		}

		if (this.bucketOrders == null) {
			this.bucketOrders = new ArrayList<>();
		}

		this.bucketOrders.addAll(Arrays.asList(bucketOrder));

		return this;
	}

	/**
	 * Gets a map that represents the {@link Request} body for this
	 * {@link Aggregation}.
	 * @return {@link Map}
	 */
	@Override
	public Map body() {
		Map body = super.body();
		Map fieldMap = (Map) body.get(field);

		if (size != null) {
			fieldMap.put("size", size);
		}

		if (bucketOrders != null && !bucketOrders.isEmpty()) {
			fieldMap.put("order", bucketOrders);
		}

		return body;
	}

	private Integer size;
	private List<BucketOrder> bucketOrders;
}
