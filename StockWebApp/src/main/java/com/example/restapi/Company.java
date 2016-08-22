package com.example.restapi;
import javax.naming.InitialContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.example.stockmonitor.Stock;

@Path("/company")
public class Company {
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
			ic.lookup("abc");
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
		return "Add symbol: "+symbol;
	}
}
