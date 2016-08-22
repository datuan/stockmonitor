package com.example.stockmonitor;
public class Stock{
	String symbol;
	double lastTradePrice;
	double ask;
	double bid;
	String lastTradeTime;
	long queryTime;//epoch time
	String name;
	public Stock(){
	}
	public Stock(String name, String symbol, double lastTradePrice, double ask, double bid, String lastTradeTime, long queryTime){
		this.symbol=symbol;
		this.lastTradePrice=lastTradePrice;
		this.ask=ask;
		this.bid=bid;
		this.queryTime=queryTime;
		this.lastTradeTime=lastTradeTime;
		this.name=name;
	}
	public String toString(){
		return "[Name:"+name+", symbol:"+symbol+", lastTradeTime:"+lastTradeTime+", queryTime:" + queryTime+", ask:"+ask+", bid:"+bid+", lastTradePrice:"+lastTradePrice+"]";
	}
}
