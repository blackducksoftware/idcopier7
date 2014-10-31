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
			data : "active",
			render : function(data, type, row) {
				if (type === 'display') {
					return '<input type="checkbox" class="editor-active">';
				}
				return data;
			},
			className : "dt-body-center"
		}, {
			"title" : "Component",
			"width" : "20%"

		}, {
			"title" : "Version",
			"width" : "15%"
		}, {
			"title" : "Comment"
		} ]
	});
}