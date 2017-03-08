package com.wedeploy.sdk;

import com.wedeploy.sdk.internal.OkHttpTransport;
import com.wedeploy.sdk.internal.RequestMethod;
import org.json.JSONObject;

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

		Call call = new Call(request, new OkHttpTransport());
		Response response = call.execute();

		if (!response.succeeded()) {
			throw new WeDeployException(response.getStatusMessage());
		}

		JSONObject jsonBody = new JSONObject(response.getBody());
		String token = jsonBody.getString("access_token");
		TokenAuth auth = new TokenAuth(token);
		WeDeploy.auth = auth;

		return getCurrentUser();

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

		Call call = new Call(builder.build(), new OkHttpTransport());
		Response response = call.execute();

		if (!response.succeeded()) {
			throw new WeDeployException(response.getStatusMessage());
		}

		JSONObject jsonBody = new JSONObject(response.getBody());

		return new User(jsonBody);
	}

	private String url;

}


