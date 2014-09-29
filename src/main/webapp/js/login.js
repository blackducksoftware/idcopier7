$(function() {
	var errorMessage = document.getElementById('errorLoginMsg').innerHTML;

	if (errorMessage != '') {
		$.growl({
			title : '<h4>Login Error</h4>',
			message : errorMessage
		}, {
			type : 'danger',
			placement : {
				from : "center",
				align : "center"
			},
			icon_type : 'class'
		});
	}
});
