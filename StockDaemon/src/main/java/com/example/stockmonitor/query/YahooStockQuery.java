package com.example.stockmonitor.query;
import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;

import com.example.stockmonitor.data.*;
import com.example.stockmonitor.query.util.JsonParser;
import com.example.stockmonitor.query.util.YahooJSonParser;
public class YahooStockQuery implements StockQuery{
	private final String yahoo_api="http://query.yahooapis.com/v1/public/yql?q=";
	private final String options="&diagnostics=true&env=http://datatables.org/alltables.env&format=json";
	JsonParser parser=new YahooJSonParser();
	@Override
	public List<Stock> getStockPrices(String[] symbols) throws IOException{
		//?q=select * from yahoo.finance.quotes where symbol in ("AMZN","AAPL","GOOG","MSFT")
		
		String query="select * from yahoo.finance.quotes where symbol in (";
		for (int i=0;i<symbols.length-1;i++){
			query+="\""+symbols[i]+"\",";	
		}
		query+="\""+symbols[symbols.length-1]+"\")";
		String url=yahoo_api+URLEncoder.encode(query,"utf-8")+options;
		HttpClient client=HttpClientBuilder.create().build();
		HttpGet request=new HttpGet(url);
		HttpResponse response = client.execute(request);
		BufferedReader rd=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder sb=new StringBuilder();
		String line;
		while ((line=rd.readLine())!=null){
			sb.append(line);
			sb.append("\n");
		}
		rd.close();
		try {
			List<Stock> stocks= parser.parseString(sb.toString());
			return stocks;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
