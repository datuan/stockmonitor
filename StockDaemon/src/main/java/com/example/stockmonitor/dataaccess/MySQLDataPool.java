package com.example.stockmonitor.dataaccess;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
public class MySQLDataPool {
	private static BasicDataSource ds=null;
	//connection information
	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://10.0.1.14:3306/stock?useSSL=false";
	private static final String DB_USER = "tuan";
    private static final String DB_PASSWORD = "password";
    private static final int CONN_POOL_SIZE = 10;

	private static void initDataSource(){
		ds=new BasicDataSource();
		ds.setDriverClassName(DRIVER_CLASS_NAME);
		ds.setUrl(DB_URL);
		ds.setUsername(DB_USER);
		ds.setPassword(DB_PASSWORD);
		ds.setInitialSize(CONN_POOL_SIZE);
	}
	public static DataSource getDataSource(){
		synchronized(MySQLDataPool.class){
			if (ds==null){
				initDataSource();
			}
		}
		return ds;
	}
}
