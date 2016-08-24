package com.example.restapi;
import java.io.IOException;
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
		DataAccess da=new SQLDataAccess(pool);
		String userName=(String) req.getAttribute("username");
		try{
			da.deleteCompany(userName, symbol);
			return Response.ok().build();
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
		//Query google to see if this is a valid symbol
		StockQuery query=new GoogleStockQuery();
		String[] symbols=new String[]{symbol};
		try{
			List<Stock> stocks=query.getStockPrices(symbols);
			if (stocks.size()<1){
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Stock symbol does not exist").build();
			}
		}
		catch(IOException e){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Cannot verify stock symbol at this time").build();
		}
		
		DataAccess da=new SQLDataAccess(pool);
		String userName=(String) req.getAttribute("username");
		try{
			da.addSymbol(userName, symbol);
			return Response.ok().build();
//			return Response.ok("1", MediaType.TEXT_PLAIN).build();
		}
		catch(DataAccessException e){
			e.printStackTrace();
			//send back error message
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
	}
}
