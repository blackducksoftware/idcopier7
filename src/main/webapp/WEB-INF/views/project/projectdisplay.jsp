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
<!-- <link rel="stylesheet" href="css/Dynatree/skin-vista/ui.dynatree.css"> -->
<link rel="stylesheet"
	href="css/Fancytree/skin-bootstrap/ui.fancytree.css">
<link rel="stylesheet" href="css/justified-nav.css">
<link rel="stylesheet" href="css/menu.css">
<link rel="stylesheet" href="css/Animate/animate.min.css">

</head>
<body>
	<div class="main">
		<div class="header-logo">
			<img src="images/tempIcon.gif">
		</div>
		<div>
			<nav class="animate">
				<h1>Menu</h1>
				<div class="divider"></div>

				<ul>
					<li><a href="log" target="_blank"><h4>
								View Log <span class="glyphicon glyphicon-list-alt"></span>
							</h4> </a></li>
					<li><a href="admin" target="_blank"><h4>
								Admin <span class="glyphicon glyphicon-cog"></span>
							</h4> </a></li>
				</ul>

				<div class="divider"></div>
				<ul>
					<li><a href="login" target="_blank"><h4>
								Logout <span class="glyphicon glyphicon-user"></span>
							</h4> </a></li>
				</ul>
			</nav>
			<div class="nav-controller">
				<table>
					<tbody>
						<tr>
							<td><span
								class="[ glyphicon glyphicon-align-justify ] controller-open"></span>
							</td>
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
					<div id="sourceCodeTree" data-source="ajax" class="sourceCodeTree"></div>
				</div>
			</div>
			<div class="col-sm-2 col-lg-2">
				<table class="table">
					<tbody>
						<tr>
							<td><h4>Defer BOM refresh</h4></td>
							<td><input id="deferBomRefreshCheckBox"
								name="deferBomRefreshCheckBox" class="checkbox" type="checkbox"
								value="deferBomRefreshCheckBox" checked /></td>
						</tr>
						<tr>
							<td><h4>Recursive</h4></td>
							<td><input id="recursiveCopyCheckBox"
								name="recursiveCopyCheckBox" class="checkbox" type="checkbox"
								value="recursiveCopy" checked /></td>
						</tr>
						<tr>
							<td><h4>Overwrite Identifications</h4></td>
							<td><input id="overwriteIDsCheckBox"
								name="overwriteIDsCheckBox" class="checkbox" type="checkbox" /></td>
						</tr>
						<tr>
							<td colspan="2"><input type="button" id="submitCopyButton"
								value="Perform Copy" class="btn btn-primary perform-copy-button" /></td>
						</tr>
						<tr>
							<td colspan="2"><input type="button"
								id="performRefreshButton" value="Refresh BOM"
								class="btn btn-primary perform-refresh-button" /></td>
						</tr>
						<tr>
							<td colspan="2">
								<!--  Progress Bar For Refresh -->
								<center>
									<div id="progressBar" class="easyui-progressbar"></div>
								</center>
							</td>
						</tr>
						<tr>
							<td></td>
							<td></td>
						</tr>
						<!-- 
						<tr>
							<td><h4></h4></td>
							<td></td>
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
	</div>
</body>

<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/libs/jquery-ui/jquery-ui.custom.js"></script>
<script src="js/libs/progress/p-loader.js"></script>
<script src="js/libs/jquery-cookie/jquery.cookie.js"></script>
<!-- <script src="js/libs/Dynatree/jquery.dynatree.js"></script> -->
<script src="js/libs/Fancytree/jquery.fancytree.js"></script>
<script src="js/libs/Fancytree/jquery.fancytree.glyph.js"></script>
<script src="js/libs/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>
<script src="js/libs/bootstrap-growl/bootstrap-growl.min.js"></script>
<!-- <script src="js/tree.js"></script> -->
<script src="js/treeFancy.js"></script>
<script src="js/menu.js"></script>
<script src="js/main.js"></script>
<script src="js/progress.js"></script>

</ html>