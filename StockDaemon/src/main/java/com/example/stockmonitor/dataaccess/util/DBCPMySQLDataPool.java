package com.example.stockmonitor.dataaccess.util;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.*;
/**
 * Utility class that leverages Apache DPCP2 for connection pooling
 * @author tuandao
 *
 */
public class DBCPMySQLDataPool implements SQLDataPool{
	private static BasicDataSource ds=null;
	//connection information
	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/stock?useSSL=false";
	private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "tuandao";
    private static final int CONN_POOL_SIZE = 10;

	private static void initDataSource(){
		ds=new BasicDataSource();
		ds.setDriverClassName(DRIVER_CLASS_NAME);
		ds.setUrl(DB_URL);
		ds.setUsername(DB_USER);
		ds.setPassword(DB_PASSWORD);
		ds.setInitialSize(CONN_POOL_SIZE);
	}
	@Override
	public DataSource getDataSource(){
		synchronized(DBCPMySQLDataPool.class){
			if (ds==null){
				initDataSource();
			}
		}
		return ds;
	}
}
