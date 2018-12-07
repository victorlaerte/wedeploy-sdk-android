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


import com.wedeploy.android.query.filter.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that represents a Filter {@link Aggregation}.
 * You can filter the results of a query based on a condition, before
 * doing an aggregation.
 */
public class FilterAggregation extends Aggregation {

    FilterAggregation(String name, String field, Filter... filters) {
        super(name, field, "filter", new ArrayList());
        ((List)this. getValue()).addAll(Arrays.asList(filters));
    }

    /**
     * Adds a filter to current aggregation.
     * @param name The field name of {@link Filter}
     * @param operator The operator of {@link Filter}
     * @param value The value of {@link Filter}
     * @return {@link FilterAggregation}
     */
    public FilterAggregation filter(String name, String operator, Object value) {
        return filter(Filter.field(name, operator, value));
    }

    /**
     * Adds a filter to current aggregation.
     * @param filter Instance of {@link Filter}
     * @return {@link FilterAggregation}
     */
    public FilterAggregation filter(Filter filter) {
        ((List)this.getValue()).add(filter);
        return this;
    }
}
