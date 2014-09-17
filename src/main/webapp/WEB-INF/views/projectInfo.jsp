<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Black Duck | IDCopier</title>

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
								<td><h4>Server</h4></td>
								<td><select id="selectSourceServer"
									name="selectSourceServer" class="form-control">
										<option value="1">https://server-one.blackducksoftware.com</option>
										<option value="2">https://server-two.blackducksoftware.com</option>
								</select></td>
							</tr>
							<tr>
								<td><h4>Project</h4></td>
								<td><select id="selectSourceProject"
									name="selectSourceProject" class="form-control">
										<option value="1">Project 1</option>
										<option value="2">Project 2</option>
								</select></td>
							</tr>
						</tbody>
					</table>

					<h3>Project ID: ${projectId}</h3>
					<br /> Project JSON: ${jsonTree}
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
								<td><h4>Server</h4></td>
								<td><select id="selectDestinationServer"
									name="selectDestinationServer" class="form-control">
										<option value="1">https://server-one.blackducksoftware.com</option>
										<option value="2">https://server-two.blackducksoftware.com</option>
								</select></td>
							</tr>
							<tr>
								<td><h4>Project</h4></td>
								<td><select id="selectDestinationProject"
									name="selectDestinationProject" class="form-control">
										<option value="1">Project 1</option>
										<option value="2">Project 2</option>
								</select></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="js/libs/jquery/jquery.min.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
</html>

