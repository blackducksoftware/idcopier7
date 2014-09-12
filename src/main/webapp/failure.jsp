<%@ page language="java" import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
serverSingleton ss = serverSingleton.getServerSingleton();

String sourceProj = request.getParameter("s");
String sourcePath = request.getParameter("sy");
String destProj = request.getParameter("d");
String destPath = request.getParameter("dy");
String user = ss.getUserName(session.getId());
String password = ss.getPassword(session.getId());
String recurse = request.getParameter("r");
String direction = request.getParameter("di");
String host = ss.getHost(session.getId());
boolean isRecurse = false;

if("true".equals(recurse)) isRecurse = true;

//System.err.println("sourceProj = '" + sourceProj + "'");
//System.err.println("sourcePath = '" + sourcePath + "'");
//System.err.println("destProj = '" + destProj + "'");
//System.err.println("destPath = '" + destPath + "'");
//System.err.println("recurse = '" + recurse + "'");

if(!ss.sessionStillActive(session.getId())) response.sendRedirect("./login.jsp");
else { 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Protex ID Copier</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <script>
       function returnToMain() 
         {
         document.location="projectTree.jsp?lprojId=<%= sourceProj %>&rprojId=<%= destProj %>&d=<%= direction %>&host=<%= host %>";
         }
    </script>
  </head>
  
  <body>
    <table width='100%'><tr><td><table border="0" width="100%"><tr><td width="400"><h5><b>User:</b>&nbsp;<i><%= ss.getUserName(session.getId()) %></i><br/><br/></h5></td><td width='100%'>&nbsp;</td></tr></table>&nbsp;</td><td width='200px' align="right"><table cellpadding="0" cellspacing="0" width="100%"><tr><td><img alt="" src="images/Protex.gif" width="166" height="50"></td><td>ID Copier</td></tr></table></td></tr></table>
    <center>
      <br/><br/><br/><b>ID's <u>were not</u> copied successfully. Please ask your administrator to check the web logs.<br/>
      <input type="button" value="Ok" onclick="returnToMain();" />
      </b></center>
  </body>
</html>
<% } %>