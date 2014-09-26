jQuery(document).ready(function() {
	$.growl({
		icon : 'glyphicon glyphicon-warning-sign',
		title : '<strong>SAVING:</strong> ',
		message : 'Do not close this page'
	});
	/*
	 * $('#submitButton').click(function(e) { var serverName = $('#serverURI').val(); var userName = $('#userName').val(); var password = $('#password').val();
	 * if (serverName != '' && userName != '' && password != '') { $.bootstrapGrowl("This is a test."); } else { setTimeout(function() {
	 * $.bootstrapGrowl("Danger, Danger!", { type : 'danger', align : 'center', width : 'auto', allow_dismiss : false }); }, 2000); } e.preventDefault(); var l =
	 * Ladda.create(this); l.start(); $("#loginForm").submit(function(event) { }); });
	 */
});