/**
 * Builds DataTable for the admin page
 * 
 * @param location
 * @param dataSet
 */
function buildBomDataTable(dataSet) {
	$('#sourceProjectComponentTable').dataTable({
		"retrieve" : true,
		"searching" : true,
		"ordering" : true,
		"paging" : true,
		"bInfo" : true,
		"data" : dataSet,
		"columns" : [ {
			"title" : "Component"
		}, {
			"title" : "Version"
		}, {
			"title" : "Comment"
		} ]
	});
}