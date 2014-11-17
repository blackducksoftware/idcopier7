/**
 * Globals
 */
// Session Variables
var usernameConstant = 'username';

// If the 'display bom' is clicked, this is flipped to false, since it will no longer be express.
var expressCopy = true;

// The copy button
var copyButton = $("#copyCommentsButton");

// This is the table return by the table builder
var table = null;

/**
 * Loader
 */
jQuery(document).ready(function () {
	// Gets the user that is currently logged in
	$.getJSON("sessionInfo", function (sessionData) {
		$('.username-data').text(sessionData[usernameConstant]);
	});
	
	/**
	 * Hides the progress animators
	 */
	$('.commentLoader').hide();
	$('.copyLoader').hide();
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
	
			table = buildBomDataTable(null);
			
	

		});
	})();
	
	
	/**
	 * Can either be Express Copy
	 * or Copy Selected.
	 * 
	 * The behavior is governed by the user's previous
	 * actions. 
	 */
	copyButton.on('click', function () 
	{
		var idArray = getTableData();

		console.log("Submitting comments copy...");
		// Copy required values
		var sourceServer = $('.selectSourceCommentServer').children(":selected").text();
		var targetServer = $('.selectTargetCommentServer').children(":selected").text();
		var sourceProjectId = $('.selectSourceCommentProject').children(":selected").attr("id");
		var targetProjectId = $('.selectTargetCommentProject').children(":selected").attr("id");
		var appendCommentsOption = $('#appendCommentsCheckbox').is(':checked');

		var params = {
			'copy-source-server' : sourceServer,
			'copy-target-server' : targetServer,
			'copy-source-project-id' : sourceProjectId,
			'copy-target-project-id' : targetProjectId,
			'copy-comment-ids' : idArray,
			'copy-express' : expressCopy,
			'append-comments-option': appendCommentsOption
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
			traditional: true,
			// TODO: Can this be repurposed?
			beforeSend: function()
			{
				$('.copyLoader').show();
			},	
			complete: function()
			{
				$('.copyLoader').hide();
			},
			success : function (msg) {
				console.log(msg);
				displayNotificationMessage(success, 'Copying Comments', msg, noisy);
			},
			error : function (msg) {
				console.log(msg);
				displayNotificationMessage(error, 'Error Copying Comments', msg, noisy);
			}
		});
	});
	
	/**
	 * Loads the BOM into the table
	 */
	$("#displayBomButton").on('click', function () 
	{
		// Gather variables
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
			url : 'displayBom',
			data : params,
			beforeSend: function()
			{
				$('.commentLoader').show();
			},	
			complete: function()
			{
				$('.commentLoader').hide();
			},
			success : function (billOfMaterials) {
				console.log('Successfully retrieved the Bill of Materials');
				displayNotificationMessage(success, 'BOM Display', 'Successfully retrieved the Bill of Materials', noisy);

				setBOMData(billOfMaterials);
				// Change button of copying
				copyButton.val("Copy Selected");
				// Disable express copy
				expressCopy = false;
			},
			error : function (msg) {
				console.log('Unable to retrieved the Bill of Materials');
				displayNotificationMessage(error, 'BOM Display', msg, noisy);
			}
		});
		
		// Resize window headers		
		$(window).bind('resize', function() {
			table.fnAdjustColumnSizing();
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
		} else {
			projectSelectorDiv.append("<option id=\"" + value.projectId + "\">" + value.name + "</option>");
		}
		console.log("Outputting project: " + value);
	});
}


function checkValue(value) {
	return "'" + value + "'";
}

