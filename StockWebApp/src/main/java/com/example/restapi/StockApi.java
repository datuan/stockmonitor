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
	private static SQLDataPool pool=new TomcatMySQLDataPool();
	
	@GET
	@Produces("application/json")
	public List<Stock> getAllCompanies(){
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
	
	@GET @Path("/{symbol}")
	@Produces("application/json")
	public List<Stock> getCompany(@PathParam("symbol") String symbol){
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
	
	@DELETE @Path("/{symbol}")
	public String deleteCompary(@PathParam("symbol") String symbol){
		DataAccess da=new SQLDataAccess(pool);
		try{
			da.deleteCompany("user1", symbol);
			return "Symbol: "+symbol+" deleted";
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return "Error, msg="+e.getMessage();
		}
	}
	
	@POST @Path("/{symbol}")
	public String addCompany(@PathParam("symbol") String symbol){
		//TODO: add a new company (symbol) to MySQL
		DataAccess da=new SQLDataAccess(pool);
		try{
			da.addSymbol("user1", symbol);
			return "Symbol: "+symbol+" added";
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return "Error, msg="+e.getMessage();
		}
	}
}
