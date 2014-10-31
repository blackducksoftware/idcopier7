/**
 * Builds DataTable for the admin page
 * 
 * @param location
 * @param dataSet
 */
function buildBomDataTable(dataSet) {
	$('#sourceProjectComponentTable').dataTable({
		"paging" : false,
		"bInfo" : false,
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