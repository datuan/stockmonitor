package com.example.stockmonitor.query;

import java.io.IOException;
import java.util.List;

import com.example.stockmonitor.data.Stock;

public interface StockQuery {
	public List<Stock> getStockPrices(String[] symbols) throws IOException;
}
