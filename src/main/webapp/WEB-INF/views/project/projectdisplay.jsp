<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- 

	The initial display after the login. 
	Contains the server selectors, project pulldown and JSON representing tree paths.

 -->

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Protex ID Copier | Main</title>

<link rel="shortcut icon" href="images/favicon.png" type="image/png" />
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/justified-nav.css">

</head>
<body>
	<div class="main">
		<div class="header-logo">
			<img src="images/tempIcon.gif">
		</div>
		<hr>
		<div class="project-container">
			<div class="col-sm-5 col-lg-5 col-mx-5">
				<div class="container well">
					<table class="table">
						<tbody>
							<tr>
								<h4>Source Project</h4>
							</tr>
							<tr>
								<td><select class="form-control selectSourceServer"
									title="Select Source Server">
								</select></td>
							</tr>
							<tr>
								<form:form method="POST" action="processProject.do">

									<td><select id="selectSourceProject"
										name="selected-project-source-id"
										class="form-control selectSourceProject">
											<option value="1">No Projects</option>
									</select></td>
								</form:form>

							</tr>
						</tbody>
					</table>

					<h3>Project ID: ${projectId}</h3>
					<br /> Project JSON: ${projectJsonTree}
				</div>
			</div>
			<div class="col-sm-2 col-lg-2">
				<table class="table">
					<tbody>
						<tr>
							<td><h4>Defer BOM refresh</h4></td>
							<td></td>
						</tr>
						<tr>
							<td><h4>Recursive</h4></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="col-sm-5 col-lg-5">
				<div class="container well">
					<table class="table">
						<tbody>
							<tr>
								<h4>Destination Project</h4>
							</tr>
							<tr>
								<td><select id="selectDestinationServer"
									name="selectDestinationServer"
									class="form-control selectDestinationServer">
										<option value="1">https://server-one.blackducksoftware.com</option>
										<option value="2">https://server-two.blackducksoftware.com</option>
								</select></td>
							</tr>
							<tr>
								<td><select id="selectDestinationProject"
									name="selectDestinationProject"
									class="form-control selectDestinationProject">
										<option value="1">No Projects</option>
								</select></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/main.js"></script>
</html>

