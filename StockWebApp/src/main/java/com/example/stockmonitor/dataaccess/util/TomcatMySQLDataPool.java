package com.example.stockmonitor.dataaccess.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
/**
 * Provide a connection pool to web application
 * @author tuandao
 *
 */
public class TomcatMySQLDataPool implements SQLDataPool {
	private static DataSource ds=null;
	private static void initDataSource(){
		String dbhost = "", dbuser="", dbpassword="", dbdriver="", dbname="", dbclass="";
		//read configuration from web.xml file
		try {
			Context env=(Context) new InitialContext().lookup("java:comp/env");
			dbhost=(String) env.lookup("dbhost");
			dbuser=(String) env.lookup("dbuser");
			dbpassword=(String) env.lookup("dbpassword");
			dbdriver=(String) env.lookup("dbdriver");
			dbname=(String) env.lookup("dbname");
			dbclass=(String) env.lookup("dbcls");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//put some warning to the website?
		}
		
		
		
		PoolProperties p=new PoolProperties();
		String url=dbdriver+"://"+dbhost+"/"+dbname+"?useSSL=false";
		p.setUrl(url);
		p.setDriverClassName(dbclass);
		p.setUsername(dbuser);
		p.setPassword(dbpassword);
		p.setJmxEnabled( true);
	    p.setTestWhileIdle( true);
	    p.setTestOnBorrow( true);
	    p.setValidationQuery( "SELECT 1");
	    p.setTestOnReturn( false);
		p.setMaxActive(15);
        p.setInitialSize(5);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(3);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"
                + "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");
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
