package com.example.stockmonitor.query.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;

import com.example.stockmonitor.data.Stock;

public interface JsonParser {
	public List<Stock> parseString(String input) throws JSONException;
	/**
	 * Convert a string stored as yyyy-HH-ddTHH:mm:ssZ format returned by Yahoo into an epoch time
	 * @param currentUTC UTC time string
	 * @return epoch time
	 */
	default long convertUTCtoEpoch(String currentUTC){
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
