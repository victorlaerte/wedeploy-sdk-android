package com.wedeploy.android.util;

/**
 * @author Silvio Santos
 */
public class URLUtil {

	/**
	 * Parses the url separating the domain and port from the path.
	 */
	public static String joinPathAndQuery(String path, String query) {
		StringBuilder builder = new StringBuilder();

		if (path != null) {
			builder.append(path);
		}

		if ((query != null) && (query.length() > 0)) {
			builder.append('?').append(query);
		}

		return builder.toString();
	}

}
