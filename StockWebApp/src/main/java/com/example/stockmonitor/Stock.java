package com.example.stockmonitor;
public class Stock{
	public String symbol;
	public double lastTradePrice;
	public double ask;
	public double bid;
	public String lastTradeTime;
	public long queryTime;//epoch time
	public String name;
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
