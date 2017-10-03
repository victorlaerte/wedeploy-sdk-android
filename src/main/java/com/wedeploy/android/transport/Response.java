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

import java.util.List;
import java.util.Map;

/**
 * @author Silvio Santos
 */
public class Response {

	public String getBody() {
		return body;
	}

	public String getHeader(String name) {
		List<String> values = headers.get(name);

		if ((values != null) && !values.isEmpty()) {
			return values.get(values.size() - 1);
		}

		return null;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public boolean succeeded() {
		return succeeded;
	}

	private Response(Builder builder) {
		this.body = builder.body;
		this.headers = builder.headers;
		this.statusCode = builder.statusCode;
		this.statusMessage = builder.statusMessage;
		this.succeeded = builder.succeeded;
	}

	private final String body;
	private final Map<String, List<String>> headers;
	private final int statusCode;
	private final String statusMessage;
	private final boolean succeeded;

	public static class Builder {
		String body;
		Map<String, List<String>> headers;
		int statusCode;
		String statusMessage;
		boolean succeeded;

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Builder headers(Map<String, List<String>> headers) {
			this.headers = headers;
			return this;
		}

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder statusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
			return this;
		}

		public Builder succeeded(boolean succeeded) {
			this.succeeded = succeeded;
			return this;
		}

		public Response build() {
			return new Response(this);
		}

	}

}
