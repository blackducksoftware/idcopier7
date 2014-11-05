var dataTableData = null;
var showOnlyComments = true;

jQuery(document).ready(
	function () {
	$("#onlyShowComponentsWithCommentsCheckBox").change(function () {
		showOnlyComments = $('#onlyShowComponentsWithCommentsCheckBox').prop('checked');
		if (dataTableData === null)
			return;

		setBOMData(dataTableData);
	});
});

function setBOMData(billOfMaterials) {
	dataTableData = billOfMaterials;

	var bomDataSet = [];
	var bomItem;
	$.each(JSON.parse(billOfMaterials), function (index, currentBomItem) {
		var componentName = new String(currentBomItem.componentName);
		var componentId = new String(currentBomItem.componentId);
		var versionName = new String(currentBomItem.versionName);
		var versionId = new String(currentBomItem.versionId);
		var comment = new String(currentBomItem.comment);

		bomItem = [true, componentName, componentId, versionName, versionId, comment];

		if (!showOnlyComments) {
			bomDataSet.push(bomItem);
		}else if (comment.trim().length > 0){
			bomDataSet.push(bomItem);
		}
	});

	buildBomDataTable(bomDataSet);
}

/**
 * Builds DataTable for the admin page
 *
 * @param location
 * @param dataSet
 */
function buildBomDataTable(dataSet) {
	console.log("Building BOM table with the comment filter set to " + showOnlyComments);
	$('#sourceProjectComponentTable').dataTable({
		"multipleSelection" : true,
		"destroy" : true,
		"scrollY" : 250,
		"searching" : true,
		"ordering" : true,
		"paging" : false,
		"bInfo" : true,
		"data" : dataSet,
		"columns" : [{
				"title" : "Select",
				className : "center-horizontal",
				"width" : "40px",
				render : function (data, type, row) {
					return '<input type="checkbox" class="editor-active" checked>';
				}
			}, {
				"title" : "Component",
				className : "center-horizontal",
				"width" : "20%"
			}, {
				"title" : "Component ID",
				"visible" : false
			}, {
				"title" : "Version",
				className : "center-horizontal",
				"width" : "15%"
			}, {
				"title" : "Version ID",
				"visible" : false
			}, {
				"title" : "Comment"
			}
		]
	});
}
