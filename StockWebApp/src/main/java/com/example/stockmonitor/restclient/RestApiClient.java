package com.example.stockmonitor.restclient;


import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

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
			    .nonPreemptive()
			    .credentials(userName, password)
			    .build();

			ClientConfig clientConfig = new ClientConfig();
			clientConfig.register(feature) ;
			client = ClientBuilder.newClient(clientConfig);
	}
	public List<Stock> getAllSymbols(String userID){
		prepareClient();
		WebTarget target=client.target(restPath).path("/stock/");
		target.queryParam("username", "user1");
		List<Stock> stocks=target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Stock>>(){});
		return stocks;
	}
}
