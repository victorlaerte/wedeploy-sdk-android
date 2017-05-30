package com.wedeploy.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.wedeploy.android.auth.Authorization;
import com.wedeploy.android.auth.ProviderAuthorization;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import org.json.JSONObject;

import static com.wedeploy.android.util.Validator.checkNotNull;

/**
 * The WeDeployAuth service provides authentication using email/password or popular third-party
 * identity providers like Google, Facebook, and GitHub.
 *
 * The Auth service allows you to avoid the headache of operating your own user management system.
 * Features like creating accounts, resetting passwords, and updating profiles are easily
 * accomplished with a few lines of code.
 *
 * This class is not thread safe. In order to avoid concurrency issues, you must create a new instance
 * by calling {@link WeDeploy#auth(String)} for every request you want to do on {@link WeDeployAuth}.
 *
 * @author Silvio Santos
 */
public class WeDeployAuth extends BaseWeDeployService<WeDeployAuth> {

	/**
	 * Constructs a {@link WeDeployAuth} instance.
	 *
	 * @param weDeploy A WeDeploy instance.
	 * @param url The WeDeploy Auth service url.
	 */
	WeDeployAuth(WeDeploy weDeploy, String url) {
		super(weDeploy, url);
	}

	/**
	 * Signs in using email and password.
	 *
	 * @param email The user email
	 * @param password The user password
	 * @return {@link Call}
	 */
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

	/**
	 * Signs in using using OAuth authentication.
	 * This method starts the phone browser and displays the provider's authentication page.
	 * After signing in, an Activity registered in the AndroidManifest.xml will receive an intent
	 * with the authentication token. You can then create a
	 * {@link com.wedeploy.android.auth.TokenAuthorization} and pass it to authorize your requests
	 * on WeDeploy.
	 *
	 * To register the Activity you want to receive the token, you need to declare an intent filter
	 * like the one below. Don't forget to replace [mypackagename] by your application package name.
	 *
	 * <intent-filter>
	 *  <action android:name="android.intent.action.VIEW" />
	 *  <category android:name="android.intent.category.DEFAULT" />
	 *  <category android:name="android.intent.category.BROWSABLE" />
	 *  <data android:scheme="oauth-wedeploy" android:host="[mypackagename]" />
	 * </intent-filter>
	 *
	 * @param activity An Android Activity used to start the phone's browser.
	 * @param provider The {@link ProviderAuthorization} you want to authenticate on.
	 */
	public void signIn(Activity activity, ProviderAuthorization provider) {
		Uri oauthUrl = Uri.parse(url + provider.getAuthUrl());

		Intent intent = new Intent(Intent.ACTION_VIEW, oauthUrl);
		activity.startActivity(intent);
	}

	/**
	 * Revokes the current being used token from WeDeploy. Any following requests with the provided
	 * will be rejected.
	 *
	 * @return {@link Call}
	 */
	public Call<Response> signOut() {
		Authorization authorization = getAuthorization();

		checkNotNull(authorization, "You must be signed in");

		Request request = newAuthenticatedRequestBuilder()
			.path("oauth/revoke")
			.param("token", authorization.getToken())
			.method(RequestMethod.GET)
			.build();

		return newCall(request);
	}

	/**
	 * Creates a user on WeDeploy.
	 *
	 * @param email The user email.
	 * @param password The user password.
	 * @param name The user name.
	 * @return {@link Call}
	 */
	public Call<Response> createUser(String email, String password, String name) {
		checkNotNull(email, "Email must be specified");
		checkNotNull(password, "Password must be specified");

		JSONObject json = new JSONObject()
			.put("email", email)
			.put("password", password)
			.put("name", name);

		Request.Builder builder = newAuthenticatedRequestBuilder()
			.path("users")
			.body(json.toString())
			.method(RequestMethod.POST);

		return newCall(builder.build());
	}

	/**
	 * Deletes a user from WeDeploy.
	 *
	 * @param userId The user id.
	 * @return {@link Call}
	 */
	public Call<Response> deleteUser(String userId) {
		checkNotNull(userId, "userId must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path("/users/" + userId)
			.method(RequestMethod.DELETE)
			.build();

		return newCall(request);
	}

	/**
	 * Gets the user associated with the current being used token.
	 * @return {@link Call}
	 */
	public Call<Response> getCurrentUser() {
		checkNotNull(getAuthorization(), "You must be signed in");

		Request.Builder builder = newAuthenticatedRequestBuilder()
			.path("user")
			.method(RequestMethod.GET);

		return newCall(builder.build());
	}

	/**
	 * Gets a user from WeDeploy.
	 *
	 * @param userId The user id.
	 * @return {@link Call}
	 */
	public Call<Response> getUser(String userId) {
		checkNotNull(userId, "userId must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path("users/" + userId)
			.method(RequestMethod.GET)
			.build();

		return newCall(request);
	}

	/**
	 * Updates a user on WeDeploy.
	 *
	 * @param id The user id.
	 * @param fields The fields to be updated.
	 * @return {@link Call}
	 */
	public Call<Response> updateUser(String id, JSONObject fields) {
		checkNotNull(id, "id must be specified");

		if (fields == null || fields.length() == 0) {
			throw new IllegalArgumentException("Fields must be specified");
		}

		fields.put("id", id);

		Request request = newAuthenticatedRequestBuilder()
			.path("users/" + id)
			.body(fields.toString())
			.method(RequestMethod.PATCH)
			.build();

		return newCall(request);
	}

	/**
	 * Sends an email with the instructions to reset the user's password.
	 *
	 * @param email The email associated with the account you want the password to be recovered.
	 * @return {@link Call}
	 */
	public Call<Response> sendPasswordResetEmail(String email) {
		checkNotNull(email, "email must be specified");

		Request request = newAuthenticatedRequestBuilder()
			.path("user/recover")
			.form("email", email)
			.method(RequestMethod.POST)
			.build();

		return newCall(request);
	}

}


