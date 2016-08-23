package com.example.stockmonitor.servlet;

public class ServletUtility {
	/**
	 * Find the absolute path to the rest api from the servlet/jsp path
	 * @param requestURL The request URL, e.g., http://localhost:8080/WebApp
	 * @param contextPath The "context" (app name) path, e.g., /WebApp
	 * @return the absolute path to the rest api, e.g., http://localhost:8080/WebApp/rest
	 */
	public static String getRestPath(String requestURL, String contextPath){
		int start=requestURL.indexOf(contextPath);
		String restApiPath=requestURL.substring(0,start)+contextPath+"/rest";
		return restApiPath;
	}
}
