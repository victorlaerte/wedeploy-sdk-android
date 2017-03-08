package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Silvio Santos
 */
class WeDeployAuth {

	WeDeployAuth(String url) {
		this.url = url;
	}

	public User signIn(String email, String password) {
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
		TokenAuth auth = new TokenAuth(token);
		WeDeploy.auth = auth;

		return getCurrentUser();

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
		Auth auth = WeDeploy.auth;

		if (auth == null) {
			throw new IllegalStateException("There is no signed in user");
		}

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

		if (WeDeploy.auth == null) {
			return builder;
		}

		return WeDeploy.auth.authenticate(builder);
	}

	private Call<Response> newCall(Request request) {
		return new Call<>(request, new OkHttpTransport(), Response.class);
	}

	private String url;

}


