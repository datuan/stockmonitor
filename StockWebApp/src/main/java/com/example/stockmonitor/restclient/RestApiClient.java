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

public class RestApiClient {
	Client client=null;
	String restPath, userName, password;
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
			return null;//something wrong happens
		}
		return res.readEntity(new GenericType<List<Stock>>(){});
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
			return null;//something wrong happens
		}
		return res.readEntity(new GenericType<List<Stock>>(){});
	}
	/**
	 * 
	 * @param userID
	 * @param symbol
	 * @return
	 */
	public boolean addStock(String symbol){
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/"+symbol);
		Response res=target.request(MediaType.TEXT_PLAIN).post(Entity.text(symbol));
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			return false;//something wrong happens
		}
		return true;
	}
	
	public boolean delStock(String symbol){
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/"+symbol);
		Response res=target.request(MediaType.TEXT_PLAIN).delete();
		if (res.getStatusInfo().getFamily()!=Family.SUCCESSFUL){
			return false;//something wrong happens
		}
		return true;
	}
}
