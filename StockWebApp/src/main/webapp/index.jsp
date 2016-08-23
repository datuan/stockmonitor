<html>
<%@ page import="com.example.stockmonitor.restclient.*" %>
<%@ page import="com.example.stockmonitor.data.*" %>
<%@ page import="java.util.*" %>

<body>
<form action="process" method="get">
Symbol: <input type="text" id="symbol" name="symbol"/><br/>
<input type="submit" value="Add" name="Action"/><input type="submit" value="Delete" name="Action"/><input type="reset">
</form>
<%
	String msg=(String)session.getAttribute("message"); 
	if (msg!=null){
%>
<p>Message: ${message}</p>
<% 
		session.removeAttribute("message");
	}
%>
<% 
	String requestURL=request.getRequestURL().toString();
	String contextPath=request.getContextPath();
	
	//get absolute address to the rest api
	String restApiPath=(String)session.getAttribute("restApiPath");
	if (restApiPath==null){
		restApiPath=com.example.stockmonitor.servlet.ServletUtility.getRestPath(requestURL, contextPath);
		session.setAttribute("restApiPath", restApiPath);
	}
	
	//get username
	String userName=(String)request.getSession().getAttribute("username");
	if (userName==null){
		//we should point to a login page, however, just for the sake of simplicity ...
		userName="user1";
		request.getSession().setAttribute("username", userName);
	}
	
	RestApiClient client=new RestApiClient(restApiPath,userName,"");
	List<Stock> stocks=client.getAllSymbols(userName);
%>
<h2>Stock symbols that you followed:</h2>
<table border="1">
<tr>
	<td>Symbol</td>
	<td>Price</td>
	<td>Last Trade Time</td>
	<td>Last Query Time</td>
	<td></td>
</tr>
<%	//print out followed stocks and current prices
	if (stocks!=null){
		for (Stock s : stocks){
			out.println("<tr>");
			out.println("<td>"+s.symbol+"</td>");
			out.println("<td>"+s.lastTradePrice+"</td>");
			out.println("<td>"+s.lastTradeTime+"</td>");
			out.println("<td>"+s.queryTime+"</td>");
			out.println("<td><a href=\"detail.jsp?symbol="+s.symbol+"\">Detail...</a></td>");
			out.println("</tr>"); 
		} 
	}
%>
</table>
</body>
</html>
