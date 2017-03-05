package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.RequestMethod;

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

    public Map<String, String> getForms() {
        return Collections.unmodifiableMap(forms);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public RequestMethod getMethod() {
        return method;
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
        this.params = builder.params;
        this.path = builder.path;
        this.url = builder.url;
    }

    public static class Builder {
        String body;
        Map<String, String> forms = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        RequestMethod method;
        Map<String, String> params = new HashMap<>();
        String path = "";
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
            return new Request(this);
        }
    }

    private final String body;
    private final Map<String, String> forms;
    private final Map<String, String> headers;
    private final RequestMethod method;
    private final Map<String, String> params;
    private final String path;
    private final String url;

}
