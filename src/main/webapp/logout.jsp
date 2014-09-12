<%@ page language="java" import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton" pageEncoding="ISO-8859-1"%>
<%

serverSingleton ss = serverSingleton.getServerSingleton();
if(ss.sessionStillActive(session.getId()))
session.invalidate();
response.sendRedirect("./login.jsp");
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


//if(projects!=null) System.err.println("projects list is of size: " + projects.size());
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Protex Scan scheduler</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <body>
  </body>
</html>
