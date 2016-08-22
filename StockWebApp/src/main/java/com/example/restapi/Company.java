package com.example.restapi;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.dataaccess.util.TomcatMySQLDataPool;

@Path("/company")
public class Company {
	SQLDataPool pool=new TomcatMySQLDataPool();
	String symbol;
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String getCompany(@QueryParam("symbol") String symbol){
//		if (symbol==null){//symbol.length()==0){
//			return "All companies";
//		}
//		return "Query company = "+symbol;
//	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCompanies(){
		try{
			InitialContext ic=new InitialContext();
			DataSource ds=(DataSource)ic.lookup("java:comp/env/jdbc/StockDB");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "All companies";
	}
	
	@GET @Path("/{symbol}")
	@Produces("application/json")
	public Stock getCompany(@PathParam("symbol") String symbol){
		Stock stock=new Stock("abc", symbol, 10.0, 11.0, 15.2, "now",14325435);
		//public Stock(String name, String symbol, double lastTradePrice, double ask, double bid, String lastTradeTime, long queryTime){
		return stock;
	}
	
	@DELETE @Path("/{symbol}")
	public String deleteCompary(@PathParam("symbol") String symbol){
		return "Delete symbol: "+symbol;
	}
	
	@POST @Path("/{symbol}")
	public String addCompany(@PathParam("symbol") String symbol){
		//TODO: add a new company (symbol) to MySQL
		DataAccess da=new SQLDataAccess(pool);
		try{
			da.addCompany("user1", symbol);
			return "Symbol: "+symbol+" added";
		}
		catch(DataAccessException e){
			e.printStackTrace();
			return "Error, msg="+e.getMessage();
		}
	}
}
