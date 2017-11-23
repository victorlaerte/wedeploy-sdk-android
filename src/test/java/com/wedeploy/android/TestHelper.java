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

import com.wedeploy.android.auth.TokenAuthorization;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.MultiMap;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.Scanner;

import static com.wedeploy.android.Constants.AUTHORIZATION;
import static com.wedeploy.android.Constants.DATA_URL;

/**
 * @author Silvio Santos
 */
public class TestHelper {

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

	public static MultiMap<String> getHeaders(
		String url, TokenAuthorization authorization, boolean withCredentials) {

		return weDeploy.url(url)
			.authorization(authorization)
			.withCredentials(withCredentials)
			.newAuthenticatedRequestBuilder()
			.build()
			.getHeaders();
	}

	private static WeDeploy weDeploy = new WeDeploy.Builder().build();

}
