jQuery(document).ready(function() {
	$.ajax({
		url : 'servers',
		success : function(data) {
			alert(data);
		}
	});
});