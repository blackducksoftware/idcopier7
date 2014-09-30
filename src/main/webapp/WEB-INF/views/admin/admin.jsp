
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Protex ID Copier | Admin</title>

<link rel="shortcut icon" href="images/favicon.png" type="image/png" />
<link rel="stylesheet" href="css/admin.css">
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/justified-nav.css">
<link rel="stylesheet" href="css/DataTables/jquery.dataTables.css">
</head>
<body>
	<div class="main">
		<div class="header-logo">
			<img src="images/tempIcon.gif">
		</div>
		<hr>
		<div class="container">
			<div class="well">
				<div class="row input-spacing">
					<div class="input-group">
						<input type="text" class="form-control" id="newServerInput"
							name="newServerInput" placeholder="Add Protex Server"> <span
							class="input-group-btn">
							<button class="btn btn-primary" type="button">Add Server</button>
						</span>
					</div>
				</div>
			</div>
			<div class="well">
				<div class="row input-spacing">
					<table id="serverConfigurationTable"
						class="display hover row-border" cellspacing="0" width="100%">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/libs/DataTables/jquery.dataTables.js"></script>
<script src="js/admin.js"></script>