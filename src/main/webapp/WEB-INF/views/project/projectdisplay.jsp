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
<!-- <link rel="stylesheet" href="css/EasyTree/skin-win8/ui.easytree.css">  -->
<link rel="stylesheet" href="css/Dynatree/skin-vista/ui.dynatree.css">
<link rel="stylesheet" href="css/justified-nav.css">
<link rel="stylesheet" href="css/menu.css">

</head>
<body>
	<div class="main">
		<div class="header-logo">
			<img src="images/tempIcon.gif">
		</div>
		<div>
			<nav class="animate">
				<div class="menu-header">
					Mouse0270 created this snippet on <strong>Bootsnipp</strong>
					network.
				</div>
				<ul>
					<li><a href="https://twitter.com/mouse0270">@mouse0270 on
							twitter</a></li>
					<li><a href="https://facebook.com/rem.mcintosh">Learn More
							about me on Facebook</a></li>
					<li><a href="http://bootsnipp.com/mouse0270">Snippets</a></li>
					<li><a href="https://github.com/mouse0270">Projects</a></li>
					<li><a
						href="http://bootsnipp.com/user/snippets/www.linkedin.com/in/remcintosh/">R�sum�</a>
					</li>
				</ul>
			</nav>
			<div class="nav-controller">
				<table>
					<tbody>
						<tr>
							<td><span id="sideMenuButton"
								class="[ glyphicon glyphicon-chevron-down ] controller-open"></span>
								<span class="[ glyphicon glyphicon-remove ] controller-close"></span>
							</td>
						</tr>
						<tr>
							<td><h4>Menu</h4></td>
						</tr>
					</tbody>
				</table>

			</div>
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
					<div id="sourceCodeTree" class="sourceCodeTree"></div>
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
						<!--
						<tr>
							<td colspan="2">
								<div id="droppableSample" class="ui-widget-content">
									<p>Drop something here</p>
								</div>
							</td>
						</tr>						
						<tr>
							<td>
								<div>
									<select id=" targetSelect" class=" targetSelect"></select>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<ul id="list-group targetPathList"
										class="list-group targetPathList"></ul>
								</div>
							</td>
						</tr>
						 -->
					</tbody>
				</table>
			</div>
			<div class="col-sm-5 col-lg-5 col-mx-5">
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
					<div id="targetCodeTree" class="targetCodeTree"></div>
				</div>
			</div>
		</div>
		<div id="footer">
			<div class="container"></div>
		</div>
	</div>
</body>

<script src="js/libs/jquery/jquery.js" type="text/javascript"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"
	type="text/javascript"></script>
<script src="js/libs/jquery-ui/jquery-ui.custom.js"
	type="text/javascript"></script>
<script src="js/libs/jquery-cookie/jquery.cookie.js"
	type="text/javascript"></script>
<script src="js/libs/Dynatree/jquery.dynatree.js" type="text/javascript"></script>
<!-- <script src="js/libs/EasyTree/jquery.easytree.js"></script>  -->
<script src="js/menu.js" type="text/javascript"></script>
<script src="js/main.js" type="text/javascript"></script>

</ html>