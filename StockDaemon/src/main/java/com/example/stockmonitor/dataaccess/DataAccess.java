package com.example.stockmonitor.dataaccess;
import java.util.List;

import com.example.stockmonitor.data.*;
/**
 * This interface describes data operators, data source can be XML file, SQL server, etc.
 * @author tuandao
 *
 */
public interface DataAccess{
	/**
	 * Add a company to the user's follow list
	 * @param userID userID
	 * @param companyID company's stock symbol
	 * @throws DataAccessException
	 */
	void addSymbol(String userID, String companyID) throws DataAccessException;
	/**
	 * Add a new stock prize
	 * @param stock struct that contains stock information
	 * @throws DataAccessException
	 */
	void addStockItem(Stock stock) throws DataAccessException;
	/**
	 * Delete a company (stock symbol) from a user's follow list
	 * @param userID userID
	 * @param companyID company's stock symbol
	 * @throws DataAccessException
	 */
	void deleteCompany(String userID, String companyID) throws DataAccessException;
	/**
	 * Get stock information of a company
	 * @param companyID company's stock symbol
	 * @return List of all stored stock prices of the company
	 * @throws DataAccessException
	 */
	List<Stock> companyHistory(String companyID) throws DataAccessException;
	/**
	 * List of all company stock symbols a user followed
	 * @param userID userID
	 * @return list of all companies and associated latest stock prices that a user followed
	 * @throws DataAccessException
	 */
	List<Stock> listCompany(String userID) throws DataAccessException;
}