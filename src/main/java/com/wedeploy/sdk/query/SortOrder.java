package com.wedeploy.sdk.query;

/**
 * @author Silvio Santos
 */
public enum SortOrder {

	ASCENDING("asc"), DESCENDING("desc");

	public String getValue() {
		return value;
	}

	SortOrder(String value) {
		this.value = value;
	}

	private final String value;

}
