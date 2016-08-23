package com.example.stockmonitor.servlet;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.core.Response;

import com.example.stockmonitor.restclient.RestApiClient;


@SuppressWarnings("serial")
public class ProcessingServlet extends HttpServlet{
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
			}
			String restApiPath=(String)session.getAttribute("restApiPath");
			if (restApiPath==null){
				restApiPath=ServletUtility.getRestPath(req.getRequestURL().toString(), req.getContextPath());
				session.setAttribute("restApiPath", restApiPath);
			}
			RestApiClient client=new RestApiClient(restApiPath, username, "");
			String symbol=req.getParameter("symbol");
			if (action.equals("Add")){
				boolean result=client.addStock(symbol);
				if (result==false){
					session.setAttribute("message", "Unable to add symbol "+symbol+", symbol code might not exist, or already added.");
				}
				else{
					session.setAttribute("message","Symbol "+symbol+" added");
				}
			}
			else if (action.equals("Delete")){
				boolean result=client.delStock(symbol);
				if (result==false){
					session.setAttribute("message", "Unable to delete symbol "+symbol);
				}
				else{
					session.setAttribute("message","Symbol "+symbol+" deleted");
				}
			}
//			req.getRequestDispatcher("index.jsp").forward(req, resp);
			resp.sendRedirect("index.jsp");
			
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		doGet(req, resp);
	}
	
	  
}
