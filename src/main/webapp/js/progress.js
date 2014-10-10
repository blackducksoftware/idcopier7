/**
 * GLOBALS
 */
var previousPercent = -1;
// Project currently selected
var projectId;
// Target Progress Bar
var targetProgressBar;
/**
 * Loader 
 */
jQuery(document).ready(function()
{
	targetProgressBar = $('#targetProgressBar').progressbar({
		display_text: 'none',
		use_percentage: true
	});
	targetProgressBar.attr('data-transitiongoal', 100).progressbar();	
		
	
	/**
	 * Performs a refresh on target project
	 */
	$("#performRefreshButton").on('click', function() {
		targetProgressBar.attr('data-transitiongoal', 0).progressbar();	
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
		},
		success: function(msg) 
		{
			console.log("Refresh finished with success");		
			// Grab the status
			getRefreshStatusForProject(targetServer, targetProjectId);
			// Reload the tree
			refreshNodes("target");
		}
	});

}

/**
 * Gets back the current status of hte refresh
 */
function getRefreshStatusForProject(targetServer, targetProjectId) 
{
	projectId = targetProjectId;
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
	}
	else
	{	
		previousPercent = percentComplete;
		console.log('Percent Complete: '+percentComplete);
		targetProgressBar.attr('data-transitiongoal', percentComplete).progressbar({
			display_text: 'fill'
		});
		// progressLoader.setValue(percentComplete + '%' + " [" + refreshStage + "]");
	}
	
	if (percentComplete < 100) 
	{
		getRefreshStatusForProject(server, projectId);
	} else {
		previousPercent = -1;
		targetProgressBar.attr('data-transitiongoal', 100).progressbar({
		display_text: 'none'
	});
		//updateCodeTreeNodes('target', server, projectId);
	}
}