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

	private static void initDataSource(){
		ds=new BasicDataSource();
		ds.setDriverClassName(Configuration.DRIVER_CLASS_NAME);
		ds.setUrl(Configuration.DB_URL);
		ds.setUsername(Configuration.DB_USER);
		ds.setPassword(Configuration.DB_PASSWORD);
		ds.setInitialSize(Configuration.CONN_POOL_SIZE);
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
