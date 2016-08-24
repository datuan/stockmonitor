package com.example.stockmonitor.restclient;


import java.util.List;

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

import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccessException;
/**
 * Client class to connect to the Stock Rest API
 * @author tuandao
 *
 */
public class RestApiClient {
	Client client=null;
	String restPath, userName, password;
	/**
	 * 
	 * @param restPath absolute url to the rest api
	 * @param userName username for basic http authentication
	 * @param password password for basic http authentication
	 */
	public RestApiClient(String restPath,String userName, String password) {
		// TODO Auto-generated constructor stub
		this.restPath=restPath;
		this.userName=userName;
		this.password=password;
	}
	private void prepareClient(){
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
			    .credentials(userName, password)
			    .build();

			ClientConfig clientConfig = new ClientConfig();
			clientConfig.register(feature) ;
			client = ClientBuilder.newClient(clientConfig);
	}
	/**
	 * Get all stock symbols a user follows
	 * @param userID user name
	 * @return list of Stock symbols and newest prices, refer to {@link Stock}
	 */
	public List<Stock> getAllSymbols(String userID){
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/");
		Response res=target.request(MediaType.APPLICATION_JSON).get();
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			//something wrong, it might be no connections to SQL server
			return null;
		}
		List<Stock> stocks = res.readEntity(new GenericType<List<Stock>>(){});
		return stocks;
	}
	/**
	 * Get stock prices of a specific symbols
	 * @param symbol symbol ID, e.g., GOOG for Google
	 * @return list of Stock prices, refer to {@link Stock}
	 */
	public List<Stock> getSymbolHistory(String symbol){
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/"+symbol);
		Response res=target.request(MediaType.APPLICATION_JSON).get();
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			//something wrong, it might be no connections to SQL server
			return null;
		}
		return res.readEntity(new GenericType<List<Stock>>(){});
	}
	/**
	 * Add a stock symbol
	 * @param symbol
	 * @throws DataAccessException if errors happen
	 */
	public void addStock(String symbol) throws DataAccessException{
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/"+symbol);
		Response res=target.request(MediaType.TEXT_PLAIN).post(Entity.text(symbol));
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			//get message from server
			String message=res.readEntity(String.class);
//			return false;//something wrong happens
			throw new DataAccessException(message);
		}
		return;//success
	}
	
	public void delStock(String symbol) throws DataAccessException{
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/"+symbol);
		Response res=target.request(MediaType.TEXT_PLAIN).delete();
		String message=res.readEntity(String.class);
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			
			throw new DataAccessException(message);
		}
		int count=Integer.parseInt(message);
		if (count==0){
			//no item is deleted, even though the process is successful
			throw new DataAccessException("The user did not follow symbol "+symbol+", no entries deleted");
		}
		return;
	}
}
