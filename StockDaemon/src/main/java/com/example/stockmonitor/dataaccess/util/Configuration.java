package com.example.stockmonitor.dataaccess.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	static String DB_HOST="localhost:3306";
	static String DB_NAME="stock";
	static String DB_URL = "jdbc:mysql://localhost:3306/stock?useSSL=false";
	static String mySQLSSL="?useSSL=false";
	static String DB_USER = "root";
    static String DB_PASSWORD = "tuandao";
    static int CONN_POOL_SIZE = 10;
    //interval to query stock prices
    public static int INTERVAL=300000;
    
    public static void setConfiguration(String configFile){
    	InputStream input=null;
    	try{
    		input=new FileInputStream(configFile);
    		Properties prop=new Properties();
    		prop.load(input);
    		
    		DRIVER_CLASS_NAME=prop.getProperty("dbclass");
    		DB_HOST=prop.getProperty("dbhost");
    		DB_NAME=prop.getProperty("dbname");
    		DB_URL="jdbc:mysql://"+DB_HOST+"/"+DB_NAME+mySQLSSL;
    		DB_USER=prop.getProperty("dbuser");
    		DB_PASSWORD=prop.getProperty("dbpassword");
    		INTERVAL=Integer.parseInt(prop.getProperty("interval"));
    	}
    	catch(IOException e){
    		//do nothing
    	}
    	finally{
    		try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
