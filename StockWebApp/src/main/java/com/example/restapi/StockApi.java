package com.example.restapi;
import java.util.List;

import javax.ws.rs.*;
import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.dataaccess.util.TomcatMySQLDataPool;

@Path("/stock")
public class StockApi {
	static final int SUCCESS = 200;
	static final int FAIL = 400;
	
	private static SQLDataPool pool=new TomcatMySQLDataPool();
	
	/**
	 * Get all stock symbols a user follows
	 * @return a list of stock symbols, refer to {@link Stock}, null if errors occurred
	 */
	@GET
	@Produces("application/json")
	public List<Stock> getAllStockSymbols(){
		DataAccess da=new SQLDataAccess(pool);
		try{
			List<Stock> stocks=da.listCompany("user1");
			return stocks;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Get all stock prices of a specific symbol
	 * @param symbol stock symbol code, e.g., GOOG for Google
	 * @return all the stock prices stored in the database, refer to {@link Stock}, null if errors occurred
	 */
	@GET @Path("/{symbol}")
	@Produces("application/json")
	public List<Stock> getStockSymbol(@PathParam("symbol") String symbol){
		DataAccess da=new SQLDataAccess(pool);
		try{
			List<Stock> stocks=da.companyHistory(symbol);
			return stocks;
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Delete (unfollow) a stock symbol
	 * @param symbol symbol code, e.g., GOOG for Google
	 * @return a string value, "200" if success, "400 - {Message}" if errors occurred
	 */
	@DELETE @Path("/{symbol}")
	public String deleteStockSymbol(@PathParam("symbol") String symbol){
		DataAccess da=new SQLDataAccess(pool);
		try{
			da.deleteCompany("user1", symbol);
			return String.valueOf(SUCCESS);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return FAIL+" - Error, msg="+e.getMessage();
		}
	}
	/**
	 * Add (follow) a stock symbol
	 * @param symbol symbol code, e.g., GOOG for Google
	 * @return a string value, "200" if success, "400 - {Message}" if errors occurred 
	 */
	@POST @Path("/{symbol}")
	public String addCompany(@PathParam("symbol") String symbol){
		//TODO: add a new company (symbol) to MySQL
		DataAccess da=new SQLDataAccess(pool);
		try{
			da.addSymbol("user1", symbol);
			return String.valueOf(SUCCESS);
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return FAIL + " - Error, msg="+e.getMessage();
		}
	}
}
