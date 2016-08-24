package com.example.stockmonitor;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.stockmonitor.data.Stock;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	static String DB_HOST="localhost:3306";
	static String DB_NAME="stock";
	static String DB_URL = "jdbc:mysql://localhost:3306/stock?useSSL=false";
	static String mySQLSSL="?useSSL=false";
	static String DB_USER = "root";
    static String DB_PASSWORD = "tuandao";
    static String WebURL="http://localhost:8080/StockWebApp/";
    
    private Client client=null;
    private void prepareClient(String userName, String password){
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
			    .credentials(userName, password)
			    .build();

			ClientConfig clientConfig = new ClientConfig();
			clientConfig.register(feature) ;
			client = ClientBuilder.newClient(clientConfig);
	}
    
	@Test
	public void testMySQLConnection(){
    	//test connect to MySQL server
		try {
			Class.forName(DRIVER_CLASS_NAME);
			Connection conn=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			Statement stmt=conn.createStatement();
			String sql="Select 1";
			ResultSet rs=stmt.executeQuery(sql);
			int count=0;
			while (rs.next()){
				count++;
			}
			rs.close();
			stmt.close();
			conn.close();
			assertEquals(1, count);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Cannot load MySQL driver");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Cannot connect to MySQL");
		}
    }
    
    @Test
    public void testUnauthorizeRestInvoke(){
    	client=ClientBuilder.newClient();
    	WebTarget target=client.target(WebURL+"/rest").path("/stock/");
		Response res=target.request(MediaType.TEXT_PLAIN).get();
		int responseCode=res.getStatus();
		assertEquals("Non authenticated user cannot access rest api",401, responseCode);
    }
    
    @Test
    public void testAdd(){
    	prepareClient("junit", "");
    	String symbol="junit";
    	WebTarget target=client.target(WebURL+"/rest").path("/stock/"+symbol);
    	
		Response res=target.request(MediaType.TEXT_PLAIN).post(Entity.text(symbol));
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			fail("Bad response from server");
		}
		String str=res.readEntity(String.class);
//		assertTrue(str.equals("ADD_SUCCESS"));
		assertEquals(str, "ADD_SUCCESS");
    }
    
    @Test
    public void testDelete(){
    	prepareClient("junit", "");
    	String symbol="junit";
    	WebTarget target=client.target(WebURL+"/rest").path("/stock/"+symbol);
		Response res=target.request(MediaType.TEXT_PLAIN).delete();
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			fail("Bad response from server");
		}
		String str=res.readEntity(String.class);
//		assertTrue(str.equals("DEL_SUCCESS"));
		assertEquals(str, "DEL_SUCCESS");
    }
    
    @Test
    public void testGetSymbols(){
    	prepareClient("junit", "");
    	WebTarget target=client.target(WebURL+"/rest").path("/stock/");
		Response res=target.request(MediaType.APPLICATION_JSON).get();
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			//something wrong, it might be no connections to SQL server
			fail("Bad response from server");
		}
		List<Stock> stocks = res.readEntity(new GenericType<List<Stock>>(){});
		assertTrue(stocks!=null && stocks.size()==1 && stocks.get(0).symbol.equals("junit"));
    }
    
    @Test
    public void testGetStockHistory(){
    	prepareClient("junit", "");
    	String symbol="junit";
    	WebTarget target=client.target(WebURL+"/rest").path("/stock/"+symbol);
		Response res=target.request(MediaType.APPLICATION_JSON).get();
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			fail("Bad response from server");
		}
		List<Stock> stocks = res.readEntity(new GenericType<List<Stock>>(){});
		assertTrue(stocks!=null && stocks.size()==1 && stocks.get(0).symbol.equals(symbol));
    }
    @BeforeClass
    public static void readConfig(){
    	String configFile=System.getProperty("configFile");
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
    		WebURL=prop.getProperty("weburl");
    		if (WebURL.endsWith("/")){
    			WebURL=WebURL.substring(0, WebURL.length()-1);
    		}
    		System.out.println("Will tesk rest api at url="+WebURL+"/rest"+"/stock/");
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
