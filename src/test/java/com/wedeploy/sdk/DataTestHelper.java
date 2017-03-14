package com.wedeploy.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

import static com.wedeploy.sdk.Constants.AUTH;
import static com.wedeploy.sdk.Constants.DATA_URL;

/**
 * @author Silvio Santos
 */
public class DataTestHelper {

	public static void deleteData() {
		WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.delete("messages")
			.execute();
	}

	public static void initDataFromFile(String path) {
		deleteData();

		InputStream is = WeDeployDataTest.class.getClassLoader()
			.getResourceAsStream(path);

		String json = new Scanner(is, "UTF-8")
			.useDelimiter("\\A")
			.next();

		WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.create("messages", new JSONArray(json))
			.execute();
	}
}
