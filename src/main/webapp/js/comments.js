/**
 * Globals
 */
var servers = "servers";
var source = "Source";
var target = "Target";
// The locations array will be used to auto trigger internal jQuery functions
var locations = [source, target];
// Session Variables
var usernameConstant = 'username';
// Growl Types
var success = 'success';
var info = 'info';
var warning = 'warning';
var error = 'error';
// Growl level
// This toggles the growl messages
var noisy = 0;
var quiet = 1;
var noiseLevel = quiet;
jQuery(document).ready(function () {
	// Gets the user that is currently logged in
	$.getJSON("sessionInfo", function (sessionData) {
		$('.username-data').text(sessionData[usernameConstant]);
	});
	/**
	 * Populates the pulldowns Assign onChange behavior
	 */
	var populateWidgets = (function () {
		console.log("Populating project pulldows for the comments copying");
		$.each(locations, function (index, locationValue) {
			console.log("Processing Comment location: " + locationValue);
			// Variables used for all processes
			// This is the div id of the
			// pulldown
			var serverSelectorDiv = ".select" + locationValue + "CommentServer";
			// This is the dev id of the project
			// pulldown
			var projectSelectorDiv = ".select" + locationValue + "CommentProject";
			// This is the default non-value
			// message of the selector
			var messageServer = "Select " + locationValue + " Comment Server";
			/**
			 * Populate server pulldown
			 */
			processJSON(servers, locationValue, "Servers").done(function (data) {
				// Grab the
				// cookie, if
				// there is one
				var cachedServerName = $.cookie(serverSelectorDiv);
				$(serverSelectorDiv).empty();
				$(serverSelectorDiv).append("<option>" + messageServer + "</option>");
				$.each(data, function (index, value) {
					if (value.serverName === cachedServerName) {
						displayNotificationMessage(info, "Loading Cached Comment Server", "Cookie found for: " + cachedServerName, noiseLevel);
						$(serverSelectorDiv).append("<option selected=true>" + value.serverName + "</option>");
						populateProjectPerServer(serverSelectorDiv, locationValue);
					} else
						$(serverSelectorDiv).append("<option>" + value.serverName + "</option>");
				});
			});
			/**
			 * Assign server pulldown behavior
			 */
			$(serverSelectorDiv).change(function () {
				var serverName = $(serverSelectorDiv).children(":selected").text();
				var path = "reloginServer/" + source + "/?server-name=" + serverName;
				console.log("Sending  relogin path: " + path);
				processJSON(path, locationValue, "Server Projects").done(function (data) {
					setProjects(locationValue, messageServer, data);
				});
				// Remember cookies
				$.cookie(serverSelectorDiv, serverName);
			});
			/**
			 * Assign project pulldown behavior
			 */
			$(projectSelectorDiv).change(function () {
				// progressDiv.hide();
				var projectId = $(this).children(":selected").attr("id");
				var serverName = $(serverSelectorDiv).children(":selected").text();
				// Remember selection in cookie
				$.cookie(projectSelectorDiv, projectId);
			});
			// var dataSet = [['true', 'Niles', 'c_niles', '1.0', 'v101', 'This is my comment'], ['true', 'Madison', 'c_madison', '4.6-Beta-3', 'v46beta3', 'This is not really used']];
			buildBomDataTable(null);
		});
	})();
	/**
	 * Submit copying of commments button.
	 */
	$("#copyCommentsButton").on('click', function () {
		console.log("Submitting comments copy...");
		// Copy required values
		var sourceServer = $('.selectSourceCommentServer').children(":selected").text();
		var targetServer = $('.selectTargetCommentServer').children(":selected").text();
		var sourceProjectId = $('.selectSourceCommentProject').children(":selected").attr("id");
		var targetProjectId = $('.selectTargetCommentProject').children(":selected").attr("id");
		var components = $('.sourceSelectedPath').text();
		var params = {
			'copy-source-server' : sourceServer,
			'copy-target-server' : targetServer,
			'copy-source-project-id' : sourceProjectId,
			'copy-target-project-id' : targetProjectId,
			'copy-comments-components' : components
		};
		var verified = verifyCopyParameters(params);
		if (!verified) {
			return false;
		}
		// Perform the Copying of comments
		$.ajax({
			type : 'POST',
			url : 'copyComments',
			data : params,
			success : function (msg) {
				console.log(msg);
				displayNotificationMessage(success, 'Successfully copied comments', msg, noiseLevel);
				// Call the BOM refresh
				// if necessary
				// performBOMRefresh("target", targetServer, targetProjectId, partialBOMOption);
			},
			error : function (msg) {
				console.log(msg);
				displayNotificationMessage(error, 'Failed to copy identifications', msg, noisy);
			}
		});
	});
	$("#displayBomButton").on('click', function () {
		var sourceServer = $('.selectSourceCommentServer').children(":selected").text();
		var sourceProjectId = $('.selectSourceCommentProject').children(":selected").attr("id");
		var sourceProjectName = $('.selectSourceCommentProject').children(":selected").text();
		if (sourceProjectId === undefined) {
			return false;
		}
		var params = {
			'copy-source-server' : sourceServer,
			'copy-source-project-id' : sourceProjectId,
			'copy-source-project-name' : sourceProjectName
		};
		console.log("Displaying the Bill of Materials for " + sourceProjectName + " [" + sourceProjectId + "] on " + sourceServer);
		
		$.ajax({
			type : 'POST',
			url : 'bom',
			data : params,
			success : function (billOfMaterials) {
				console.log('Successfully retrieved the Bill of Materials');
				displayNotificationMessage(success, 'Successfully!', 'Successfully retrieved the Bill of Materials', noisy);

				var bomDataSet = [];

				$.each(JSON.parse(billOfMaterials), function (index, currentBomItem) {
					var componentName = currentBomItem.componentName;
					var componentId = currentBomItem.componentId;
					var versionName = currentBomItem.versionName;
					var versionId = currentBomItem.versionId;
					var comment = currentBomItem.comment;

					var bomItem = [true ,  componentName, componentId , versionName , versionId , comment];
					bomDataSet.push(bomItem);
				});
				
				buildBomDataTable(JSON.parse(billOfMaterials));
			},
			error : function (msg) {
				console.log('Unable to retrieved the Bill of Materials');
				displayNotificationMessage(error, 'Unable to retrieved the Bill of Materials', msg, noisy);
			}
		});
	});
});
/**
 * Populates projects for a particular server selector
 *
 * @param serverSelectorDiv
 */
