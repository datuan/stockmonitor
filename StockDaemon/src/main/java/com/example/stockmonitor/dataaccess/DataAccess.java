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
	 * @return number of data entries affected, should be 1
	 * @throws DataAccessException
	 */
	int addSymbol(String userID, String companyID) throws DataAccessException;
	/**
	 * Add a new stock price
	 * @param stock struct that contains stock information
	 * @return number of data entries affected, should be 1
	 * @throws DataAccessException
	 */
	int addStockItem(Stock stock) throws DataAccessException;
	/**
	 * Delete a company (stock symbol) from a user's follow list
	 * @param userID userID
	 * @param companyID company's stock symbol
	 * @return number of data entries affected, should be 1
	 * @throws DataAccessException
	 */
	int deleteCompany(String userID, String companyID) throws DataAccessException;
	/**
	 * Get stock information of a company
	 * @param companyID company's stock symbol
	 * @return List of all stored stock prices of the company
	 * @throws DataAccessException
	 */
	List<Stock> companyHistory(String companyID) throws DataAccessException;
	/**
	 * Lists all newest/updated prices for the symbols a user followed
	 * @param userID userID
	 * @return list of all newest stock prices that a user followed
	 * @throws DataAccessException
	 */
	List<Stock> listCompany(String userID) throws DataAccessException;
	/**
	 * Get a list of all
	 * @param userID userID
	 * @return list of all followed symbols
	 * @throws DataAccessException
	 */
	List<String> followedSymbol(String userID) throws DataAccessException;
	/**
	 * Get all stock symbols in the database, used for the daemon process
	 * @return all stock symbols
	 * @throws DataAccessException
	 */
	List<String> getAllSymbols() throws DataAccessException;
}