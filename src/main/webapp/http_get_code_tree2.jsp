<%@ page import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton" language="java" %>
<%
serverSingleton ss = serverSingleton.getServerSingleton();
String host = request.getParameter("h");
String sTreeNum = request.getParameter("t");
String u = ss.getUserName(session.getId());
String p = ss.getPassword(session.getId());
String projId = request.getParameter("p");

if(projId==null) projId="";

int treeNum = 1;
if(sTreeNum != null) {
  try {
    treeNum = Integer.parseInt(sTreeNum);
    }
  catch (Exception e) {
    }
  }
//System.err.println("http_get code tree2.jsp -- projId is " + projId + ", host is " + host + ", treeNum is " + treeNum + " user is " + u + " password is " + p);

String ret = "";

if(projId.length()>0) ret = ss.getCodeTreeHTML(session.getId(),projId, "", host, false, treeNum, true, false, false, false);

out.print(ret);
%>