<%@ page language="java" import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton, com.blackducksoftware.protex.sdk.idcopier.SDKUtils" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
serverSingleton ss = serverSingleton.getServerSingleton();

String sourceProj = request.getParameter("sx");
String sourcePath = request.getParameter("sy");
String destProj = request.getParameter("dx");
String destPath = request.getParameter("dy");
String user = ss.getUserName(session.getId());
String password = ss.getPassword(session.getId());
String recurse = request.getParameter("r");
String direction = request.getParameter("d");
String host = request.getParameter("h");
String dbrString = request.getParameter("dbr");

// NI-5:  These paths come from the projectTree.jsp and will need to be encoded again.  This is because
// they are being passed to copyIDs2.jsp, where special characters may cause URL problems.
sourcePath = SDKUtils.encodeString(sourcePath);
destPath = SDKUtils.encodeString(destPath);

System.out.println("Source path inside copyIDs: " + sourcePath);
System.out.println("Destination path inside copyIDs: " + destPath);

boolean isRecurse = false;

if("true".equals(recurse)) isRecurse = true;

if(!ss.sessionStillActive(session.getId())) response.sendRedirect("./login.jsp");
else { 
  //System.err.println("sourceProj = '" + sourceProj + "'");
  //System.err.println("sourcePath = '" + sourcePath + "'");
  //System.err.println("destProj = '" + destProj + "'");
  //System.err.println("destPath = '" + destPath + "'");
  //System.err.println("recurse = '" + recurse + "'");
  System.err.println("copyIDs defer BOM refresh = '" + dbrString + "'");

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
       function cp2() 
         {
         document.location="copyIDs2.jsp?sx=<%= sourceProj %>&sy=<%= sourcePath %>&dx=<%= destProj %>&dy=<%= destPath %>&r=<%= recurse %>&h=<%= host %>&dbr=<%= dbrString %>";
         }
    </script>
  </head>
  
  <body onload="cp2();">
    <table width='100%'><tr><td><table border="0" width="100%"><tr><td width="400"><h5><b>User:</b>&nbsp;<i><%= ss.getUserName(session.getId()) %></i><br/><br/></h5></td><td width='100%'>&nbsp;</td></tr></table>&nbsp;</td><td width='200px' align="right"><table cellpadding="0" cellspacing="0" width="100%"><tr><td><img alt="" src="images/Protex.gif" width="166" height="50"></td><td>ID Copier</td></tr></table></td></tr></table>
    <center>
      <br/><br/><br/><b>Please wait.<br/>Copying ID's from <%= sourceProj %> path <%= sourcePath %><br/>
      to  <%= destProj %> path <%= destPath %><br/>
      <% if(isRecurse) out.print("and recursing to all lower files and directories"); %>
    </b></center>
  </body>
</html>
<% } %>