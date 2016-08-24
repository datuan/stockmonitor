package com.example.stockmonitor.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;

import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.query.util.GoogleJsonParser;
import com.example.stockmonitor.query.util.JsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoogleStockQuery implements StockQuery {
	private static final Logger logger = LogManager.getLogger("GoogleStockQuery");
	JsonParser parser=new GoogleJsonParser();
	final static String query="http://finance.google.com/finance/info?client=ig&q=NSE:";
	@Override
	public List<Stock> getStockPrices(String[] symbols) throws IOException {
		// TODO Auto-generated method stub
		StringBuilder data=new StringBuilder();
		for (String symbol : symbols){
			data.append(symbol+",");
		}
		data.deleteCharAt(data.length()-1);
		String url=query+data;
		logger.debug("Query stock from Google Finance");
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
		int start=sb.indexOf("[");
		
		try{
			List<Stock> stocks=parser.parseString(sb.substring(start));
			return stocks;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			logger.error("str="+sb.toString()+", start="+start);
			return null;
		}
	}

}
