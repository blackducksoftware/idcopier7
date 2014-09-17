
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<head>
<title>Black Duck | IdCopier</title>

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

		<br />
		<div class="container">
			<form class="well" method="POST" commandName="protex-server"
				action="login.do">
				<legend> Please Login </legend>
				<fieldset>
					<!-- Server input-->
					<div class="form-group">
						<label control-label" for="serverName">Server:</label>
						<div>
							<input id="serverName" name="serverName" placeholder="Server"
								class="form-control input-md" required="" type="text"
								path="serverName">
						</div>
					</div>

					<!-- Username input-->
					<div class="form-group">
						<label class="control-label" for="userName">Username:</label>
						<div>
							<input id="userName" name="userName" placeholder="Username"
								class="form-control input-md" required="" type="text"
								path="userName">

						</div>
					</div>

					<!-- Password input-->
					<div class="form-group">
						<label class="control-label" for="password">Password:</label>
						<div>
							<input id="password" name="password" placeholder="Password"
								class="form-control input-md" required="" type="password"
								path="password">

						</div>
					</div>

					<!-- Submit Button -->
					<div class="form-group">
						<label class="control-label" for="submitButton"></label>
						<div>
							<a href="#myModal">
								<button id="submitButton" name="submitButton"
									class="btn btn-primary pull-right" data-toggle="modal">Submit</button>
							</a>
						</div>
					</div>
				</fieldset>
			</form>
		</div>

		<div id="loginModal" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">Logging in user...</h4>
					</div>
					<div class="modal-body">
						<p>Loader will go here...</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="js/libs/jquery/jquery.min.js"></script>
<script src="js/libs/twitter-bootstrap/js/bootstrap.js"></script>