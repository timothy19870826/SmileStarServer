package com.bigcat.utils;

public class UrlUtils {

	public static String[] getMethodPath(String queryString) {
		if (queryString == null || queryString.length() == 0 || queryString.indexOf("/") == -1) {
			return null;
		}
		return queryString.split("/");
	}

}
