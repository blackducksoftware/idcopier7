
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Protex ID Copier | Login</title>

<link rel="shortcut icon" href="images/favicon.png" type="image/png" />
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/justified-nav.css">
<link rel="stylesheet" href="css/Ladda/ladda-themeless.css">
<link rel="stylesheet" href="css/Animate/animate.min.css">

</head>
<body>
	<div class="main">
		<div class="header-logo">
			<img src="images/tempIcon.png">
		</div>
		<hr>
		<div class="container center-on-screen">
			<form id="loginForm" class="well" method="POST"
				commandName="protex-server" action="login.do" role="form">
				<legend> Please Login </legend>
				<fieldset>

					<!-- Username input-->
					<div class="form-group">
						<label class="control-label" for="userName">Username:</label>
						<div>
							<input id="userName" name="userName" placeholder="Username"
								class="form-control input-md" required="" type="text"
								path="userName" value=${server.userName}>

						</div>
					</div>

					<!-- Password input-->
					<div class="form-group">
						<label class="control-label" for="password">Password:</label>
						<div>
							<input id="password" name="password" placeholder="Password"
								class="form-control input-md" required="" type="password"
								path="password" value=${server.password}>

						</div>
					</div>

					<!-- Remember Me Option -->
					<div class="form-group">
						<div class="checkbox">
							<label><input type="checkbox" id="remember-cookies"
								name="remember-cookies">Remember Me</label>
						</div>
					</div>

					<!-- Submit Button -->
					<div class="form-group">
						<label class="control-label" for="submitButton"></label>
						<div>
							<button id="submitButton" name="submitButton"
								class="btn btn-primary pull-right ladda-button"
								data-style="zoom-in">
								<span class="ladda-label">Submit</span>
							</button>
							<!-- <label id="spinner"></label>  -->
						</div>
					</div>
				</fieldset>
			</form>
			<div id="errorLoginMsg" class="errorLoginMsg">${loginErrorMsg}</div>

		</div>
	</div>
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
</body>

<script src="js/libs/jquery/jquery.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>
<script src="js/libs/Ladda/spin.js"></script>
<script src="js/libs/Ladda/ladda.js"></script>
<script src="js/libs/bootstrap-growl/bootstrap-growl.min.js"></script>
<script src="js/login.js"></script>
</html>