/**
 * Builds DataTable for the admin page
 * 
 * @param location
 * @param dataSet
 */
function buildBomDataTable(dataSet) {
	$('#sourceProjectComponentTable').dataTable({
		"multipleSelection" : true,
		"retrieve" : true,
		"searching" : true,
		"ordering" : true,
		"paging" : true,
		"bInfo" : true,
		"data" : dataSet,
		"columns" : [ {
			"title" : "Select",
			render : function(data, type, row) {
				if (type === 'display') {
					return '<input type="checkbox" class="editor-active" checked>';
				}
				return data;
			},
			className : "center-content",
			"width" : "70px"
		}, {
			"title" : "Component",
			className : "center-content",
			"width" : "20%"

		}, {
			"title" : "Version",
			className : "center-content",
			"width" : "15%"
		}, {
			"title" : "Comment"
		} ]
	});
}