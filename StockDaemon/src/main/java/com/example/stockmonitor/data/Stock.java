package com.example.stockmonitor.data;
/**
 * Represent the stock information of a stock symbol
 * @author tuandao
 *
 */
public class Stock{
	public String symbol="";
	public double lastTradePrice;
//	public double ask;
//	public double bid;
	public String lastTradeTime;
	public long queryTime;//epoch time
	public String name;
	public Stock(){
	}
//	public Stock(String name, String symbol, double lastTradePrice, double ask, double bid, String lastTradeTime, long queryTime){
//		this.symbol=symbol;
//		this.lastTradePrice=lastTradePrice;
//		this.ask=ask;
//		this.bid=bid;
//		this.queryTime=queryTime;
//		this.lastTradeTime=lastTradeTime;
//		this.name=name;
//	}
	public Stock(String symbol, double lastTradePrice, String lastTradeTime, long queryTime){
		this.symbol=symbol;
		this.lastTradePrice=lastTradePrice;
		this.lastTradeTime=lastTradeTime;
		this.queryTime=queryTime;
	}
	/**
	 * Debug function
	 */
	public String toString(){
		return "[Symbol:"+symbol+", lastTradeTime:"+lastTradeTime+", queryTime:" + queryTime+", lastTradePrice:"+lastTradePrice+"]";
	}
}
