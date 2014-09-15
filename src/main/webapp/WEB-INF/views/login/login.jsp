
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
    pageEncoding="ISO-8859-1"%>  
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>  

<p><p>
<h2>Please login:</h2>
<form:form method="POST" commandName="protex-server" action="login.do">
   <table>
    <tr>
        <td><form:label path="serverName">Server</form:label></td>
        <td><form:input path="serverName" /></td>
    </tr>
    <tr>
        <td><form:label path="userName">User</form:label></td>
        <td><form:input path="userName" /></td>
    </tr>
    <tr>
        <td><form:label path="password">Password</form:label></td>
        <td><form:input path="password" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Submit"/>
        </td>
    </tr>
</table>  
</form:form>