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

		bomItem = [true, componentName, componentId, versionName, versionId, comment];

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
						return '<input type="checkbox" class="editor-active" id="' + row[2] + '">';
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

	alterTableCheckBoxes(true);
}

function getTableData() {
	// Gets the selected inputs only.
	var data = table.$('input, select');

	for (i = 0; i < data.length; i++) 
	{
		var selectedIDs = [];
		var currentBomItem = data[i];
		
		var id = currentBomItem.id;
		selectedIDs[i] = id;
//		var selected = currentBomItem[0];
//		var componentName = currentBomItem[1].toString();
//		var componentId = currentBomItem[2].toString();
//		var versionName = currentBomItem[3].toString();
//		var versionId = currentBomItem[4].toString();
//		var comment = currentBomItem[5].toString();

		//console.log(selected + " - " + componentName + " - " + componentId + " - " + versionName + " - " + versionId + " - " + comment);
		// console.log(" > " + table.cell(i, 0).data());
		console.log("ID selected: " + id);
	}
	return selectedIDs;
	console.log(data);
}