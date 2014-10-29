/**
 * GLOBALS
 */

var partialRefresh = "partialRefresh";
var fullRefresh = "fullRefresh";

var previousSourcePercent = -1;
var previousTargetPercent = -1;
// Project currently selected
var sourceProjectId;
var targetProjectId;
/**
 * Progress Bars
 */
// Source Progress Bar
var sourceProgressBar;
// Target Progress Bar
var targetProgressBar;
/**
 * Loader
 */
jQuery(document).ready(function() {
	
	// Initialize the progress bars
	// TODO: Investigate if this is even necessary.
	sourceProgressBar = $('#sourceProgressBar').progressbar({
		display_text : 'fill',
		use_percentage : true
	});
	sourceProgressBar.attr('data-transitiongoal', 100).progressbar();

	targetProgressBar = $('#targetProgressBar').progressbar({
		display_text : 'fill',
		use_percentage : true
	});
	targetProgressBar.attr('data-transitiongoal', 100).progressbar();

});

/**
 * This is called by the pulldown button
 * @param location
 * @param refreshType
 */
function activateRefreshFromPullDown(location, refreshType)
{
	var usePartial = false;
	if(refreshType == partialRefresh)
		usePartial = true;
	
	var serverName = $('.select' + location + 'Server').children(":selected").text();
	var projectId = $('.select' + location + 'Project').children(":selected").attr("id");
	
	console.log("Activating refresh, server: " + serverName + " project: " + projectId + " location: " + location);
	
	if (projectId == null) {
		displayNotificationMessage(error, 'Unable to refresh BOM', 'Please select a project!');
		return false;
	}
	
	var progressBar = $("#" + location + "ProgressBar");
	progressBar.attr('data-transitiongoal', 0).progressbar();
	
	performBOMRefresh(location, serverName, projectId, usePartial);
}


/**
 * Calls IDCProjectController and performs a BOM refresh against a specific server/project
 */
function performBOMRefresh(location, server, projectId, partialBOMOption) {
	var refreshPath = 'bomRefresh' + '/' + server + '/' + projectId;
	console.log("Submitting AJAX request to: " + refreshPath);
	var params = {
		'partial-bom-option' : partialBOMOption
	};
	// Submit the refresh request
	$.ajax({
		type : 'POST',
		url : refreshPath,
		data : params,
		error : function(msg) {
			console.log("Error submitting refresh: " + msg.statusText);
		},
		success : function(msg) {
			console.log("Refresh finished with success");
			// Grab the status
			if(location == source)
				getRefreshStatusForSourceProject(server, projectId);
			else
				getRefreshStatusForTargetProject(server, projectId);
			// Reload the tree
			refreshNodes(location);
		}
	});
}

/**
 * Gets back the current status of the refresh
 */
function getRefreshStatusForSourceProject(server, projectId) {
	sourceProjectId = projectId;
	var statusPath = 'bomRefreshStatus' + '/' + server + '/' + projectId;
	$.ajax({
		type : 'GET',
		url : statusPath,
		success : function(jsonStatusResult) {
			console.log('Status result: ' + jsonStatusResult);
			updateSourceRefreshProgress(jsonStatusResult, server, projectId);
		},
		error : function(msg) {
			console.log(msg);
		}
	});
}
/**
 * Gets back the current status of the refresh
 */
function getRefreshStatusForTargetProject(server, projectId) {
	targetProjectId = projectId;
	var statusPath = 'bomRefreshStatus' + '/' + server + '/' + projectId;
	$.ajax({
		type : 'GET',
		url : statusPath,
		success : function(jsonStatusResult) {
			console.log('Status result: ' + jsonStatusResult);
			updateTargetRefreshProgress(jsonStatusResult, server, projectId);
		},
		error : function(msg) {
			console.log(msg);
		}
	});
}
/**
 * Private internal function that is used to poll back results for refresh
 */
function updateSourceRefreshProgress(jsonStatusString, server) {
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
	if (percentComplete === 100 && previousSourcePercent === -1) {
		var selectedProject = $('.selectTargetProject').children(":selected").text();
	} else {
		previousSourcePercent = percentComplete;
		console.log('Percent Complete: ' + percentComplete);
		sourceProgressBar.attr('data-transitiongoal', percentComplete).progressbar();
		// progressLoader.setValue(percentComplete + '%' + " [" + refreshStage + "]");
	}
	if (percentComplete < 100) {
		getRefreshStatusForSourceProject(server, sourceProjectId);
	} else {
		previousSourcePercent = -1;
		sourceProgressBar.attr('data-transitiongoal', 100).progressbar();
		// updateCodeTreeNodes('target', server, projectId);
	}
}
/**
 * Private internal function that is used to poll back results for refresh
 */
function updateTargetRefreshProgress(jsonStatusString, server) {
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
	if (percentComplete === 100 && previousTargetPercent === -1) {
		var selectedTargetProject = $('.selectTargetProject').children(":selected").text();
	} else {
		previousTargetPercent = percentComplete;
		console.log('Percent Complete: ' + percentComplete);
		targetProgressBar.attr('data-transitiongoal', percentComplete).progressbar();
		// progressLoader.setValue(percentComplete + '%' + " [" + refreshStage + "]");
	}
	if (percentComplete < 100) {
		getRefreshStatusForTargetProject(server, targetProjectId);
	} else {
		previousTargetPercent = -1;
		targetProgressBar.attr('data-transitiongoal', 100).progressbar();
		// updateCodeTreeNodes('target', server, projectId);
	}
}