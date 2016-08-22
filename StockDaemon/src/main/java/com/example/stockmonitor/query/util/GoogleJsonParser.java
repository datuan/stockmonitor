package com.example.stockmonitor.query.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stockmonitor.data.Stock;

public class GoogleJsonParser implements JsonParser {

	@Override
	public List<Stock> parseString(String input) throws JSONException {
		// TODO Auto-generated method stub
		JSONArray arr=new JSONArray(input);
		List<Stock> list=new ArrayList<>();
		Iterator<Object> iter=arr.iterator();
		while (iter.hasNext()){
			JSONObject obj=(JSONObject)iter.next();
			String symbol=obj.getString("t");
			double lastTradePrice=obj.getDouble("l");
			String lastTradeTime=obj.getString("lt_dts");
			long epoch=System.currentTimeMillis();
			Stock stock=new Stock(symbol, lastTradePrice, lastTradeTime, epoch);
			list.add(stock);
		}
		return list;
	}

}

/*
 * // [ { "id": "304466804484872" ,"t" : "GOOG" ,"e" : "NASDAQ" ,"l" : "772.15" ,"l_fix" : "772.15" ,"l_cur" : "772.15" ,"s": "2" ,
 * "ltt":"4:00PM EDT" ,"lt" : "Aug 22, 4:00PM EDT" ,"lt_dts" : "2016-08-22T16:00:01Z" ,"c" : "-3.27" ,"c_fix" : "-3.27" ,"cp" : "-0.42" ,"cp_fix" : "-0.42" ,
 * "ccol" : "chr" ,"pcls_fix" : "775.42" ,"el": "772.15" ,"el_fix": "772.15" ,"el_cur": "772.15" ,"elt" : "Aug 22, 4:48PM EDT" ,"ec" : "0.00" ,"ec_fix" : "0.00" ,"ecp" : "0.00" ,"ecp_fix" : "0.00" ,"eccol" : "chb" ,"div" : "" ,"yld" : "" } ,{ "id": "22144" ,"t" : "AAPL" ,"e" : "NASDAQ" ,"l" : "108.51" ,"l_fix" : "108.51" ,"l_cur" : "108.51" ,"s": "2" ,"ltt":"4:00PM EDT" ,"lt" : "Aug 22, 4:00PM EDT" ,"lt_dts" : "2016-08-22T16:00:02Z" ,"c" : "-0.85" ,"c_fix" : "-0.85" ,"cp" : "-0.78" ,"cp_fix" : "-0.78" ,"ccol" : "chr" ,"pcls_fix" : "109.36" ,"el": "108.50" ,"el_fix": "108.50" ,"el_cur": "108.50" ,"elt" : "Aug 22, 4:56PM EDT" ,
 * "ec" : "-0.01" ,"ec_fix" : "-0.01" ,"ecp" : "-0.01" ,"ecp_fix" : "-0.01" ,
 * "eccol" : "chr" ,"div" : "0.57" ,"yld" : "2.10" } ]*/
