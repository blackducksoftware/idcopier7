<%@ page import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton" language="java" %><%
serverSingleton ss = serverSingleton.getServerSingleton();
String host = request.getParameter("h");
String projId1 = request.getParameter("projId1");
String projId2 = request.getParameter("projId2");
String u = ss.getUserName(session.getId());
String p = ss.getPassword(session.getId());
System.err.println("top of http_submit_for_id_copy. u=" + u + " and p=" + p);
String message = "";
if(host==null || projId1==null || projId2==null) {
  System.err.println("--ERROR: Required parameters not sent!");
  }
else{
  System.err.println("--------------http_submit_for_id_copy---------------");
  if(ss.copyIDs(host, u, p, projId1, "", projId2, "", true, session.getId(), false)) {
    message = "Copy Succeeded";
    }
  else {
    message = "Copy failed";
    }
  }
%><%= message %>
