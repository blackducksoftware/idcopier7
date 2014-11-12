var dataTableData = null;
var showOnlyComments = true;
var table;

jQuery(document).ready(
	function () {
	$("#onlyShowComponentsWithCommentsCheckBox").change(function () {
		showOnlyComments = $('#onlyShowComponentsWithCommentsCheckBox').prop('checked');
		if (dataTableData === null)
			return;

		setBOMData(dataTableData);
	});
	$("#selectAllButton").click(function () {
		console.log("Selecting all components");
		alterTableCheckBoxes(true);
	});
	$("#clearSelectionButton").click(function () {
		console.log("Unselecting all components");
		alterTableCheckBoxes(false);
	});
});

function alterTableCheckBoxes(value) {
	$('.editor-active').each(function () {
		this.checked = value;
	});
}

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
		var uniqueID = new String(currentBomItem.uniqueID);
		
		bomItem = [true, componentName, componentId, versionName, versionId, comment, uniqueID];

		if (!showOnlyComments) {
			bomDataSet.push(bomItem);
		} else if (comment.trim().length > 0) {
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
	table = $('#sourceProjectComponentTable').dataTable({
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
						return '<input type="checkbox" class="editor-active" id="' + row[6] + '">';
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
				, {
					"title" : "Unique ID",
					"visible": false					
				}
			]
		});

	alterTableCheckBoxes(true);
	
}

function getTableData() {
	// Gets the selected inputs only.
	var selectedIDs = [];
	$('#sourceProjectComponentTable input[type=checkbox]:checked').each(function() 
	{ 
		var checkedId = this.id;
		selectedIDs.push(checkedId);

		console.log("ID selected: " + checkedId);
	});
	

	return selectedIDs;
	console.log(data);
}