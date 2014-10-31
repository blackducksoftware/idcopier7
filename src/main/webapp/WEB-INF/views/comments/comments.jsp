<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Protex ID Copier | Copy Comments</title>

<link rel="shortcut icon" href="images/favicon.png" type="image/png" />
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/justified-nav.css">
<link rel="stylesheet" href="css/menu.css">
<link rel="stylesheet" href="css/Animate/animate.min.css">
<link rel="stylesheet" href="css/HubSpot-messenger/messenger.css">
<link rel="stylesheet"
	href="css/HubSpot-messenger/messenger-theme-air.css">
<link rel="stylesheet" href="css/DataTables/jquery.dataTables.css">

</head>
<body>
	<div class="main">
		<div>
			<div class="session-details">
				<table>
					<tbody>
						<tr>
							<td class="username-data"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="header-logo">
				<img src="images/tempIcon.png">
			</div>
		</div>
		<nav class="animate">
			<h1>Menu</h1>
			<div class="divider"></div>
			<ul>
				<li><a href="log" target="_blank"><h4>
							View Log <span class="glyphicon glyphicon-list-alt"></span>
						</h4> </a></li>
			</ul>
			<div class="divider"></div>
			<ul>
				<li><a href="login" id="logoutButton"><h4>
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
			<div class="well">
				<div class="projects-header-buffer">
					<p class="header-text">Source Project</p>
				</div>
				<div class="progress-bar-section">
					<div class="refresh-progress-button ">
						<div class="btn-group pull-right btn-spacer">
							<button type="button"
								class="btn btn-primary dropdown-toggle btn-xs"
								data-toggle="dropdown">
								Refresh <span class="caret"></span>
							</button>
							<ul class="rp-dropdown-menu dropdown-menu" role="menu">
								<li><a href="#"
									onclick="activateRefreshFromPullDown(source, partialRefresh)">Partial</a></li>
								<li><a href="#"
									onclick="activateRefreshFromPullDown(source, fullRefresh)">Full</a></li>
							</ul>
						</div>
					</div>

					<div class="progress">
						<div id="sourceProgressBar" class="progress-bar"
							role="progressbar" data-transitiongoal="100"></div>
					</div>
				</div>

				<table class="table">
					<tbody>
						<tr>
							<td><select class="form-control selectSourceCommentServer"
								title="Select Source Server">
							</select></td>
						</tr>
						<tr>
							<td><select id="selectSourceCommentProject"
								name="selectSourceCommentProject"
								class="form-control selectSourceCommentProject">
									<option value="1">No Projects</option>
							</select></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="col-sm-2 col-lg-2">
			<table class="table">
				<tbody>
					<tr>
						<td><input type="button" id="displayBomButton"
							value="Display Bill of Materials"
							class="btn btn-primary center-button" /></td>
					</tr>
					<tr>
						<td><input type="button" id="copyCommentsButton"
							value="Copy Comments" class="btn btn-success center-button" /></td>
					</tr>
				</tbody>
			</table>
			<div class="version-info">
				<table>
					<tbody>
						<tr>
							<td class="bold">Version:</td>
							<td class="version-data">${display_version}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="col-sm-5 col-lg-5 col-mx-5">
			<div class="well">
				<div class="projects-header-buffer">
					<p class="header-text">Target Project</p>
				</div>
				<div class="progress-bar-section">
					<div class="refresh-progress-button ">
						<div class="btn-group pull-right btn-spacer">
							<button type="button"
								class="btn btn-primary dropdown-toggle btn-xs"
								data-toggle="dropdown">
								Refresh <span class="caret"></span>
							</button>
							<ul class="rp-dropdown-menu dropdown-menu" role="menu">
								<li><a href="#"
									onclick="activateRefreshFromPullDown('target', partialRefresh)">Partial</a></li>
								<li><a href="#"
									onclick="activateRefreshFromPullDown('target', fullRefresh)">Full</a></li>
							</ul>
						</div>
					</div>
					<div class="progress">
						<div id="targetProgressBar" class="progress-bar"
							role="progressbar" data-transitiongoal="100"></div>
					</div>
				</div>
				<table class="table">
					<tbody>
						<tr>
							<td><select class="form-control selectTargetCommentServer"
								id="selectTargetCommentServer" name="selectTargetCommentServer">
							</select></td>
						</tr>
						<tr>
							<td><select id="selectTargetCommentProject"
								name="selectTargetCommentProject"
								class="form-control selectTargetCommentProject">
									<option value="1">No Projects</option>
							</select></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="col-sm-12 col-lg-12 col-mx-12">
			<div class="well">
				<div class="row input-spacing">
					<table id="sourceProjectComponentTable" class="display"
						cellspacing="0" width="100%">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>

<!--  JS Libraries -->
<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/libs/jquery-ui/jquery-ui.custom.js"></script>
<script src="js/libs/jquery-cookie/jquery.cookie.js"></script>
<script src="js/libs/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>
<script src="js/libs/HubSpot-messenger/messenger.js"></script>
<script src="js/libs/HubSpot-messenger/messenger-theme-future.js"></script>
<script src="js/libs/bootstrap-progressbar/bootstrap-progressbar.js"></script>
<script src="js/libs/DataTables/jquery.dataTables.js"></script>
<!--  ID Copier JS files -->
<script src="js/menu.js"></script>
<script src="js/progress.js"></script>
<script src="js/tableBuilder.js"></script>
<script src="js/comments.js"></script>
</ html>