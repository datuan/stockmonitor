package com.example.stockmonitor.daemon;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;
import com.example.stockmonitor.dataaccess.util.DBCPMySQLDataPool;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.query.GoogleStockQuery;
import com.example.stockmonitor.query.MITStockQuery;
import com.example.stockmonitor.query.StockQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Daemon extends TimerTask {
	private static final Logger logger = LogManager.getLogger("StockDaemon");
	private static Timer timer;
	private static int DELAY=20*1000;
	
	private static SQLDataPool pool=new DBCPMySQLDataPool();
	private DataAccess da=new SQLDataAccess(pool);
	
	public static void main(String[] args){
		timer=new Timer();
		timer.schedule(new Daemon(), 0, DELAY);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			List<String> symList=da.getAllSymbols();
			String[] symbols=new String[symList.size()];
			symbols=symList.toArray(symbols);
			
			StockQuery d=new GoogleStockQuery();
			List<Stock> stocks=d.getStockPrices(symbols);
			if (stocks==null){
				logger.error("Google fails, use MIT service");
				d=new MITStockQuery();//use MIT service
				stocks=d.getStockPrices(symbols);
				if (stocks==null) return;//still fail, do nothing
			}
			Iterator<Stock> iter=stocks.iterator();
			while (iter.hasNext()){
				Stock stock=iter.next();
				//TODO: enable again
				da.addStockItem(stock);
				logger.error("Stock: "+stock.toString()+" added to MySQL");
			}
		}
		catch(DataAccessException e){
			logger.error("Error(s) while contacting MySQL server, msg="+e.getMessage());
		}
		catch(IOException e){
			logger.error("Error(s) while updating stock prices, msg="+e.getMessage());
		}
	}
}
