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
	
	RestApiClient client=new RestApiClient(restApiPath,userName,"");
	String symbol=request.getParameter("symbol");
	List<Stock> stocks=client.getSymbolHistory(symbol);
	if (stocks!=null && stocks.size()==0){
		out.println("<h2>No information about the symbol</h2>");
	}
	else{
%>
<h3>Information about stock symbol <%=symbol%></h3>

<table border="1">
<tr>
	<td>Symbol</td>
	<td>Price</td>
	<td>Last Trade Time</td>
	<td>Last Query Time</td>
</tr>
<%	//print out followed stocks and current prices
		for (Stock s : stocks){
			out.println("<tr>");
			out.println("<td>"+s.symbol+"</td>");
			out.println("<td>"+s.lastTradePrice+"</td>");
			out.println("<td>"+s.lastTradeTime+"</td>");
			out.println("<td>"+s.queryTime+"</td>");
			out.println("</tr>"); 
		}
%>
</table>
<div id="chart"/>
<!-- javascript for chart -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  <script type="text/javascript">
  google.charts.load('current', {packages: ['corechart', 'bar']});
  google.charts.setOnLoadCallback(drawBasic);

  function drawBasic() {

        var data = new google.visualization.DataTable();
        data.addColumn('timeofday', 'Time of Day');
        data.addColumn('number', 'Motivation Level');

        data.addRows([
          [{v: [8, 0, 0], f: '8 am'}, 1],
          [{v: [9, 0, 0], f: '9 am'}, 2],
          [{v: [10, 0, 0], f:'10 am'}, 3],
          [{v: [11, 0, 0], f: '11 am'}, 4],
          [{v: [12, 0, 0], f: '12 pm'}, 5],
          [{v: [13, 0, 0], f: '1 pm'}, 6],
          [{v: [14, 0, 0], f: '2 pm'}, 7],
          [{v: [15, 0, 0], f: '3 pm'}, 8],
          [{v: [16, 0, 0], f: '4 pm'}, 9],
          [{v: [17, 0, 0], f: '5 pm'}, 10],
        ]);

        var options = {
          title: 'Motivation Level Throughout the Day',
          hAxis: {
            title: 'Time of Day',
            format: 'h:mm a',
            viewWindow: {
              min: [7, 30, 0],
              max: [17, 30, 0]
            }
          },
          vAxis: {
            title: 'Rating (scale of 1-10)'
          }
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