function populateProjectPerServer(serverSelectorDiv, locationValue) {
	var serverName = $(serverSelectorDiv).children(":selected").text();
	var projectPath = "getProjects" + "/?server-name=" + serverName;
	var messageProject = "Select " + locationValue + " Project";
	if (serverName !== null && serverName.length > 0) {
		processJSON(projectPath, locationValue, "Getting Projects").done(function (data) {
			console.log("Getting projects: " + projectPath);
			setProjects(locationValue, messageProject, data);
		});
	}
}
/**
 * Populate the project pulldown
 */
function setProjects(sender, message, data) {
	var projectSelectorDiv = $('.select' + sender + 'CommentProject');
	var serverSelectorDiv = $('.select' + sender + 'CommentServer');
	// Use the selector for lookup, not the actual div
	var cachedProjectId = $.cookie(projectSelectorDiv.selector);
	var cachedServerName = $.cookie(serverSelectorDiv.selector);
	projectSelectorDiv.empty();
	projectSelectorDiv.append("<option>" + message + "</option>");
	if (data === null) {
		displayNotificationMessage(error, "Project Display Error", "No projects returned from server!", noisy);
		return false;
	}
	$.each(data, function (index, value) {
		// TODO: Should this actually be here?
		if (cachedProjectId === value.projectId) {
			displayNotificationMessage(info, "Loading Cached Comment Project", "Found cached project ID", noiseLevel);
			projectSelectorDiv.append("<option id=\"" + value.projectId + "\" selected=true>" + value.name + "</option>");
			// loadFancyTree(sender, cachedServerName, cachedProjectId);
		} else {
			projectSelectorDiv.append("<option id=\"" + value.projectId + "\">" + value.name + "</option>");
		}
		console.log("Outputting project: " + value);
	});
}
/**
 * Helper method to return project ID of pulldown
 *
 * @param location
 */
function getProjectIDforLocation(locationValue) {
	var projectSelectorDiv = ".select" + locationValue + "Project";
	var projectId = $(projectSelectorDiv).children(":selected").attr("id");
	if (projectId === null) {
		displayNotificationMessage(error, "Error getting project ID", "Project ID cannot be determined", noisy);
	}
	return projectId;
}
/**
 * Uses the jQuery deferred/promise mechanism to wait until our JSON is
 * retrieved This function handles all success/fail calls in one location
 *
 * @param path
 * @returns
 */
function processJSON(path, source, widgetName) {
	var msg = "Processed " + source + " " + widgetName;
	var deferred = $.Deferred();
	$.getJSON(path, function (data) {
		deferred.resolve(data);
	}).fail(function (jqxhr, textStatus, error) {
		displayNotificationMessage(error, "Error: " + msg, jqxhr.responseText, noisy);
	}).done(function () {
		// Always show error
		displayNotificationMessage(success, "Processing", msg, noiseLevel);
	});
	return deferred.promise();
}
/**
 * Little function to test the parameters for some client-side validation
 *
 * @param params
 */
function verifyCopyParameters(params) {
	console.log("Verifying params");
	var valid = true;
	for (var key in params) {
		var parameter = params[key];
		displayNotificationMessage(info, 'Info!', parameter, noisy);
		if (typeof parameter === "string") {
			if (!parameter) {
				var msg = 'Invalid value for parameter: ' + key;
				displayNotificationMessage(error, 'ERROR!', msg, noisy);
				// alert("ERROR: Null value for parameter: " + key);
				valid = false;
			}
		}
	}
	return valid;
}
/**
 * This will display any HubSpot Messaging message we want to show up on the
 * screen
 */
function displayNotificationMessage(type, heading, message, noiselevel) {
	if (noiselevel == quiet)
		return;
	var output;
	// Check to see that the heading has been defined
	if (heading !== undefined) {
		output = '<b>' + heading + '</b>';
	}
	// Check to see that the message has been defined
	if (message !== undefined) {
		output = output + '<br />' + message;
	}
	Messenger.options = {
		extraClasses : 'messenger-fixed messenger-on-bottom messenger-on-right',
		theme : 'air'
	};
	Messenger().post({
		message : output,
		type : type,
		showCloseButton : true
	});
}
/**
 * Performs the logout for the user
 */
$("#logoutButton").on('click', function () {
	$.ajax({
		type : 'POST',
		url : 'logout',
		success : function (msg) {
			console.log(msg);
		},
		error : function (msg) {
			console.log(msg);
		}
	});
});