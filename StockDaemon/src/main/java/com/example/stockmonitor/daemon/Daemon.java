package com.example.stockmonitor.daemon;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.example.stockmonitor.data.JSonParser;
import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;

public class Daemon {

	public static void main(String[] args){
		try{
			String[] symbols=new String[]{"AMZN","AAPL","GOOG","MSFT"};
			YahooQuery d=new YahooQuery();
			String str=d.getCurrentPrices(symbols);
			DataAccess da=new SQLDataAccess();
			List<Stock> stocks=JSonParser.parse(str);
			Iterator<Stock> iter=stocks.iterator();
			while (iter.hasNext()){
				Stock stock=iter.next();
				System.out.println(stock.toString());
				try{
					da.addStockItem(stock);
					System.out.println("Data added");
				}
				catch (DataAccessException e){
					System.out.println("Error accessing data:"+e.getMessage());
				}
				
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}
