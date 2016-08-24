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
	public int addSymbol(String userID, String symbolID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="insert ignore into stock_symbol(symbol) values(?)";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, symbolID);
			stmt.executeUpdate();
			stmt.close();
			sql="insert into follow(userid,symbol) values(?,?)";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, userID);
			stmt.setString(2, symbolID);
			int count=stmt.executeUpdate();
			stmt.close();
			conn.close();
			return count;
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server: "+e.getMessage());
		}
	}

	@Override
	public int addStockItem(Stock stock) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="insert into stocks (symbol, last_query, price, lastTrade) values (?,?,?,?)";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, stock.symbol);
			stmt.setLong(2, stock.queryTime);
			stmt.setDouble(3, stock.lastTradePrice);
			stmt.setString(4, stock.lastTradeTime);
			int count=stmt.executeUpdate();
			stmt.close();
			conn.close();
			return count;
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server: "+e.getMessage());
		}
	}

	@Override
	public int deleteCompany(String userID, String symbolID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="delete from follow where userid=? and symbol=?";
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, userID);
			stmt.setString(2, symbolID);
			int count=stmt.executeUpdate();
			stmt.close();
			
			boolean optional=false;
			//experimental feature
			if (optional){
				//if this user is the last user that follows the item
				//we should delete the item also, so the daemon does not download and store information of the stock item
				sql="select count(*) from follow where symbol=?";
				stmt=conn.prepareStatement(sql);
				stmt.setString(1, symbolID);
				ResultSet rs=stmt.executeQuery();
				int numberofFollowers=0;
				while (rs.next()){
					numberofFollowers=rs.getInt(1);
				}
				rs.close();
				stmt.close();
				if (numberofFollowers==0){
					sql="delete from stock_symbol where symbol=?";
					stmt=conn.prepareStatement(sql);
					stmt.setString(1, symbolID);
					stmt.executeUpdate();
				}
			}
			
			conn.close();
			return count;
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server: "+e.getMessage());
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
			throw new DataAccessException("Error(s) while contacting SQL server: "+e.getMessage());
		}
	}

	@Override
	public List<Stock> listCompany(String userID) throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			List<String> symbolList=followedSymbol(userID);
			String[] symbols=new String[symbolList.size()];
			if (symbols.length==0){
				//the user has followed no symbols
				return new ArrayList<Stock>(); //return an empty list
			}
			symbolList.toArray(symbols);
			List<Stock> stocks= sQuery.getStockPrices(symbols);
			//TODO: Since we already query the newest prices, let us save them to the database also
			
			//however, I want to do this in another thread, so no delay in showing the results to user
			Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try{
						for (Stock s : stocks){
							addStockItem(s);
						}
					}
					catch(DataAccessException e){
						//do nothing, is not important if we can not save this piece of updated information
					}
					
				}
			});
			t.start();
			
			return stocks;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error(s) while contacting web service for updated prices: "+e.getMessage());
		}
	}
	@Override
	public List<String> followedSymbol(String userID) throws DataAccessException {
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
			return symbolList;
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server: "+e.getMessage());
		}
	}
	@Override
	public List<String> getAllSymbols() throws DataAccessException {
		// TODO Auto-generated method stub
		try{
			Connection conn=getConnection();
			String sql="select symbol from stock_symbol";
			PreparedStatement stmt=conn.prepareStatement(sql);
			List<String> symbolList=new ArrayList<>();
			ResultSet rs=stmt.executeQuery();
			while (rs.next()){
				symbolList.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			conn.close();
			return symbolList;
		}
		catch (SQLException e){
			throw new DataAccessException("Error(s) while contacting SQL server: "+e.getMessage());
		}
	}

}
