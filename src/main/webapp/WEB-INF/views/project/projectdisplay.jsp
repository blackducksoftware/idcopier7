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
<link rel="stylesheet" href="css/EasyTree/skin-win8/ui.easytree.css">
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
								<td><select id="selectSourceProject"
									name="selectSourceProject"
									class="form-control selectSourceProject">
										<option value="1">No Projects</option>
								</select></td>
							</tr>
							<tr>
								<td><input class="form-control userSourcePathInput"
									id="userSourcePathInput" name="userSourcePathInput"
									placeholder="Source Path"></input></td>
							</tr>
							<tr>
								<td><i>Selected:</i>
									<h5 class="sourceSelectedPath"></h5></td>
							</tr>
							<tr>
								<td></td>
							</tr>
						</tbody>
					</table>
					<div id="sourceCodeTree" class="sourceCodeTree"
						style="float: left; width: 100%; height: 200px;"></div>
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
								<h4>Target Project</h4>
							</tr>
							<tr>
								<td><select id="selectTargetServer"
									name="selectTargetServer"
									class="form-control selectTargetServer">
								</select></td>
							</tr>
							<tr>
								<td><select id="selectTargetProject"
									name="selectTargetProject"
									class="form-control selectTargetProject">
										<option value="1">No Projects</option>
								</select></td>
							<tr>
								<td><input class="form-control userTargetPathInput"
									id="userTargetPathInput" name="userTargetPathInput"
									placeholder="Target Path"></input></td>
							</tr>
							<tr>
								<td><i>Selected:</i>
									<h5 class="targetSelectedPath"></h5></td>
							</tr>
							<tr>
								<td></td>
							</tr>
						</tbody>
					</table>
					<div id="targetCodeTree" class="targetCodeTree"
						style="float: left; width: 100%; height: 200px;"></div>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/libs/EasyTree/jquery.easytree.js"></script>
<script src="js/main.js"></script>
</html>

