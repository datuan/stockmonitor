package com.example.stockmonitor.servlet;

public class ServletUtility {
	public static String getRestPath(String requestURL, String contextPath){
		int start=requestURL.indexOf(contextPath);
		String restApiPath=requestURL.substring(0,start)+contextPath+"/rest";
		return restApiPath;
	}
}
