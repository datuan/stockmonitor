<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.example.stockmonitor.restclient.*" %>
<%@ page import="com.example.stockmonitor.data.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Stock history</title>
</head>
<body>
<% 
	//get absolute address to the rest api
	String restApiPath=(String)session.getAttribute("restApiPath");
	if (restApiPath==null){
		String requestURL=request.getRequestURL().toString();
		String contextPath=request.getContextPath();
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
	//Date format
	//SimpleDateFormat dateFormat=new SimpleDateFormat("MM dd, yyyy HH:mm:ss");
	RestApiClient client=new RestApiClient(restApiPath,userName,"");
	String symbol=request.getParameter("symbol");
	List<Stock> stocks=client.getSymbolHistory(symbol);
	if (stocks!=null && stocks.size()==0){
		out.println("<h2>No information about the symbol</h2>");
	}
	else{
%>
<h3>Information about stock symbol <%=symbol%></h3>

<table>
<tr><td><div id="chart"/></td></tr>
</table>
<table border="1">
<tr>
	<td>Symbol</td>
	<td>Price</td>
	<td>Last Trade Time<br/>(UTC)</td>
	<td>Last Query Time<br/>(Local)</td>
</tr>
<%	//print out followed stocks and current prices
		for (Stock s : stocks){
			out.println("<tr>");
			out.println("<td>"+s.symbol+"</td>");
			out.println("<td>"+s.lastTradePrice+"</td>");
			out.println("<td>"+DateUtil.UTCToSimpleFormat(s.lastTradeTime)+"</td>");
			out.println("<td>"+DateUtil.epochToSimpleFormat(s.queryTime)+"</td>");
			out.println("</tr>"); 
		}
%>
</table>

<!-- javascript for chart -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  <script type="text/javascript">
  google.charts.load('current', {packages: ['corechart', 'bar']});
  google.charts.setOnLoadCallback(drawBasic);

  function drawBasic() {

        var data = new google.visualization.DataTable();
        data.addColumn('datetime', 'Date');
        data.addColumn('number', 'Price');
		//Date be in format ('MM DD, yyyy hh:mm:ss'))
        data.addRows([
          //[new Date("07 21, 2011 00:00:00"), 1],
          //[new Date("07 21, 2011 07:20:00"), 2],
<%
		for (Stock s : stocks){
			//out.print("[new Date(\""+dateFormat.format(new Date(s.queryTime))+"\")," +s.lastTradePrice +"],\n"); 
			out.print("[new Date(\""+DateUtil.epochToSimpleFormat(s.queryTime)+"\")," +s.lastTradePrice +"],\n");
		}
%>
        ]);

        var options = {
          title: 'History of Stock Prices',
          hAxis: {
            title: 'Time',
          },
          vAxis: {
            title: 'Stock Price'
          },
          'width':400,
          'height':300,
          legend: 'none'
        };

        var chart = new google.visualization.ColumnChart(
          document.getElementById('chart'));

        chart.draw(data, options);
      }
  </script>

<% 
	}
%>
</body>
</html>