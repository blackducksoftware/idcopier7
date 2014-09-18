<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Black Duck | IdCopier</title>

<link rel="shortcut icon" href="images/favicon.png" type="image/png" />
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/justified-nav.css">

</head>
<body>

	<c:if test="${server.error == null}">
		<h2>
			Server Information : <small>${server.serverName}</small>
		</h2>
		<h2>
			User: <small>${server.userName}</small>
		</h2>

		<form:form method="POST" action="processProject.do">
			<select name="project-source-id" onchange="this.form.submit();">
			
				<c:forEach var="pinfo" items="${server.projects}">
					<option value="${pinfo.projectId}">${pinfo.name}</option>
				</c:forEach>
				
			</select>
		</form:form>
	</c:if>
	<c:if test="${server.error != null}">
		<h2>
			<font color="red">Error: ${server.error}</font>
		</h2>
	</c:if>
			<h3>Project ID: ${projectId}</h3>
					<br /> Project JSON: ${projectJsonTree}
	<p>
</body>
</html>

