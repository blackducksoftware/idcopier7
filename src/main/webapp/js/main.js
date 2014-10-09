/**
 * Globals
 */
// The list of selected target paths
var targetPaths = "";
var servers = "servers";
var source = "Source";
var target = "Target";
// The locations array will be used to auto trigger internal jQuery functions
var locations = [ source, target ];
// Growl Types
var success = 'success';
var info = 'info';
var warning = 'warning';
var danger = 'danger';
jQuery(document).ready(function() {

	/**
	 * Populates the pulldowns Assign onChange behavior
	 */
	var populateWidgets = (function() {
		console.log("Populating project pulldows");
		$.each(locations, function(index, locationValue) 
		{
			console.log("Processing location: " + locationValue);
			// Variables used for all processes
			// This is the div id of the pulldown
			var serverSelectorDiv = ".select" + locationValue + "Server";
			// This is the dev id of the project pulldown
			var projectSelectorDiv = ".select" + locationValue + "Project";
			// This is the default non-value message of the selector
			var messageServer = "Select " + locationValue + " Server";
			/**
			 * Populate server pulldown
			 */
			processJSON(servers, locationValue, "Servers").done(function(data) 
			{
				// Grab the cookie, if there is one
				var cachedServerName = $.cookie(serverSelectorDiv);
				$(serverSelectorDiv).empty();
				$(serverSelectorDiv).append("<option>" + messageServer + "</option>");
				$.each(data, function(index, value) {
					if(value.serverName === cachedServerName)
					{
						displayNotificationMessage(info, "Loading Cached Server", "Cookie found for: " + cachedServerName);
						$(serverSelectorDiv).append("<option selected=true>" + value.serverName + "</option>");
						populateProjectPerServer(serverSelectorDiv, locationValue);
					}
					else
						$(serverSelectorDiv).append("<option>" + value.serverName + "</option>");
				});
			});
			
			/**
			 * Init the trees
			 */
			initFancyTree(locationValue);
			
			/**
			 * Assign server pulldown behavior
			 */
			$(serverSelectorDiv).change(function() 
			{

				var serverName = $(serverSelectorDiv).children(":selected").text();
				var path = "reloginServer/" + source + "/?server-name=" + serverName;
				console.log("Sending  relogin path: " + path);
				processJSON(path, locationValue, "Server Projects").done(function(data) {
					setProjects(locationValue, messageServer, data)
				});
				
				// Remember cookies
				$.cookie(serverSelectorDiv, serverName);
			});

			/**
			 * Assign project pulldown behavior
			 */
	
			$(projectSelectorDiv).change(function() {
				progressDiv.hide();
				var projectId = $(this).children(":selected").attr("id");
				var serverName = $(serverSelectorDiv).children(":selected").text();
				// Remember selection in cookie
				$.cookie(projectSelectorDiv, projectId);				
				// Load Tree
				loadFancyTree(locationValue, serverName, projectId);
				// Check refresh status via progress.js
				getRefreshStatusForProject(serverName, projectId);
			});
			
			/**
			 * Establish tooltips
			 */
			var pathSelectInput = ".user" + locationValue + "PathInput";
			$(pathSelectInput).tooltip({
				'show' : true,
				'placement' : 'bottom',
				'title' : "Type " + locationValue +" path here"
			});
						
			/**
			 * Assign enter key behavior for path field
			 */				
			var selectedPath = "." + locationValue + "SelectedPath";
			$(pathSelectInput).keyup(function(event) {
				$(selectedPath).empty();
				$(selectedPath).text(this.value);
				if (event.which == 13 || event.keyCode == 13) 
				{
					var projectId = getProjectIDforLocation(locationValue);
					if(projectId == null)
					{
						displayNotificationMessage(warning, "Can not select path", "No project selected");
						return false;
					}	
					
					var path = $(pathSelectInput).val();
					console.log("Loading tree for user entered path: " + path);

					// IF path starts with '/', then discard it
					var startingChar = path.substring(0, 1);
					if(startingChar === "/" || startingChar === "\\")
					{
						console.log("Removing leading slash");
						path = path.substring(1,path.length);
					}	

					var strings = path.split("/");
					var stringArry = new Array();
					for(var i = 0; i < strings.length+1; i++)
					{
						var tempString=  "_";
						
						if(stringArry.length > 0)
						{
						
							for(var j=0; j<stringArry.length; j++)
							{
								if(j == 0)
									tempString = tempString + strings[j];
								else
									tempString = tempString + "/" + strings[j];
							}
						}
					
						stringArry[i] = tempString;
					}
					for(var k = 0; k < stringArry.length; k++)
					{
						var key = stringArry[k];
						if(key != "_")
						{
							// Build monster string here
							var monsterKey = "";
							for(var z = 0; z <= k; z++)
							{
								monsterKey = monsterKey + stringArry[z];
							}
							
						}
					}
					// tree.js
					fetchPaths(locationValue, monsterKey);
				}
			});
			
		});
	})();

	function loadSelectBox(sender) {
		var currentlySelected = $('.' + sender.toLowerCase() + 'CodeTree :selected').val();
		console.log(sourceTree.getNode(currentlySelected));
	}

	/**
	 * Submit copy button Note the # lookup for non-div Grab the options on the main page as they are part of the copy functionality.
	 */
	$("#submitCopyButton").on('click', function() {
		console.log("Submitting copy IDs...");
		// Copy required values
		var sourceServer = $('.selectSourceServer').children(":selected").text();
		var targetServer = $('.selectTargetServer').children(":selected").text();
		var sourceProjectId = $('.selectSourceProject').children(":selected").attr("id");
		var targetProjectId = $('.selectTargetProject').children(":selected").attr("id");
		var selectedSourcePath = $('.sourceSelectedPath').text();
		// Copy options
		var deferBOMOption = $('#deferBomRefreshCheckBox').is(':checked');
		var recursiveCopyOption = $('#recursiveCopyCheckBox').is(':checked');
		var overwriteIDsOption = $('#overwriteIDsCheckBox').is(':checked');
		var partialBOMOption = $('#partialBOMCheckBox').is(':checked');
		
		var params = {
			'copy-source-server' : sourceServer,
			'copy-target-server' : targetServer,
			'copy-source-project-id' : sourceProjectId,
			'copy-target-project-id' : targetProjectId,
			'copy-source-path' : selectedSourcePath,
			'copy-target-paths' : targetPaths,
			// Also inside the tree behavior Check boxes
			'defer-bom-option' : deferBOMOption,
			'recursive-option' : recursiveCopyOption,
			'overwrite-option' : overwriteIDsOption,
			'partial-bom-option' : partialBOMOption
			
		};
		var verified = verifyCopyParameters(params);
		if (!verified) {
			return false;
		}
		
		// Perform the Copy
		$.ajax({
			type : 'POST',
			url : 'copyIDs',
			data : params,
			success : function(msg) {
				console.log(msg);
				displayNotificationMessage(success, 'Successfully copied identifications', msg);
			},
			error : function(msg) {
				console.log(msg);
				displayNotificationMessage(danger, 'Failed to copy identifications', msg);
			}
		});
		// Call the BOM refresh if necessary
		if(!deferBOMOption)
		{
			displayNotificationMessage(info, "Refresh","Defer BOM refresh unchecked, triggering refresh.");
			performBOMRefresh(targetServer, targetProjectId, partialBOMOption);
		}
	});

	$(".userTargetPathInput").keyup(function() {
		$('.targetSelectedPath').empty();
		// $('.targetSelectedPath').text(this.value);
		if(targetPaths != null)
		{
			if (targetPaths.length === 0) {
				$('.targetSelectedPath').text(this.value);
			} else if (this.value.length === 0) {
				$('.targetSelectedPath').text(targetPaths);
			} else {
				$('.targetSelectedPath').text(targetPaths + ", " + this.value);
			}
		}
	});
});

