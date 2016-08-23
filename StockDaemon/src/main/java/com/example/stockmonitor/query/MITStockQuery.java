package com.example.stockmonitor.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import com.example.stockmonitor.data.Stock;

public class MITStockQuery implements StockQuery{
	Client client=ClientBuilder.newClient();
	String url="http://dev.markitondemand.com/MODApis/Api/v2/Quote/";
	String path="MODApis/Api/v2/Quote";
	@Override
	public List<Stock> getStockPrices(String[] symbols) throws IOException {
		// TODO Auto-generated method stub
		List<Stock> stocks=new ArrayList<>();
		for (String symbol:symbols){
			Stock s=getStockPrice(symbol);
			if (s!=null) stocks.add(s);
			try {
				//Because of the rate limit
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stocks;
	}
	
	public Stock getStockPrice(String symbol) throws IOException{
		//http://dev.markitondemand.com/MODApis/Api/v2/Quote?symbol=GOOG
		WebTarget target=client.target(url).path("path").queryParam("symbol", symbol);
		Response res=target.request(MediaType.TEXT_XML).get();
		long queryTime=System.currentTimeMillis();
		if (res.getStatusInfo().getFamily()==Family.SUCCESSFUL){
			String input=res.readEntity(String.class);
			if (input.indexOf("<Status>SUCCESS</Status>")==-1){
				return null;
			}
			int start=input.indexOf("<LastPrice>");
			int end=input.indexOf("</LastPrice>");
			int length=new String("<LastPrice>").length();
			if (start==-1 || end==-1) return null;
			double price=Double.parseDouble(input.substring(start+length, end));
			start=input.indexOf("<Timestamp>");
			end=input.indexOf("</Timestamp>");
			length=new String("<Timestamp>").length();
			if (start==-1 || end==-1) return null;
			String tradeDate=input.substring(start+length, end);
			Stock stock=new Stock(symbol,price,tradeDate,queryTime);
			return stock;
		}
		else
			return null;
	}
	/* Example response:
	 * <StockQuote>
			<Status>SUCCESS</Status>
			<Name>Alphabet Inc</Name>
			<Symbol>GOOGL</Symbol>
			<LastPrice>798.52</LastPrice>
			<Change>1.57</Change>
			<ChangePercent>0.1970010666</ChangePercent>
			<Timestamp>Tue Aug 23 14:19:46 UTC-04:00 2016</Timestamp>
			<MSDate>42605.5970601852</MSDate>
			<MarketCap>274427368400</MarketCap>
			<Volume>40344</Volume>
			<ChangeYTD>778.01</ChangeYTD>
			<ChangePercentYTD>2.6362129021</ChangePercentYTD>
			<High>800.895</High>
			<Low>797.86</Low>
			<Open>800.53</Open>
		</StockQuote>
	 */
}
