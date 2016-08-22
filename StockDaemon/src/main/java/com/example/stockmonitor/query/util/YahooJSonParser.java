package com.example.stockmonitor.query.util;
import org.json.*;

import com.example.stockmonitor.data.Stock;

import java.util.*;
import java.text.SimpleDateFormat;
/**
  * @author: Tuan Dao
  * This class is used to parse a json string into a list of Stock objects using the org.json library
  */
public class YahooJSonParser implements JsonParser{
	/**
	  * Parse a json string into list of Stock objects
	  * @param input json string
	  * @return a list of Stock objects
	  */
	public List<Stock> parseString(String input) throws JSONException{
		JSONObject json=new JSONObject(input).getJSONObject("query");
		String currentUTC=json.getString("created");
		long epoch=convertUTCtoEpoch(currentUTC);
		JSONArray results=json.getJSONObject("results").getJSONArray("quote");
		List<Stock> list=new ArrayList<>();
		Iterator<Object> iter=results.iterator();
		while (iter.hasNext()){
			JSONObject obj=(JSONObject)iter.next();
			String name=obj.getString("Name");
			if (name==null){
				//incorrect symbol
				continue;
			}
			String symbol=obj.getString("symbol");
//			double ask=obj.getDouble("Ask");
//			double bid=obj.getDouble("Bid");
			double lastTradePrice=obj.getDouble("LastTradePriceOnly");
			String lastTradeTime=obj.getString("LastTradeTime");
			Stock stock=new Stock(symbol, lastTradePrice, lastTradeTime, epoch);
			list.add(stock);
		}
		return list;
	}
	
}
