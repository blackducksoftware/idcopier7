$(function() {
	$('.submitButton').click(function(e) {
		e.preventDefault();
		var l = Ladda.create(this);
		l.start();
		$.post("your-url", {
			data : data
		}, function(response) {
			console.log(response);
		}, "json").always(function() {
			l.stop();
		});
		return false;
	});
});