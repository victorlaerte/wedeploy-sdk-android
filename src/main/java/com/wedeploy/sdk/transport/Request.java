package com.wedeploy.sdk.transport;

import com.wedeploy.sdk.internal.RequestMethod;
import com.wedeploy.sdk.query.Query;

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
		this.path = builder.path;
		this.url = builder.url;
	}

	private final String body;
	private final MultiMap<String> forms;
	private final MultiMap<String> headers;
	private final RequestMethod method;
	private final String path;
	private final String query;
	private final String url;

	public static class Builder {
		String body;
		MultiMap<String> forms = new SimpleMultiMap<>();
		MultiMap<String> headers = new SimpleMultiMap<>();
		RequestMethod method;
		String path = "";
		Query query = new Query.Builder().build();
		String url;

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Builder forms(String name, String value) {
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

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Request build() {
			return new Request(this);
		}

	}

}
