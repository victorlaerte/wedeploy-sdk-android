package com.wedeploy.sdk;

import com.wedeploy.sdk.exception.WeDeployException;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.Scanner;

import static com.wedeploy.sdk.Constants.AUTHORIZATION;
import static com.wedeploy.sdk.Constants.DATA_URL;

/**
 * @author Silvio Santos
 */
public class DataTestHelper {

	public static void deleteData() {
		try {
			weDeploy.data(DATA_URL)
				.authorization(AUTHORIZATION)
				.delete("messages")
				.execute();
		}
		catch (WeDeployException e) {
		}
	}

	public static void initDataFromFile(String path) throws WeDeployException {
		deleteData();

		InputStream is = WeDeployDataTest.class.getClassLoader()
			.getResourceAsStream(path);

		String json = new Scanner(is, "UTF-8")
			.useDelimiter("\\A")
			.next();

		weDeploy.data(DATA_URL)
			.authorization(AUTHORIZATION)
			.create("messages", new JSONArray(json))
			.execute();
	}

	private static WeDeploy weDeploy = new WeDeploy.Builder().build();

}
