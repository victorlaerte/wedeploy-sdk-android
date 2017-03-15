package com.wedeploy.sdk;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.auth.TokenAuth;
import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Silvio Santos
 */
class WeDeployAuth {

	WeDeployAuth(String url) {
		this.url = url;
	}

	public WeDeployAuth auth(Auth auth) {
		this.auth = auth;

		return this;
	}

	public Auth signIn(String email, String password) {
		Request request = new Request.Builder()
			.url(url)
			.forms("grant_type", "password")
			.forms("username", email)
			.forms("password", password)
			.path("oauth/token")
			.method(RequestMethod.POST)
			.build();

		Response response = newCall(request).execute();

		if (!response.succeeded()) {
			throw new WeDeployException(response.getStatusMessage());
		}

		JSONObject jsonBody = new JSONObject(response.getBody());
		String token = jsonBody.getString("access_token");

		return new TokenAuth(token);

	}

	public User createUser(String email, String password, String name) {
		JSONObject json = new JSONObject()
			.put("email", email)
			.put("password", password)
			.put("name", name);

		Request.Builder builder = newAuthenticatedBuilder()
			.path("users")
			.body(json.toString())
			.method(RequestMethod.POST);

		Response response = newCall(builder.build())
			.execute();

		if (!response.succeeded()) {
			throw new WeDeployException(response.getStatusMessage());
		}

		return new User(new JSONObject(response.getBody()));
	}

	public User getCurrentUser() {
		Request.Builder builder = new Request.Builder()
			.url(url)
			.path("user")
			.method(RequestMethod.GET);

		auth.authenticate(builder);

		Response response = newCall(builder.build()).execute();

		if (!response.succeeded()) {
			throw new WeDeployException(response.getStatusMessage());
		}

		JSONObject jsonBody = new JSONObject(response.getBody());

		return new User(jsonBody);
	}

	//TODO Change fields to be a Map<String, Any>
	public void updateUser(String id, Map<String, String> fields) {
		if (id == null) {
			throw new IllegalArgumentException("User id can't be null");
		}

		if (fields == null || fields.isEmpty()) {
			throw new IllegalArgumentException("You must provide fields to be updated");
		}

		JSONObject body = new JSONObject()
			.put("id", id);

		for (Map.Entry<String, String> entry : fields.entrySet()) {
			body.put(entry.getKey(), entry.getValue());
		}

		Request request = newAuthenticatedBuilder()
			.path("users/" + id)
			.body(body.toString())
			.method(RequestMethod.PATCH)
			.build();

		Response response = newCall(request).execute();

		if (!response.succeeded()) {
			throw new WeDeployException(response.getStatusMessage());
		}
	}

	private Request.Builder newAuthenticatedBuilder() {
		Request.Builder builder = new Request.Builder()
			.url(url);

		if (auth == null) {
			return builder;
		}

		return auth.authenticate(builder);
	}

	private Call<Response> newCall(Request request) {
		return new Call<>(request, new OkHttpTransport(), Response.class);
	}

	private Auth auth;
	private String url;

}


