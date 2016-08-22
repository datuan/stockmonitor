package com.example.stockmonitor.daemon;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;
import com.example.stockmonitor.dataaccess.util.DBCPMySQLDataPool;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.query.GoogleStockQuery;
import com.example.stockmonitor.query.StockQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Daemon extends TimerTask {

	public static void main(String[] args){
		final Logger logger = LogManager.getLogger("StockDaemon");
		try{
			String[] symbols=new String[]{"AMZN","AAPL","GOOG","MSFT"};
			StockQuery d=new GoogleStockQuery();
			List<Stock> stocks=d.getStockPrices(symbols);
			SQLDataPool pool=new DBCPMySQLDataPool();
			DataAccess da=new SQLDataAccess(pool);
			Iterator<Stock> iter=stocks.iterator();
			while (iter.hasNext()){
				Stock stock=iter.next();
				try{
					da.addStockItem(stock);
					logger.info("Stock: "+stock.toString()+" added to MySQL");
				}
				catch (DataAccessException e){
					logger.error("Error accessing data:"+e.getMessage());
				}
				
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
