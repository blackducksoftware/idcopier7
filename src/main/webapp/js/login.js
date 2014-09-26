$(function() {
	/*
	$('#submitButton').click(function(e) {
		var serverName = $('#serverURI').val();
		var userName = $('#userName').val();
		var password = $('#password').val();
		if (serverName != '' && userName != '' && password != '') {
			$.bootstrapGrowl("This is a test.");
		} else {
			setTimeout(function() {
				$.bootstrapGrowl("Danger, Danger!", {
					type : 'danger',
					align : 'center',
					width : 'auto',
					allow_dismiss : false
				});
			}, 2000);
		}
		e.preventDefault();
		var l = Ladda.create(this);
		l.start();
		$("#loginForm").submit(function(event) {
		});
	});
	/*
	 * $.bootstrapGrowl("This is a test."); setTimeout(function() { $.bootstrapGrowl("This is another test.", { type : 'success' }); }, 1000);
	 * setTimeout(function() { $.bootstrapGrowl("Danger, Danger!", { type : 'danger', align : 'center', width : 'auto', allow_dismiss : false }); }, 2000);
	 * setTimeout(function() { $.bootstrapGrowl("Danger, Danger!", { type : 'info', align : 'left', stackup_spacing : 30 }); }, 3000);
	 */
});