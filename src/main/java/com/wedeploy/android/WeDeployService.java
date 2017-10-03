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
 * This class helps to communicate with custom WeDeploy services.
 * It offers generic methods for building and firing requests to them.
 * <p>
 * This class is not thread safe. In order to avoid concurrency issues, you must create a new
 * instance by calling {@link WeDeploy#url(String)} for every request you want to do on a
 * {@link WeDeployService}.
 *
 * @author Silvio Santos
 */
public class WeDeployService extends BaseWeDeployService<WeDeployService> {

	/**
	 * Constructs a {@link WeDeployService} instance.
	 *
	 * @param weDeploy A WeDeploy instance.
	 * @param url The WeDeployService url.
	 */
	WeDeployService(WeDeploy weDeploy, String url) {
		super(weDeploy, url);
	}

	/**
	 * Sets the Http request body.
	 *
	 * @param body
	 * @return {@link WeDeployService} Returns the {@link WeDeployService} object itself,
	 * so calls can be chained.
	 */
	public WeDeployService body(String body) {
		this.body = body;

		return this;
	}

	/**
	 * Adds a key/value pair to be sent via the body in a 'multipart/form-data' format.
	 * If the body is set by other means (for example, through the {@link #body(String)} method),
	 * this will be ignored.
	 *
	 * @param name The field name
	 * @param value The field value
	 * @return {@link WeDeployService} Returns the {@link WeDeployService} object itself,
	 * so calls can be chained.
	 */
	public WeDeployService form(String name, String value) {
		this.forms.put(name, value);

		return this;
	}

	/**
	 * Adds a query. If the query with the same name already exists, it will not
	 * be overwritten, but new value will be stored.
	 *
	 * @param name Param name.
	 * @param value Param value.
	 * @return {@link WeDeployService} Returns the {@link WeDeployService} object itself,
	 * so calls can be chained.
	 */
	public WeDeployService param(String name, String value) {
		this.params.put(name, value);

		return this;
	}

	/**
	 * Sets the url path. A path value set by this method will override any previous set path.
	 *
	 * @param path The url path.
	 * @return {@link WeDeployService} Returns the {@link WeDeployService} object itself,
	 * so calls can be chained.
	 */
	public WeDeployService path(String path) {
		this.path = path;

		return this;
	}

	/**
	 * Returns {@link Call} object that represents a DELETE http request.
	 *
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
	public Call<Response> delete() {
		return newCall(newRequest(DELETE));
	}

	/**
	 * Returns {@link Call} object that represents a GET http request.
	 *
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
	public Call<Response> get() {
		return newCall(newRequest(GET));
	}

	/**
	 * Returns {@link Call} object that represents a PATCH http request.
	 *
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
	public Call<Response> patch() {
		return newCall(newRequest(PATCH));
	}

	/**
	 * Returns {@link Call} object that represents a POST http request.
	 *
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
	public Call<Response> post() {
		return newCall(newRequest(POST));
	}

	/**
	 * Returns {@link Call} object that represents a PUT http request.
	 *
	 * @return {@link Call} Returns a Call object that encapsulates the request.
	 */
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
