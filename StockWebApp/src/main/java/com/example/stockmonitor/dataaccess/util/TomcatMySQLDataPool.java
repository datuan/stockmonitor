package com.example.stockmonitor.dataaccess.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class TomcatMySQLDataPool implements SQLDataPool {
	private static DataSource ds=null;
	private static void initDataSource(){
		PoolProperties p=new PoolProperties();
		p.setUrl("jdbc:mysql://10.0.1.14:3306/stock?useSSL=false");
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername("tuan");
		p.setPassword("password");
		p.setMaxActive(100);
        p.setInitialSize(5);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        ds = new DataSource();
        ds.setPoolProperties(p);
	}
	@Override
	public javax.sql.DataSource getDataSource() {
		// TODO Auto-generated method stub
		synchronized (TomcatMySQLDataPool.class) {
			if (ds==null){
				initDataSource();
			}
		}
		return ds;
	}
}
