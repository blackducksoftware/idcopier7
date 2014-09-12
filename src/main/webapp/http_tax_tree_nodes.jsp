<%@ page import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton" language="java" %>
<%
String nodeId = request.getParameter("catId");
String host = request.getParameter("h");
String projId = request.getParameter("p");
String sTreeNum = request.getParameter("t");

int treeNum = 1;
if(sTreeNum != null) {
  try {
    treeNum = Integer.parseInt(sTreeNum);
    }
  catch (Exception e) {
    }
  }
//System.err.println("http_tax_tree_nodes.jsp -- nodeId is " + nodeId + " and host is " + host + " and projectId is " + projId);
serverSingleton ss = serverSingleton.getServerSingleton();

/*
Params:
sessionID,
projectID
startingPath
host,
aggregate,
treeNum,
getPending - when is this false?
getSeperatePendingCounts - When is this ever true?
getIdentified - When is this ever true?

*/
String ret = ss.getCodeTreeHTML(session.getId(), projId, nodeId, host, false, treeNum, true, false, false);

%><%= ret %>