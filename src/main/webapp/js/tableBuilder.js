/*
 * IDCopier
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var dataTableData = null;
var showOnlyComments = true;
var table;

jQuery(document).ready(
		function() {
			$("#onlyShowComponentsWithCommentsCheckBox").change(
					function() {
						showOnlyComments = $(
								'#onlyShowComponentsWithCommentsCheckBox')
								.prop('checked');
						if (dataTableData === null)
							return;

						setBOMData(dataTableData);
					});
			$("#selectAllButton").click(function() {
				console.log("Selecting all components");
				alterTableCheckBoxes(true);
			});
			$("#clearSelectionButton").click(function() {
				console.log("Unselecting all components");
				alterTableCheckBoxes(false);
			});
		});

function alterTableCheckBoxes(value) {
	$('.editor-active').each(function() {
		this.checked = value;
	});
}

function setBOMData(billOfMaterials) {
	dataTableData = billOfMaterials;

	var bomDataSet = [];
	var bomItem;
	$.each(JSON.parse(billOfMaterials), function(index, currentBomItem) {
		var componentName = new String(currentBomItem.componentName);
		var componentId = new String(currentBomItem.componentId);
		var versionName = new String(currentBomItem.versionName);
		var versionId = new String(currentBomItem.versionId);
		var comment = new String(currentBomItem.comment);
		var uniqueID = new String(currentBomItem.uniqueID);

		bomItem = [ true, componentName, componentId, versionName, versionId,
				comment, uniqueID ];

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
	console.log("Building BOM table with the comment filter set to "
			+ showOnlyComments);
	table = $('#sourceProjectComponentTable')
			.dataTable(
					{
						"multipleSelection" : true,
						"destroy" : true,
						"scrollY" : 250,
						"searching" : true,
						"ordering" : true,
						"paging" : false,
						"bInfo" : true,
						"data" : dataSet,
						"fnInitComplete" : function() {
							this.css("visibility", "visible");
						},
						"columns" : [
								{
									"title" : "Select",
									className : "center-horizontal",
									"width" : "5%",
									render : function(data, type, row) {
										return '<input type="checkbox" class="editor-active" id="'
												+ row[6] + '">';
									}
								},
								{
									"title" : "Component",
									className : "center-horizontal",
									"width" : "20%"
								},
								{
									"title" : "Component ID",
									"visible" : false
								},
								{
									"title" : "Version",
									className : "center-horizontal",
									"width" : "15%"
								},
								{
									"title" : "Version ID",
									"visible" : false
								},
								{
									"title" : "Comment",
									"width" : "30%",
									'fnCreatedCell' : function(nTd, sData,
											oData, iRow, iCol) 
									{
										// This is used by the tooltip
										// Places the value of the column into the title
					            		nTd.title =  oData[iCol];
			
									},
									render : function(data, type, row) {
										return '<div class="truncate">' + data + "</div>";
									}
								}, {
									"title" : "Unique ID",
									"visible" : false
								} ]
					});

	// Tooltip 
    var tdElements = table.$('td');
    if(tdElements != null && tdElements.length > 0)
	{
    	tdElements.smallipop(
		{
	        popupYOffset: 2, // Bubble has a 20px vertical offset
	        popupDistance: 2, // Bubble travels vertically 
	        popupOffset: 0, // No horizontal offset
	        theme: 'blue fat-shadow',
	        preferredPosition: "right",
	        popupAnimationSpeed: "100"
		 });	
	}
	
	alterTableCheckBoxes(true);
	return table;
}

function getTableData() {
	// Gets the selected inputs only.
	var selectedIDs = [];
	$('#sourceProjectComponentTable input[type=checkbox]:checked').each(
			function() {
				var checkedId = this.id;
				selectedIDs.push(checkedId);

				console.log("ID selected: " + checkedId);
			});

	return selectedIDs;
	console.log(data);
}