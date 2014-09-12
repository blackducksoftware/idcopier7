<%@ page language="java" import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.serverSingleton, com.blackducksoftware.proserv.idcopier.config.IDCProperties,com.blackducksoftware.protex.sdk.idcopier.domain.ICUser" pageEncoding="ISO-8859-1"%>

<%
	String errorMessage= "";

	serverSingleton ss = serverSingleton.getServerSingleton();
	
	IDCProperties properties = ss.loadProperties();
	if(properties == null)
		errorMessage += "Unable to load properties, see log file";
	
	String version = ss.getVersion();
	
	
	String user = request.getParameter("user");
	String password= request.getParameter("password");
	
	System.out.println("User supplied password: " + password);
	// Attempt to fill all the fields from property file
	// After every subsequent login, we will grab the user object and fill in the fields from the user.
	String serverFromProperties=properties.getProperty(properties.PROTEX_SERVER);
	String serverFromTextField = request.getParameter("selServer");
	String serverFromDropDown = request.getParameter("selServerDropDown");
	ArrayList<String> serverList = properties.getServerList();
	String server = "";  // This is the value to put into the text field.
	
	if(user == null || user.length() == 0)
	{
		user=properties.getProperty(properties.PROTEX_USER);
	}
	if(password == null || password.length() == 0)
	{
		password= properties.getProperty(properties.PROTEX_PASSWORD);
	}
	
	/**
	Server priorities
	1. Check for config
	2. Check for text
	3. Check for dropdown
	**/
	if(serverFromProperties != null && serverFromProperties.length() > 0)
	{
		server = serverFromProperties;
	}
	else if(serverFromTextField != null && serverFromTextField.length() > 0)
	{
		server = serverFromTextField;
	}
	else if(serverFromDropDown != null && serverFromDropDown.length() > 0)
	{
		server = serverFromDropDown;
	} 
		
	System.out.println("Using server: " + server);
	

	if(serverList == null || serverList.size() == 0)
	{
		System.out.println("Server list not populated");
	}
	else if(serverList.size() > 0)
	{
		System.out.println("Server list populated");
	}
	
	
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	boolean authenticated = false;
	
	if((user!=null && user.length() > 0) &&  (password != null && password.length() > 0) && server != null) 
	{
		  authenticated = ss.isUserAuthenticated(user, password, request.getSession().getId(), server);
		  if(authenticated)
		  {
		  		response.sendRedirect("./projectTree.jsp");
		  }
		  else 
		  {
			  	ICUser userObject = ss.getUserObjectBySession(request.getSession().getId());
			  	errorMessage = userObject.getLoginErrorMessage();
		  }
	}
 
if(!authenticated) 
{
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
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
	  function doLogin() 
	  {
	    if((document.getElementById('user').value!='') &&(document.getElementById('password').value!='')) document.getElementById('logonForm').submit();
	    else alert('Please enter name and password to log in');
	    }

	  function checkEnter(e){ //e is event object passed from function invocation
	    //alert('top checkEnter');
        var characterCode; //literal character code will be stored in this variable

        if(e && e.which){ //if which property of event object is supported (NN4)
          e = e;
          characterCode = e.which; //character code is contained in NN4's which property
          }
        else{
          e = event;
          characterCode = e.keyCode //character code is contained in IE's keyCode property
          }

        if(characterCode == 13){ //if generated character code is equal to ascii 13 (if enter key)
          document.getElementById('btnLogin').focus();
          doLogin(); //submit the form
          return false;
          }
        else{
          return true;
          }
      }

	  function checkEnter2(e){ //e is event object passed from function invocation
	    //alert('top checkEnter');
        var characterCode; //literal character code will be stored in this variable

        if(e && e.which){ //if which property of event object is supported (NN4)
          e = e;
          characterCode = e.which; //character code is contained in NN4's which property
          }
        else{
          e = event;
          characterCode = e.keyCode //character code is contained in IE's keyCode property
          }

        if(characterCode == 13){ //if generated character code is equal to ascii 13 (if enter key)
          document.getElementById('password').focus(); //set focus to password field
          return false;
          }
        else{
          return true;
          }
      }
	</script>
  </head>
  
  <body>
  
  <center><form name="logonForm" id="logonForm" method="post"><br/>&nbsp;<br/>&nbsp;<br/>&nbsp;<br/>&nbsp;
      <table width="300px"> 
        <tr>
          <td colspan="2" align="center">
          <table cellpadding="0" cellspacing="0" width="100%">
          	<tr>
          		<td><img alt="" src="images/Protex.gif" width="166" height="50"></td>
          		<td>ID Copier<br></td>
          	</tr>
          </table></td>
        </tr>
        <tr>
          <td colspan="2" align="center"><font color="red"><%=errorMessage %></font></td>
        </tr>
        <tr>
          <td align="right">User: </td>
          <td><input type="text" name="user" id="user" value="<%= user %>" onkeypress="checkEnter2(event)" style="width: 250px; padding: 2px; border: 1px solid black"/></td>
        </tr>
        <tr>
          <td align="right">Password: </td>
          <td><input type="password" value="<%= password %>" id="password" name="password" onkeypress="checkEnter(event)" style="width: 250px; padding: 2px; border: 1px solid black"/></td>
        </tr>
        <tr>
        	<td align="right">Server:</td>
        	<td><input type="text" id="selServer" name="selServer" value ="<%= server %>" style="width: 250px; padding: 2px; border: 1px solid black"/></td>
		</tr>
         <! -- Server drop list -->
         <tr>
         	<td align="right">Select:</td>
			<td>
			        <select name="selServerDropDown" style="width: 256px; padding: 2px; border: 1px solid black"/>         	
	         	<%  
				    for(String serverName : serverList) 
				    {
				      out.print("<option value='" + serverName + "'>" + serverName + "</option>\r\n");
				    }
				%>  				    
					</select>
         	</td>
         </tr> 
          
          
        <tr>
          <td colspan="2" align="center"><input id='btnLogin' type="button" value="Login" onclick="doLogin()"></td>
        </tr>
      </table>
    </form>  
    <p>
    <p><p><p>
            <div id="footer">&copy; 2006-2013 Black Duck - Confidential and Proprietary<sup>&reg;</sup>
				<br>
				
				Version: <%= version %>
				</div>
			</div>
  </center>
  </body>
</html>
<%}%>