/**
 * Populates projects for a particular server selector
 * @param serverSelectorDiv
 */
function populateProjectPerServer(serverSelectorDiv, locationValue)
{	
	var serverName = $(serverSelectorDiv).children(":selected").text();
	var projectPath = "getProjects" + "/?server-name=" + serverName;
	var messageProject = "Select " + locationValue + " Project";
	if (serverName != null && serverName.length > 0) {
		processJSON(projectPath, locationValue, "Getting Projects").done(function(data) {
			console.log("Getting projects: " + projectPath);
			setProjects(locationValue, messageProject, data)
		});
	}
}

/**
 * Populate the project pulldown
 */
function setProjects(sender, message, data) {
	var projectSelectorDiv = $('.select' + sender + 'Project');
	var serverSelectorDiv = $('.select' + sender + 'Server');
	
	// Use the selector for lookup, not the actual div
	var cachedProjectId = $.cookie(projectSelectorDiv.selector);
	var cachedServerName = $.cookie(serverSelectorDiv.selector);
	projectSelectorDiv.empty();
	projectSelectorDiv.append("<option>" + message + "</option>");
	
	if(data == null)
	{
		displayNotificationMessage(error, "Project Display Error", "No projects returned from server!");
		return false;
	}
	$.each(data, function(index, value) 
	{
		// TODO: Should this actually be here?
		if(cachedProjectId === value.projectId)
		{
			displayNotificationMessage(info, "Loading Cached Project", "Found cached project ID");
			projectSelectorDiv.append("<option id=\"" + value.projectId + "\" selected=true>" + value.name + "</option>");
			loadFancyTree(sender, cachedServerName, cachedProjectId);
		}
		else
		{
			projectSelectorDiv.append("<option id=\"" + value.projectId + "\">" + value.name + "</option>");
		}	
		console.log("Outputting project: " + value);

	});
}

