/**
 * GLOBALS
 */
var source = "Source";
var target = "Target";
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
	/**
	 * Handle source project refresh
	 */
	sourceProgressBar = $('#sourceProgressBar').progressbar({
		display_text : 'fill',
		use_percentage : true
	});
	sourceProgressBar.attr('data-transitiongoal', 100).progressbar();
	/**
	 * Performs a refresh on project
	 */
	$("#refreshSourceProjectBom").on('click', function() {
		var sourceServer = $('.selectSourceServer').children(":selected").text();
		var sourceProjectId = $('.selectSourceProject').children(":selected").attr("id");
		var partialBOMOption = $('#partialBOMCheckBox').is(':checked');
		if (sourceProjectId == null) {
			displayNotificationMessage(error, 'Unable to refresh BOM', 'Please select a target project!');
			return false;
		}
		sourceProgressBar.attr('data-transitiongoal', 0).progressbar();
		performBOMRefreshSource(sourceServer, sourceProjectId, partialBOMOption);
	});
	/**
	 * Handle target project refresh
	 */
	targetProgressBar = $('#targetProgressBar').progressbar({
		display_text : 'fill',
		use_percentage : true
	});
	targetProgressBar.attr('data-transitiongoal', 100).progressbar();
	/**
	 * Performs a refresh on project
	 */
	$("#refreshTargetProjectBom").on('click', function() {
		var targetServer = $('.selectTargetServer').children(":selected").text();
		var targetProjectId = $('.selectTargetProject').children(":selected").attr("id");
		var partialBOMOption = $('#partialBOMCheckBox').is(':checked');
		if (targetProjectId == null) {
			displayNotificationMessage(error, 'Unable to refresh BOM', 'Please select a target project!');
			return false;
		}
		targetProgressBar.attr('data-transitiongoal', 0).progressbar();
		performBOMRefreshTarget(targetServer, targetProjectId, partialBOMOption);
	});
});
/**
 * Calls IDCProjectController and performs a BOM refresh against a specific server/project
 */
function performBOMRefreshSource(server, projectId, partialBOMOption) {
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
			getRefreshStatusForSourceProject(server, projectId);
			// Reload the tree
			refreshNodes("target");
		}
	});
}
/**
 * Calls IDCProjectController and performs a BOM refresh against a specific server/project
 */
function performBOMRefreshTarget(server, projectId, partialBOMOption) {
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
			getRefreshStatusForTargetProject(server, projectId);
			// Reload the tree
			refreshNodes("target");
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