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
}
