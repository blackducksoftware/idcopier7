jQuery(document).ready(function() {
	$.ajax({
		url : 'myprojects',
		success : function(data) {
			alert(data);
		}
	});
});