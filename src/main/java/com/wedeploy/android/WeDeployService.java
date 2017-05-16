package com.wedeploy.android;

import com.wedeploy.android.transport.MultiMap;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.SimpleMultiMap;

import java.util.List;
import java.util.Map;

import static com.wedeploy.android.transport.RequestMethod.*;

/**
 * @author Silvio Santos
 */
public class WeDeployService extends BaseWeDeployService<WeDeployService> {

	WeDeployService(WeDeploy weDeploy, String url) {
		super(weDeploy, url);
	}

	public WeDeployService body(String body) {
		this.body = body;

		return this;
	}

	public WeDeployService form(String name, String value) {
		this.forms.put(name, value);

		return this;
	}

	public WeDeployService param(String name, String value) {
		this.params.put(name, value);

		return this;
	}

	public WeDeployService path(String path) {
		this.path = path;

		return this;
	}

	public Call<Response> delete() {
		return newCall(newRequest(DELETE));
	}

	public Call<Response> get() {
		return newCall(newRequest(GET));
	}

	public Call<Response> patch() {
		return newCall(newRequest(PATCH));
	}

	public Call<Response> post() {
		return newCall(newRequest(POST));
	}

	public Call<Response> put() {
		return newCall(newRequest(PUT));
	}

	private Request newRequest(RequestMethod method) {
		Request.Builder builder = newAuthenticatedRequestBuilder()
			.body(body)
			.path(path)
			.method(method);

		for (Map.Entry<String, List<String>> entry : forms.entrySet()) {
			for (String value : entry.getValue()) {
				builder.form(entry.getKey(), value);
			}
		}

		for (Map.Entry<String, List<String>> entry : params.entrySet()) {
			for (String value : entry.getValue()) {
				builder.param(entry.getKey(), value);
			}
		}

		return builder.build();
	}

	private String body;
	private MultiMap<String> forms = new SimpleMultiMap<>();
	private MultiMap<String> params = new SimpleMultiMap<>();
	private String path;

}
