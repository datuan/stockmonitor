package com.example.restapi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.dataaccess.util.TomcatMySQLDataPool;
import com.example.stockmonitor.query.GoogleStockQuery;
import com.example.stockmonitor.query.MITStockQuery;
import com.example.stockmonitor.query.StockQuery;

@Path("/stock")
public class StockApi {
	@Context HttpServletRequest req;
	private static SQLDataPool pool=new TomcatMySQLDataPool();
	
	/**
	 * Get all stock symbols a user follows
	 * @return a list of stock symbols, refer to {@link Stock}, null if errors occurred
	 */
	@GET
	@Produces("application/json")
	public Response getAllStockSymbols(){
		String userName=(String) req.getAttribute("username");
		if (userName.equals("junit")){
			//this is a junit test
			return RestAPIMockTest.getAllStock();
		}
		DataAccess da=new SQLDataAccess(pool);
		try{
			List<Stock> stocks=da.listCompany(userName);
			return Response.ok(stocks, MediaType.APPLICATION_JSON_TYPE).build();
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return Response.noContent().entity(e.getMessage()).build();
		}
	}
	/**
	 * Get all stock prices of a specific symbol
	 * @param symbol stock symbol code, e.g., GOOG for Google
	 * @return all the stock prices stored in the database, refer to {@link Stock}, null if errors occurred
	 */
	@GET @Path("/{symbol}")
	@Produces("application/json")
	public Response getStockSymbol(@PathParam("symbol") String symbol){
		String userName=(String) req.getAttribute("username");
		if (userName.equals("junit")){
			//this is a junit test
			return RestAPIMockTest.getStockSymbol(symbol);
		}
		
		DataAccess da=new SQLDataAccess(pool);
		try{
			List<Stock> stocks=da.companyHistory(symbol);
			return Response.ok(stocks, MediaType.APPLICATION_JSON_TYPE).build();
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return Response.noContent().entity(e.getMessage()).build();
		}
	}
	/**
	 * Delete (unfollow) a stock symbol
	 * @param symbol symbol code, e.g., GOOG for Google
	 * @return OK/ERROR HTTP status
	 */
	@DELETE @Path("/{symbol}")
	public Response deleteStockSymbol(@PathParam("symbol") String symbol){
		
		String userName=(String) req.getAttribute("username");
		if (userName.equals("junit")){
			//this is a junit test
			return RestAPIMockTest.delCompany(symbol);
		}
		
		DataAccess da=new SQLDataAccess(pool);
		try{
			int count=da.deleteCompany(userName, symbol);
			//let the client know the result
			//0: the item of interest might not be in the database
			//1: the item was in the database and gets deleted
			//how to deal with the information is left to the client
			return Response.ok(String.valueOf(count),MediaType.TEXT_PLAIN).build();
		}
		catch(DataAccessException e){
			e.printStackTrace();
			//send back error message
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
	}
	/**
	 * Add (follow) a stock symbol
	 * @param symbol symbol code, e.g., GOOG for Google
	 * @return OK/ERROR HTTP status
	 */
	@POST @Path("/{symbol}")
	public Response addCompany(@PathParam("symbol") String symbol){
		//TODO: add a new company (symbol) to MySQL
		
		String userName=(String) req.getAttribute("username");
		if (userName.equals("junit")){
			//this is a junit test
			return RestAPIMockTest.addCompany(symbol);
		}
		
		//Query google to see if this is a valid symbol
		StockQuery query=new GoogleStockQuery();
		String[] symbols=new String[]{"GOOG",symbol};
		try{
			//using Google to check validation
			//There is a trick here, for some stock, e.g. SSS (a valid one)
			//Google said it is not valid if it is query alone, however if we ask about the same stock with another one, e.g. GOOG
			//Google is able to return the correct result -> combine our query with GOOG
			List<Stock> stocks=query.getStockPrices(symbols);
			if (stocks.size()<2){
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Stock symbol does not exist").build();
			}
		}
		catch(Exception e){
			try{
				//TODO: this piece of code is not nice, rewrite if have time, make it into a loop
				//Google service fails, use MIT as a fail over
				query=new MITStockQuery();
				symbols=new String[]{symbol};
				List<Stock> stocks=query.getStockPrices(symbols);
				if (stocks.size()<1){
					return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Stock symbol does not exist").build();
				}
			}
			catch(Exception ex){
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Cannot verify stock symbol at this time. Please try again.").build();
			}
			
		}
		
		DataAccess da=new SQLDataAccess(pool);
		try{
			int count=da.addSymbol(userName, symbol);
			return Response.ok(String.valueOf(count),MediaType.TEXT_PLAIN).build();
		}
		catch(DataAccessException e){
			e.printStackTrace();
			//send back error message
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
	}
	/**
	 * Small utility class for mock testing the rest api
	 * Will be called if the username is 'junit'
	 * @author tuandao
	 *
	 */
	static class RestAPIMockTest{
		static Response addCompany(String symbol){
			return Response.ok("ADD_SUCCESS", MediaType.TEXT_PLAIN).build();
		}
		static Response delCompany(String symbol){
			return Response.ok("DEL_SUCCESS", MediaType.TEXT_PLAIN).build();
		}
		static Response getStockSymbol(String symbol){
			Stock s=new Stock(symbol, 0.0, "tradetime", 0);
			List<Stock> stocks=new ArrayList<>();
			stocks.add(s);
			return Response.ok(stocks, MediaType.APPLICATION_JSON_TYPE).build();
		}
		static Response getAllStock(){
			Stock s=new Stock("junit", 0.0, "tradetime", 0);
			List<Stock> stocks=new ArrayList<>();
			stocks.add(s);
			return Response.ok(stocks, MediaType.APPLICATION_JSON_TYPE).build();
		}
	}
}
