package com.wedeploy.sdk;

import com.wedeploy.sdk.exception.WeDeployException;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.Scanner;

import static com.wedeploy.sdk.Constants.AUTH;
import static com.wedeploy.sdk.Constants.DATA_URL;

/**
 * @author Silvio Santos
 */
public class DataTestHelper {

	public static void deleteData() throws WeDeployException {
		WeDeploy.data(DATA_URL)
			.auth(AUTH)
			.delete("messages")
			.execute();
	}

	public static void initDataFromFile(String path) throws WeDeployException {
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
