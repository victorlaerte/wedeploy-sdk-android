package com.wedeploy.sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.wedeploy.sdk.auth.AuthProvider;
import com.wedeploy.sdk.internal.RequestMethod;
import com.wedeploy.sdk.transport.Request;
import com.wedeploy.sdk.transport.Response;
import org.json.JSONObject;

import java.util.Map;

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
			.forms("grant_type", "password")
			.forms("username", email)
			.forms("password", password)
			.path("oauth/token")
			.method(RequestMethod.POST)
			.build();

		return newCall(request);
	}

	public void signIn(Activity activity, AuthProvider provider) {
		Uri oauthUrl = Uri.parse(url + provider.getAuthUrl());

		Intent intent = new Intent(Intent.ACTION_VIEW, oauthUrl);
		activity.startActivity(intent);
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

	public Call<Response> getCurrentUser() {
		Request.Builder builder = newAuthenticatedRequestBuilder(url)
			.path("user")
			.method(RequestMethod.GET);

		return newCall(builder.build());
	}

	//TODO Change fields to be a Map<String, Any>
	public Call<Response> updateUser(String id, Map<String, String> fields) {
		checkNotNull(id, "id must be specified");

		if (fields == null || fields.isEmpty()) {
			throw new IllegalArgumentException("Fields must be specified");
		}

		JSONObject body = new JSONObject()
			.put("id", id);

		for (Map.Entry<String, String> entry : fields.entrySet()) {
			body.put(entry.getKey(), entry.getValue());
		}

		Request request = newAuthenticatedRequestBuilder(url)
			.path("users/" + id)
			.body(body.toString())
			.method(RequestMethod.PATCH)
			.build();

		return newCall(request);
	}

	private String url;

}


