package com.example.stockmonitor.data;
import org.json.*;
import java.util.*;
import java.text.SimpleDateFormat;
/**
  * @author: Tuan Dao
  * This class is used to parse a json string into a list of Stock objects using the org.json library
  */
public class JSonParser{
	/**
	  * Parse a json string into list of Stock objects
	  * @param input json string
	  * @return a list of Stock objects
	  */
	public static List<Stock> parse(String input){
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
			double ask=obj.getDouble("Ask");
			double bid=obj.getDouble("Bid");
			double lastTradePrice=obj.getDouble("LastTradePriceOnly");
			String lastTradeTime=obj.getString("LastTradeTime");
			Stock stock=new Stock(name,symbol, lastTradePrice, ask, bid, lastTradeTime,epoch);
			list.add(stock);
		}
		return list;
	}
	/**
	 * Convert a string stored as yyyy-HH-ddTHH:mm:ssZ format returned by Yahoo into an epoch time
	 * @param currentUTC UTC time string
	 * @return epoch time
	 */
	private static long convertUTCtoEpoch(String currentUTC){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		try{
			Date date = format.parse(currentUTC);
			return date.getTime();
		}
		catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
}
