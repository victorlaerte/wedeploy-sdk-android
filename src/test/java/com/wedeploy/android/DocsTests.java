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

import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.transport.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import static com.wedeploy.android.query.aggregation.Aggregation.avg;
import static com.wedeploy.android.query.filter.Filter.*;

/**
 * @author Silvio Santos
 */
@Ignore
public class DocsTests {

	@Test
	public void docs_auth_createUser() throws WeDeployException {
		WeDeploy
			.auth("<auth-url>")
			.createUser("user@domain.com", "password", "somename")
			.execute();
	}

	@Test
	public void docs_auth_getCurrentUser() throws WeDeployException {
		WeDeploy
			.auth("<auth-url>")
			.getCurrentUser()
			.execute();
	}

	@Test
	public void docs_auth_getUser() throws WeDeployException {
		WeDeploy
			.auth("<auth-url>")
			.getUser("userId")
			.execute();
	}

	@Test
	public void docs_auth_getAllUsers() throws WeDeployException {
		WeDeploy
			.auth("https://<serviceID>-<projectID>.wedeploy.io")
			.getAllUsers()
			.execute();
	}

	@Test
	public void docs_auth_deleteUser() throws WeDeployException {
		WeDeploy
			.auth("https://<serviceID>-<projectID>.wedeploy.io")
			.deleteUser("userId")
			.execute();
	}

	@Test
	public void docs_auth_updateUser() throws WeDeployException {
		JSONObject fields = new JSONObject()
			.put("email", "eleven@hawkinslabs.com")
			.put("password", "newPassword")
			.put("name", "Eleven");

		WeDeploy
			.auth("<auth-url>")
			.updateUser("userId", fields)
			.execute();
	}

	@Test
	public void docs_auth_sendResetPasswordEmail() throws WeDeployException {
		WeDeploy
			.auth("<auth-url>")
			.sendPasswordResetEmail("user@domain.com")
			.execute();

	}

	@Test
	public void docs_auth_sign() throws WeDeployException {
		WeDeploy
			.auth("<auth-url>")
			.signIn("user@domain.com", "password")
			.execute();
	}

	@Test
	public void docs_auth_signOut() throws WeDeployException {
		WeDeploy
			.auth("<auth-url>")
			.signOut()
			.execute();
	}

	@Test
	public void docs_data_delete() {
		WeDeployData data = WeDeploy.data("http://datademo.wedeploy.io");

		data.delete("movies/star_wars_v/title");

		data.delete("movies/star_wars_v");

		data.delete("movies");
	}

	@Test
	public void docs_data_realtime() {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.watch("movies")
			.on("changes", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {

				}
			})
			.on("fail", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {

				}
			});

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(equal("category", "cinema").or("category", "cartoon"))
			.watch("movies")
			.on("changes", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... data) {
					System.out.println(data);
				}
			})
			.on("fail", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... error) {
					System.out.println(error);
				}
			});

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.limit(1)
			.orderBy("id", SortOrder.DESCENDING)
			.watch("movies")
			.on("changes", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {

				}
			})
			.on("fail", new RealTime.OnEventListener() {
				@Override
				public void onEvent(Object... args) {

				}
			});
	}

	@Test
	public void docs_data_get() throws WeDeployException {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.get("movies/star_wars_v")
			.execute();

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.get("movies/star_wars_v/title")
			.execute();
	}

	@Test
	public void docs_data_orderBy() throws WeDeployException {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.orderBy("rating", SortOrder.DESCENDING)
			.get("movies")
			.execute();
	}

	@Test
	public void docs_data_filter() throws WeDeployException {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(lt("year", 2000).or(gt("rating", 8.5)))
			.get("movies")
			.execute();
	}

	@Test
	public void docs_data_limit_offset() throws WeDeployException {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(gt("year", 2000))
			.orderBy("rating")
			.limit(2)
			.offset(1)
			.get("movies")
			.execute();
	}

	@Test
	public void docs_data_save() throws WeDeployException {
		JSONObject movieJsonObject = new JSONObject()
			.put("title", "Star Wars IV")
			.put("year", 1977)
			.put("rating", 8.7);

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.create("movies", movieJsonObject)
			.execute();

		JSONObject movie1JsonObject = new JSONObject()
			.put("title", "Star Wars III")
			.put("year", 2005)
			.put("rating", 8.0);

		JSONObject movie2JsonObject = new JSONObject()
			.put("title", "Star Wars II")
			.put("year", 2002)
			.put("rating", 8.6);

		JSONArray moviesJsonArray = new JSONArray()
			.put(movie1JsonObject)
			.put(movie2JsonObject);

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.create("movies", moviesJsonArray)
			.execute();

		movieJsonObject = new JSONObject()
			.put("title", "Star Wars I")
			.put("obs", "First in ABC order")
			.put("year", 1999)
			.put("rating", 9.0);

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.create("movies", movieJsonObject)
			.execute();

	}

	@Test
	public void docs_data_search() throws WeDeployException {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(match("title", "Sith's revenge"))
			.get("movies")
			.execute();

		//====

		// we can run this
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(match("title", "(jedi | force) -return"))
			.get("movies")
			.execute();

		// or this
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(match("title", "awake*"))
			.get("movies")
			.execute();

		// or even this
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(match("title", "awake~"))
			.get("movies")
			.execute();

		//=====
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(similar("title", "The attack an awaken Jedi uses to strike a Sith is pure " +
				"force!"))
			.search("movies")
			.execute();

		//====
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(similar("title", "The attack an awakened Jedi uses to strike a Sith is pure " +
				"force!"))
			.highlight("title")
			.search("movies")
			.execute();

		//====
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(lt("year", 1990))
			.aggregate(avg("Old Movies", "rating"))
			.count()
			.get("movies")
			.execute();

		JSONObject locationJsonObject = new JSONObject()
			.put("location", "geo_point");

		JSONObject placesJsonObject = new JSONObject();
		placesJsonObject.put("places", locationJsonObject);

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.create("", placesJsonObject);

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.where(any("category", "cinema").and(distance("location", "51.5031653,-0.1123051",
				"1mi")))
			.get("places")
			.execute();
	}

	@Test
	public void docs_data_update() throws WeDeployException {
		JSONObject movieJsonObject = new JSONObject()
			.put("rating", 9.1);

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.update("movies/115992383516607958", movieJsonObject)
			.execute();
	}

	@Test
	public void docs_email_send() throws WeDeployException {
		WeDeploy
			.email("http://<serviceID>.<projectID>.wedeploy.io/emails")
			.from("from@domain.com")
			.to("to@domain.com")
			.subject("Hi there!")
			.send()
			.execute();

		//====
		WeDeploy
			.email("http://<serviceID>.<projectID>.wedeploy.io")
			.checkEmailStatus("<emailID>")
			.execute();
	}

	@Test
	public void docs_intro() throws WeDeployException {
		WeDeploy
			.data("http://datademo.wedeploy.io")
			.get("movies")
			.execute();

		WeDeploy
			.data("http://datademo.wedeploy.io")
			.get("movies")
			.execute(new Callback() {
				@Override
				public void onSuccess(Response response) {

				}

				@Override
				public void onFailure(Exception e) {

				}
			});
	}

	private static WeDeploy WeDeploy = new WeDeploy.Builder().build();
}
