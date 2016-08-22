package com.example.stockmonitor;
import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
public class Daemon{
	private final String yahoo_api="http://query.yahooapis.com/v1/public/yql?q=";
	// private final String options="&diagnostics=true&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json";
	private final String options="&diagnostics=true&env=http://datatables.org/alltables.env&format=json";
	
	public static void main(String[] args){
		try{
			String query="select * from yahoo.finance.quotes " +
				"where symbol in (\"AMZN\",\"AAPL\",\"GOOG\",\"MSFT\")";
			Daemon d=new Daemon();
			String str=d.getRequest(query);
			// System.out.println(str);
			List<Stock> stocks=JSonParser.parse(str);
			Iterator<Stock> iter=stocks.iterator();
			while (iter.hasNext()){
				Stock stock=iter.next();
				System.out.println(stock.toString());
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public String getRequest(String query) throws IOException{
		//?q=select * from yahoo.finance.quotes where symbol in ("AMZN","AAPL","GOOG","MSFT")
		String url=yahoo_api+URLEncoder.encode(query)+options;
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
		return sb.toString();
	}
}
