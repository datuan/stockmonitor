package com.example.stockmonitor.dataaccess;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.*;

import com.example.stockmonitor.data.Stock;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.query.GoogleStockQuery;
import com.example.stockmonitor.query.StockQuery;
/**
 * Utility class to access data stored in a SQL server, implements the {@link DataAccess} interface
 * @author tuandao
 *
 */
public class SQLDataAccess implements DataAccess{
	private SQLDataPool pool=null;
	private DataSource dataSource;
	
	StockQuery sQuery=new GoogleStockQuery();
	
	public SQLDataAccess(SQLDataPool pool){
		this.pool=pool;
	}
	Connection getConnection() throws SQLException{
//		SQLDataPool dataPool=new TomcatMySQLDataPool();
		dataSource=pool.getDataSource();
		return dataSource.getConnection();
	}
	@Override
	public void addSymbol(String userID, String companyID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="insert ignore into stock_symbol(symbol) values(?)";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, companyID);
			stmt.executeUpdate();
			stmt.close();
			sql="insert into follow(userid,symbol) values(?,?)";
			stmt=conn.prepareStatement(sql);
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
			String sql="insert into stocks (symbol, last_query, price, lastTrade) values (?,?,?,?)";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, stock.symbol);
			stmt.setLong(2, stock.queryTime);
			stmt.setDouble(3, stock.lastTradePrice);
			stmt.setString(4, stock.lastTradeTime);
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
			String sql="delete from follow where userid=? and symbol=?";
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
	public List<Stock> companyHistory(String symbol) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="select last_query, price, lastTrade from stocks where symbol=? order by last_query desc";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, symbol);
			ResultSet rs=stmt.executeQuery();
			List<Stock> stocks=new ArrayList<>();
			while (rs.next()){
				Stock stock=new Stock();
				stock.symbol=symbol;
				stock.queryTime=rs.getLong(1);
				stock.lastTradePrice=rs.getDouble(2);
				stock.lastTradeTime=rs.getString(3);
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
			String sql="select symbol from follow where userid=?";
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
			//get update stock via Google web service as required in the description
			String[] symbols=new String[symbolList.size()];
			symbols=symbolList.toArray(symbols);
			return sQuery.getStockPrices(symbols);
			//TODO: check for error
//			YahooStockQuery stockDaemon=new YahooStockQuery();
//			String data=stockDaemon.getCurrentPrices(symbols);
//			return YahooJSonParser.parse(data);
			
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server, msg="+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error(s) while contacting web service for updated prices, msg="+e.getMessage());
		}
	}

}
