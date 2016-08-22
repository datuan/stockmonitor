package com.example.stockmonitor.dataaccess;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.*;

import com.example.stockmonitor.daemon.YahooQuery;
import com.example.stockmonitor.data.JSonParser;
import com.example.stockmonitor.data.Stock;
/**
 * Utility class to access data stored in a SQL server, implements the {@link DataAccess} interface
 * @author tuandao
 *
 */
public class SQLDataAccess implements DataAccess{
	DataSource dataSource;
	Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	@Override
	public void addCompany(String userID, String companyID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="insert into follow(user_id,stock_symbol) values(?,?)";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, userID);
			stmt.setString(2, companyID);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server, msg="+e.getMessage());
		}
	}

	@Override
	public void addStockItem(Stock stock) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="insert into stocks (stock_symbol, last_query, price, ask, bid, lastTrade) values (?,?,?,?,?)";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, stock.symbol);
			stmt.setLong(2, stock.queryTime);
			stmt.setDouble(3, stock.lastTradePrice);
			stmt.setDouble(4, stock.ask);
			stmt.setDouble(5, stock.bid);
			stmt.setString(6, stock.lastTradeTime);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server, msg="+e.getMessage());
		}
	}

	@Override
	public void deleteCompany(String userID, String companyID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="delete from follow where user_id=? and stock_symbol=?";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, userID);
			stmt.setString(2, companyID);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server, msg="+e.getMessage());
		}
	}

	@Override
	public List<Stock> companyHistory(String companyID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="select (last_query, price, ask, bid, lastTrade) from stocks where stock_symbol=? order by last_query desc";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, companyID);
			ResultSet rs=stmt.executeQuery();
			List<Stock> stocks=new ArrayList<>();
			while (rs.next()){
				Stock stock=new Stock();
				stock.queryTime=rs.getLong(1);
				stock.lastTradePrice=rs.getDouble(2);
				stock.ask=rs.getDouble(3);
				stock.bid=rs.getDouble(4);
				stock.lastTradeTime=rs.getString(5);
				stocks.add(stock);
			}
			stmt.close();
			conn.close();
			return stocks;
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server, msg="+e.getMessage());
		}
	}

	@Override
	public List<Stock> listCompany(String userID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="select stock_symbol from follow where user_id=?";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, userID);
			List<String> symbolList=new ArrayList<>();
			ResultSet rs=stmt.executeQuery();
			while (rs.next()){
				symbolList.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			conn.close();
			//get update stock via Yahoo web service as required in the description
			String[] symbols=new String[symbolList.size()];
			symbols=symbolList.toArray(symbols);
			YahooQuery stockDaemon=new YahooQuery();
			String data=stockDaemon.getCurrentPrices(symbols);
			return JSonParser.parse(data);
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server, msg="+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error(s) while contacting web service for updated prices, msg="+e.getMessage());
		}
	}

}
