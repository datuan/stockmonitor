package com.example.stockmonitor.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	public static long convertUTCtoEpoch(String currentUTC){
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
	public static String epochToUTC(long epoch){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		return format.format(new Date(epoch));
	}
	public static String UTCToSimpleFormat(String utcTime){
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		SimpleDateFormat simpleFormat=new SimpleDateFormat("MM dd, yyyy HH:mm:ss",Locale.US);
		try{
			return simpleFormat.format(utcFormat.parse(utcTime));
		}
		catch(Exception e){
			return null;
		}
	}
	public static String epochToSimpleFormat(long epoch){
		SimpleDateFormat simpleFormat=new SimpleDateFormat("MM dd, yyyy HH:mm:ss",Locale.US);
		try{
			return simpleFormat.format(new Date(epoch));
		}
		catch(Exception e){
			return null;
		}
	}
}
