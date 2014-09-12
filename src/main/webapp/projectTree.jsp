<%@ page language="java" import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.domain.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton,com.blackducksoftware.sdk.protex.project.Project" pageEncoding="ISO-8859-1"%>
<%

serverSingleton ss = serverSingleton.getServerSingleton();
String projId=request.getParameter("projId");
String lprojId=request.getParameter("lprojId");
String rprojId=request.getParameter("rprojId");
String host=request.getParameter("host");
if(host == null) {
  host = ss.getHost(session.getId());
  }
String direction = request.getParameter("d");

if(!ss.sessionStillActive(session.getId())) response.sendRedirect("./login.jsp");
else { 
   

   
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Project p = null;
if(projId!=null) p = ss.getProject(projId, session.getId());

String projName = null;
if(p!=null) p.getName();

%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
	    var projHost = '<%= host %>';
	    var projId = '<%= projId %>';
      
        var activeTreeNum = 1;
	
		function getChildNodes(req) {
		    //alert('getChildNodes');
		    obj = getObject(currentId + '_k' + activeTreeNum);
	        if(obj != null) {
	            obj.innerHTML = 'loading...';
		        }
			if (req.readyState == 4) {
		        if (req.status == 200) {
		            if(obj != null) {
		        		obj.innerHTML = req.responseText;
		        	}
		        } else {
		            alert("There was a problem retrieving HTTP response. readyState= " + req.readyState + "Status:\n" + req.statusText);
		            alert(req.responseText);
		        }
		    }
		}

		function getGrandfatheringOptions(req) {
		    //alert('getGrandatheringOptions');
		    obj = getObject('tdMatches');
	        if(obj != null) {
	            obj.innerHTML = 'loading...';
		        }
			if (req.readyState == 4) {
		        if (req.status == 200) {
		            var obj = getObject('tdMatches');
		            if(obj != null) {
		        		obj.innerHTML = req.responseText;
		        	}
		        } else {
		            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
		        }
		    }
		}

	  function doLogout() {
	    document.location='logout.jsp';
	    }
	    
	  function togglePlusMinus(objId) {
	    var obj = document.getElementById(objId);
	    if(obj) {
	      //alert('togglePlusMinus ' + obj + ' and innerHTML is ' + obj.innerHTML);
	      if(obj.innerHTML == '+') obj.innerHTML = '-';
	      else obj.innerHTML = '+';
	      }
	    }
	    
	  function toggle(objName) {
	    //alert('toggle ' + objName);
	    var obj = document.getElementById(objName);
	    if(obj) {
	      if(obj.style.display == 'none') obj.style.display = '';
	      else obj.style.display = 'none';
	      }
	    }
	    
	    function getSubtree(req) {
	    
	      }
	    
	    function getCodeTree_sel1(req) {
	        getCodeTree("leftTree", req);
	        }

	    function getCodeTree_sel2(req) {
	        getCodeTree("rightTree", req);
	        }
      
        function disableDropdowns() {
            document.getElementById('sel1').disabled=true;
            document.getElementById('sel2').disabled=true;
            }

        function enableDropdowns() {
            document.getElementById('sel1').disabled=false;
            document.getElementById('sel2').disabled=false;
            }

	    function getCodeTree(divName, req) {
	        //alert('getCodeTree ' + divName);
	        disableDropdowns();
	        var ret = "";
			if (req.readyState == 4) {
		        if (req.status == 200) {
		          ret = req.responseText;
		          }
		        else {
		          alert("There was a problem retrieving HTTP response:\n" + req.statusText);
		          }
		        }
		    else if(req.readyState == 1) {
		      ret = "loading...";
		      }
		    //else alert("req.readyState=" + req.readyState);
		    
		    document.getElementById(divName).innerHTML=ret;
		    enableDropdowns();
	        }

	    function getProjDropdownsFilledIn(req) {
	        //alert('getProjDropdownsFilledIn');
	        var ret = "";
	        var ret2 = "";
			if (req.readyState == 4) {
		        if (req.status == 200) {
		          ret = req.responseText;
		          ret2 = req.responseText;
		          ret = "<select id='sel1' onchange='projSel(this, projHost);'>" + ret + "</select>";
		          ret2 = "<select id='sel2' onchange='projSel(this, projHost);'>" + ret2 + "</select>";
		          //alert("getProjDropdownsFilledIn should be set " + ret);
		          }
		        else {
		            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
		        }
		      }
		    else if(req.readyState == 1) {
		      ret = "loading...";
		      ret2=ret;
		      }
		    //else alert("req.readyState=" + req.readyState);
		    
		    document.getElementById('sel1div').innerHTML=ret;
		    document.getElementById('sel2div').innerHTML=ret2;
		    <% if(lprojId!=null) {
		        out.println("if(document.getElementById('sel1')!=null) {"); 
		        out.println("setSelected('" + lprojId + "',document.getElementById(\"sel1\"));"); 
                String ret = ss.getCodeTreeHTML(session.getId(),lprojId, "", ss.getHost(session.getId()), false, 1, true, false, false, false);
                ret = ret.replace("'","\\'");
                ret = ret.replace("\\\\'","\\'");
                out.println("document.getElementById('leftTree').innerHTML = '" + ret + "';");
                out.println("document.getElementById('leftTxtPath').disabled=false;");
//		        out.println("projSel(document.getElementById(\"sel1\"),projHost);"); 
		        out.println("}"); 
		        } %>
		    <% if(rprojId!=null) {
		        out.println("if(document.getElementById('sel2')!=null) {"); 
		        out.println("setSelected('" + rprojId + "',document.getElementById(\"sel2\"));"); 
                String ret = ss.getCodeTreeHTML(session.getId(),rprojId, "", ss.getHost(session.getId()), false, 2, true, false, false, false);
                ret = ret.replace("'","\\'");
                ret = ret.replace("\\\\'","\\'");
                out.println("document.getElementById('rightTree').innerHTML = '" + ret + "';");
//		        out.println("projSel2(document.getElementById(\"sel2\"),projHost);"); 
		        out.println("}"); 
		        } %>
	      }

	   
	      
	   function copyLtoR() {
	     //alert('copy L to R');
	     var recurse = document.getElementById('recurse').checked;
	     var sourceProj = document.getElementById('sel1').value;
	     var sourcePath = document.getElementById('leftSelection').innerHTML;
	     var destProj = document.getElementById('sel2').value;
	     var destPath = document.getElementById('rightSelection').innerHTML;
         var deferBOMRefresh = document.getElementById('deferBOMRefresh').checked;
	     
         // NI-5:  Special characters can come from folders, they need to be encoded.
         sourcePath = encodeURIComponent(sourcePath);
         destPath = encodeURIComponent(destPath);
         
	     document.location = 'copyIDs.jsp?sx=' + sourceProj + "&sy=" + sourcePath + "&dx=" + destProj + "&dy=" + destPath + "&r=" + recurse + "&d=r&h=" + projHost + "&dbr=" + deferBOMRefresh;
	     }
	     
	   function copyRtoL() {
	     //alert('copy R to L');
	     var recurse = document.getElementById('recurse').checked;
	     var destProj = document.getElementById('sel1').value;
	     var destPath = document.getElementById('leftSelection').innerHTML;
	     var sourceProj = document.getElementById('sel2').value;
	     var sourcePath = document.getElementById('rightSelection').innerHTML;
	     
         // NI-5:  Special characters can come from folders, they need to be escaped
         sourcePath = encodeURIComponent(sourcePath);
         destPath = encodeURIComponent(destPath);
	     
	     document.location = 'copyIDs.jsp?sx=' + sourceProj + "&sy=" + sourcePath + "&dx=" + destProj + "&dy=" + destPath + "&r=" + recurse + "&d=l&h=" + projHost;
	     }
	   
	   function updateLeftSelection() {
	     //alert("updateLeftSelection");
  	     document.getElementById('leftSelection').innerHTML=document.getElementById('leftTxtPath').value;
  	     showButtonsIfReady();
	     }
	   
	   function updateRightSelection() {
	     //alert("updateRightSelection");
  	     document.getElementById('rightSelection').innerHTML=document.getElementById('rightTxtPath').value;
  	     showButtonsIfReady();
	     }

	   function setSelected(projId, sel) {
	     var options = sel.options;
	     for(var t=0;t<options.length;t++) {
	       var thisOne = options[t];
	       if(projId == thisOne.value) {
	         sel.selectedIndex = t;
	         t = options.length;
	         }
	       }
	     }
	     
	</script>
	<script src="scripts/xmlhttp.js" type="text/javascript"></script>
  </head>
  <body link="#000000" alink="#000000" vlink="#000000" onload="populateProjDropdowns('<%= ss.getHost(session.getId()) %>', document.getElementById('sel1'), document.getElementById('sel2'));">
    <table width='100%'><tr><td><table border="0" width="100%"><tr><td width="400"><h5><b>User:</b>&nbsp;<i><%= ss.getUserName(session.getId()) %></i><br/><b>Host:</b>&nbsp;<%=host %><br/><input type='button' value='Log out' onclick='doLogout()' /><br/></h5></td><td width='100%'>&nbsp;</td></tr></table>&nbsp;</td><td width='200px' align="right"><table cellpadding="0" cellspacing="0" width="100%"><tr><td><img alt="" src="images/Protex.gif" width="166" height="50"></td><td>ID Copier</td></tr></table></td></tr></table>
    <center>
      <form id="hidForm" method="post"><input type="hidden" name="hidProjId" id="hidProjId"><input type="hidden" name='hidAction' id='hidAction'></form>
      <table border="1" width="90%">
        <tr>
        <td width="500px" colspan="2" align="center"><div id="sel1div">
	        <select id='sel1' onchange='projSel(this);' ></select>
	        </div><br/>Type a path<br>
	        <input onkeyup="updateLeftSelection();" name="leftTxtPath" id="leftTxtPath" type="text" disabled="true" />
	        <br/>-or-<br/>
	        <i>Selected:</i><div id="leftSelection">none</div>
	     </td>
        <td width="50px;" align="center">
        <div id='btnHider' style="display:none"><input onClick='copyLtoR();' type='button' id='copyLtoR' value='Copy-->'/><br/>
        &nbsp;<!-- <input onClick='copyRtoL();' type='button' id='copyRtoL' value='<--Copy' /><br/>&nbsp; -->
        <br/>Recursive<br/><input type="checkbox" id="recurse" name="recurse" checked="checked"/>
        <br/>&nbsp;<br/>Defer BOM refresh<br/><input type="checkbox" id="deferBOMRefresh" name="deferBOMRefresh"/></div></td>
        <td colspan="2" align="center" width="500px"><div id='sel2div'><select id='sel2' onchange='projSel(this);'></select></div><br/><!-- Type a path<br><input onkeyup="updateRightSelection();" name="rightTxtPath" id="rightTxtPath" type="text" disabled="true" /><br/>-or-  --><br/>&nbsp;<br/>&nbsp;<br/><i>Selected:</i><div id="rightSelection">none</div></td></tr>
        <tr>
          <td align='left' valign='top' width="500px"><div id="leftTree">
             <% //ss.getCodeTreeHTML(session.getId(),projId, "", host, false, 1) %>
             </div>
          </td>
          <td align='left' valign='top' width='1px'>
            <div id='tdMatches1' border='0'></div>
          </td>
          <td valign='center' align='center'></td>
          <td align='left' valign='top' width="500px"><div id="rightTree">
             <% //ss.getCodeTreeHTML(session.getId(),projId, "", host, false, 2) %>
             </div>
          </td>
          <td align='left' valign='top' width='1px'>
            <div id='tdMatches2' border='0'></div>
          </td>
        </tr>
      </table>
    </center>
  </body>
</html>
<%}%>