package com.example.stockmonitor.servlet;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.example.stockmonitor.dataaccess.DataAccess;
import com.example.stockmonitor.dataaccess.DataAccessException;
import com.example.stockmonitor.dataaccess.SQLDataAccess;
import com.example.stockmonitor.dataaccess.util.SQLDataPool;
import com.example.stockmonitor.dataaccess.util.TomcatMySQLDataPool;
import com.example.stockmonitor.restclient.RestApiClient;


public class ProcessingServlet extends HttpServlet{
	private SQLDataPool pool=new TomcatMySQLDataPool();
	private DataAccess da=new SQLDataAccess(pool);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action=req.getParameter("Action");
		if (action!=null){
			HttpSession session=req.getSession();
			String username=(String) session.getAttribute("user");
			if (username==null){
				//can send them to some login page
				//here, for the sake of simplicity
				username="user1";
//				RestApiClient client=new RestApiClient(this.getServletContext());
//				client.getAllSymbols("user1");
			}
			String symbol=req.getParameter("symbol");
			if (action.equals("Add")){
				try{
					da.addSymbol(username, symbol);
				}
				catch(DataAccessException e){
					//do something
				}
			}
			else if (action.equals("Delete")){
				
			}
		}
		PrintWriter out=resp.getWriter();
		out.println("Hello!");
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		doGet(req, resp);
	}
	
	  
}
