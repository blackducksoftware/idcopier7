<%@ page language="java" import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.domain.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton, com.blackducksoftware.protex.sdk.idcopier.SDKUtils" pageEncoding="ISO-8859-1"%>
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
//CopyIdUtility cu = new CopyIdUtility(host,user,password);
boolean deferBOMRefresh=false;
boolean isRecurse = false;

if("true".equals(recurse)) isRecurse = true;
if("true".equals(dbrString)) deferBOMRefresh = true;

System.out.println("Source path inside copyIDs2: " + sourcePath);
System.out.println("Dest path inside copyIDs2: " + destPath);


if(!ss.sessionStillActive(session.getId())) response.sendRedirect("./login.jsp");
else { 
  System.err.println("sourceProj = '" + sourceProj + "'");
  System.err.println("sourcePath = '" + sourcePath + "'");
  System.err.println("destProj = '" + destProj + "'");
  System.err.println("destPath = '" + destPath + "'");
  System.err.println("recurse = '" + recurse + "'");
  System.err.println("copyIDs2 defer BOM refresh = '" + deferBOMRefresh + "'");

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
         document.location="copyIDs2.jsp?sx=<%= sourceProj %>&sy=<%= sourcePath %>&dx=<%= destProj %>&dy=<%= destPath %>&r=<%= recurse %>";
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
<% 
  
  //cu.copyIDs( sourceProj, sourcePath, destProj, destPath);
  boolean ret = ss.copyIDs(host,user,password,sourceProj,sourcePath,destProj,destPath,isRecurse,session.getId(),deferBOMRefresh);
  
  if(ret) response.sendRedirect("success.jsp?s=" + sourceProj + "&d=" + destProj + "&di=" + direction);
  else response.sendRedirect("failure.jsp?s=" + sourceProj + "&d=" + destProj + "&di=" + direction);
  } %>