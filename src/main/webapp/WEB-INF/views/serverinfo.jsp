<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


</head>
<body>

	<c:if test="${server.error == null}">
		<h2>
			Server Information : ${server.serverName}
			</h1>
			<h2>User: ${server.userName}</h2>
			<p>

				<select name="project">
					<c:forEach items="${server.projects}" var="pinfo">
						<option value="${pinfo.name}"
							selected=${role == selectedPinfo ? 'selected' : ''}>${pinfo.name}</option>
					</c:forEach>
				</select>
	</c:if>
	<c:if test="${server.error != null}">
		<h2>
			<font color="red">Error: ${server.error}</font>
		</h2>
	</c:if>

	<p>
</body>
</html>

