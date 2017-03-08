package com.wedeploy.sdk;

import org.json.JSONObject;

/**
 * @author Silvio Santos
 */
public class User {

	public User(JSONObject jsonObject) {
		id = jsonObject.getString("id");
		email = jsonObject.getString("email");
		name = jsonObject.getString("name");
		createdAt = jsonObject.getLong("createdAt");
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	private final long createdAt;
	private final String id;
	private final String email;
	private final String name;

}
