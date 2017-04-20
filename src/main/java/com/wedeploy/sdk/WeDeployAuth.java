package com.wedeploy.sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.wedeploy.sdk.auth.Authorization;
import com.wedeploy.sdk.auth.ProviderAuthorization;
import com.wedeploy.sdk.transport.RequestMethod;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONObject;

import static com.wedeploy.sdk.util.Validator.checkNotNull;

/**
 * @author Silvio Santos
 */
public class WeDeployAuth extends WeDeployService<WeDeployAuth> {

	WeDeployAuth(WeDeploy weDeploy, String url) {
		super(weDeploy);

		this.url = url;
	}

	public Call<Response> signIn(String email, String password) {
		checkNotNull(email, "Email must be specified");
		checkNotNull(password, "Password must be specified");

		Request request = new Request.Builder()
			.url(url)
			.form("grant_type", "password")
			.form("username", email)
			.form("password", password)
			.path("oauth/token")
			.method(RequestMethod.POST)
			.build();

		return newCall(request);
	}

	public void signIn(Activity activity, ProviderAuthorization provider) {
		Uri oauthUrl = Uri.parse(url + provider.getAuthUrl());

		Intent intent = new Intent(Intent.ACTION_VIEW, oauthUrl);
		activity.startActivity(intent);
	}

	public Call<Response> signOut() {
		Authorization authorization = getAuthorization();

		checkNotNull(authorization, "You must be signed in");

		Request request = newAuthenticatedRequestBuilder(url)
			.path("oauth/revoke")
			.param("token", authorization.getToken())
			.method(RequestMethod.GET)
			.build();

		return newCall(request);
	}

	public Call<Response> createUser(String email, String password, String name) {
		checkNotNull(email, "Email must be specified");
		checkNotNull(password, "Password must be specified");

		JSONObject json = new JSONObject()
			.put("email", email)
			.put("password", password)
			.put("name", name);

		Request.Builder builder = newAuthenticatedRequestBuilder(url)
			.path("users")
			.body(json.toString())
			.method(RequestMethod.POST);

		return newCall(builder.build());
	}

	public Call<Response> deleteUser(String userId) {
		checkNotNull(userId, "userId must be specified");

		Request request = newAuthenticatedRequestBuilder(url)
			.path("/users/" + userId)
			.method(RequestMethod.DELETE)
			.build();

		return newCall(request);
	}

	public Call<Response> getCurrentUser() {
		checkNotNull(getAuthorization(), "You must be signed in");

		Request.Builder builder = newAuthenticatedRequestBuilder(url)
			.path("user")
			.method(RequestMethod.GET);

		return newCall(builder.build());
	}

	public Call<Response> getUser(String userId) {
		checkNotNull(getAuthorization(), "userId must be specified");

		Request request = newAuthenticatedRequestBuilder(url)
			.path("users/" + userId)
			.method(RequestMethod.GET)
			.build();

		return newCall(request);
	}

	public Call<Response> updateUser(String id, JSONObject fields) {
		checkNotNull(id, "id must be specified");

		if (fields == null || fields.length() == 0) {
			throw new IllegalArgumentException("Fields must be specified");
		}

		fields.put("id", id);

		Request request = newAuthenticatedRequestBuilder(url)
			.path("users/" + id)
			.body(fields.toString())
			.method(RequestMethod.PATCH)
			.build();

		return newCall(request);
	}

	public Call<Response> sendPasswordResetEmail(String email) {
		Request request = newAuthenticatedRequestBuilder(url)
			.path("user/recover")
			.form("email", email)
			.method(RequestMethod.POST)
			.build();

		return newCall(request);
	}

	private String url;

}


