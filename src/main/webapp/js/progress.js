/**
 * GLOBALS
 */
var progressDiv = $("#progressBar");
var previousPercent = -1;
// Map of progress loaders
var progressMap = {};
// Project currently selected
var projectId;
/**
 * Loader 
 */
jQuery(document).ready(function()
{
	progressDiv.hide();

	/**
	 * Performs a refresh on target project
	 */
	$("#performRefreshButton").on('click', function() {
		
		var targetServer = $('.selectTargetServer').children(":selected").text();
		var targetProjectId = $('.selectTargetProject').children(":selected").attr("id");
		var partialBOMOption = $('#partialBOMCheckBox').is(':checked');
		if (targetProjectId == null) {
			displayNotificationMessage(warning, 'Unable to refresh BOM', 'Please select a target project!');
			return false;
		}
		performBOMRefresh(targetServer, targetProjectId, partialBOMOption);
	});
});

/**
 * Calls IDCProjectController and performs a BOM refresh against a 
 * specific server/project
 */
function performBOMRefresh(targetServer, targetProjectId, partialBOMOption)
{
	var refreshPath = 'bomRefresh' + '/' + targetServer + '/' + targetProjectId;
	console.log("Submitting AJAX request to: " + refreshPath);

	var params = 
	{
		'partial-bom-option' : partialBOMOption	
	};
	// Submit the refresh request
	$.ajax({
		type : 'POST',
		url : refreshPath,
		data: params,
		error : function(msg) {
			console.log("Error submitting refresh: " + msg.statusText);
		}
	});
	// Grab the status
	getRefreshStatusForProject(targetServer, targetProjectId);
}

/**
 * Gets back the current status of hte refresh
 */
function getRefreshStatusForProject(targetServer, targetProjectId) 
{
	projectId = targetProjectId;
	progressDiv.show();
	var statusPath = 'bomRefreshStatus' + '/' + targetServer + '/' + projectId;
	$.ajax({
		type : 'GET',
		url : statusPath,
		success : function(jsonStatusResult) {
			console.log('Status result: ' + jsonStatusResult);
			updateRefreshProgress(jsonStatusResult, targetServer, projectId);
		},
		error : function(msg) {
			console.log(msg);
		}
	});
}

/**
 * Private internal function that is used to poll back results for refresh
 */
function updateRefreshProgress(jsonStatusString, server) 
{
	/**
	 * Get the progress loader per project Id
	 * If empty, create a new one
	 */
	progressLoader = progressMap[projectId];
	if(progressLoader == null)
	{
		progressLoader = createProgresLoader();
		progressMap[projectId] = progressLoader;
	}
	progressLoader.show();
	/**
	 * Parse the return JSON
	 */
	var jsonObj = JSON.parse(jsonStatusString);
	if (jsonObj == null) {
		console.log("WARN: No JSON status information");
		return false;
	}
	var percentComplete = jsonObj.percentComplete;
	var refreshStage = jsonObj.refreshStage;
	
	if (percentComplete === 100 && previousPercent === -1) 
	{
		var selectedProject = $('.selectTargetProject').children(":selected").text();
		progressLoader.setValue('Done: ' + selectedProject);
		progressLoader.setProgress(1);
	}
	else
	{	
		previousPercent = percentComplete;
		progressLoader.setValue(percentComplete + '%' + " [" + refreshStage + "]");
		progressLoader.setProgress(percentComplete / 100);
	}
	
	if (percentComplete < 100) 
	{
		getRefreshStatusForProject(server, projectId);
	} else {
		previousPercent = -1;
		//updateCodeTreeNodes('target', server, projectId);
	}
}

function createProgresLoader()
{
	// Clear out the div, as we will store the progress bars internally.
	progressDiv.empty();
	var progressLoader = progressDiv.percentageLoader({
		width : 140,
		height : 140,
		progress : 0,
		value : '0%'
	})
	progressLoader.hide();
	return progressLoader;
}