/**
 * Helper method to return project ID of pulldown
 * @param location
 */
function getProjectIDforLocation(locationValue)
{
	var projectSelectorDiv = ".select" + locationValue + "Project";
	var projectId = $(projectSelectorDiv).children(":selected").attr("id");
	if(projectId == null)
	{
		displayNotificationMessage(error, "Error getting project ID", "Project ID cannot be determined");
	}
	
	return projectId;
}



function getPath(path) {
	var currentNode = $('.sourceCodeTree').dynatree("getTree").getNodeByKey(path);
	if (currentNode !== null) {
		console.log('Activating path: ' + path);
		currentNode.activate();
		console.log('Expanding path: ' + currentNode.data.key);
		currentNode.expand(true);
	}
}
/**
 * Uses the jQuery deferred/promise mechanism to wait until our JSON is retrieved This function handles all success/fail calls in one location
 * 
 * @param path
 * @returns
 */
function processJSON(path, source, widgetName) {
	var msg = "Processed " + source + " " + widgetName;
	var deferred = $.Deferred();
	$.getJSON(path, function(data) {
		deferred.resolve(data)
	}).fail(function(jqxhr, textStatus, error) {
		displayNotificationMessage(danger, "Error: " + msg, jqxhr.responseText);
	}).done(function() {
		displayNotificationMessage(success, msg);
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
	for ( var key in params) {
		var parameter = params[key];
		if (parameter == null) {
			var msg = 'Null value for parameter: ' + key;
			displayNotificationMessage(danger, 'ERROR!', msg);
			// alert("ERROR: Null value for parameter: " + key);
			return false;
		}
	}
	return true;
}
/**
 * This will display any messages we want to show up on the screen
 */
function displayNotificationMessage(type, heading, message) {
	$.growl({
		title : '<br /><h4>' + heading + '</h4>',
		message : message
	}, {
		type : type,
		placement : {
			from : "bottom",
			align : "right"
		},
		offset : {
			x : 20,
			y : 20
		},
		icon_type : 'class'
	});
}