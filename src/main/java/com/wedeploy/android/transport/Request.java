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

package com.wedeploy.android.transport;

import com.wedeploy.android.query.Query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Silvio Santos
 */
public class Request {

	public String getBody() {
		return body;
	}

	public MultiMap<String> getForms() {
		return forms;
	}

	public MultiMap<String> getHeaders() {
		return headers;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public String getEncodedQuery() {
		return query;
	}

	public Map<String, String> getParams() {
		return Collections.unmodifiableMap(params);
	}

	public String getPath() {
		return path;
	}

	public String getUrl() {
		return url;
	}

	private Request(Builder builder) {
		this.body = builder.body;
		this.forms = builder.forms;
		this.headers = builder.headers;
		this.method = builder.method;
		this.query = builder.query.getEncodedQueryString();
		this.params = builder.params;
		this.path = builder.path;
		this.url = builder.url;
	}

	private final String body;
	private final MultiMap<String> forms;
	private final MultiMap<String> headers;
	private final RequestMethod method;
	private final Map<String, String> params;
	private final String path;
	private final String query;
	private final String url;

	public static class Builder {
		String body;
		MultiMap<String> forms = new SimpleMultiMap<>();
		MultiMap<String> headers = new SimpleMultiMap<>();
		RequestMethod method;
		Map<String, String> params = new HashMap<>();
		String path = "";
		Query query = new Query.Builder().build();
		String url;

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Builder form(String name, String value) {
			this.forms.put(name, value);
			return this;
		}

		public Builder header(String name, String value) {
			this.headers.put(name, value);
			return this;
		}

		public Builder method(RequestMethod method) {
			this.method = method;
			return this;
		}

		public Builder query(Query query) {
			this.query = query;
			return this;
		}

		public Builder param(String name, String value) {
			this.params.put(name, value);
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Request build() {
			if ((body != null) && !body.isEmpty() && !forms.isEmpty()) {
				throw new IllegalArgumentException("You can't set both request body and forms");
			}

			return new Request(this);
		}

	}

}
