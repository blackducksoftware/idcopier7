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
/****
 * 
 *  Common Functions
 *  - Helper methods for easier processing
 *  Used in: projectdisplay.jsp and comments.jsp
 * 
 *******/

/**
 * GLOBALS
 */
	// Growl Types


	var servers = "servers";
	var source = "Source";
	var target = "Target";
	// The locations array will be used to auto trigger internal jQuery functions
	var locations = [source, target];

	var success = 'success';
	var info = 'info';
	var warning = 'warning';
	var error = 'error';
	// Growl level
	var noisy = 0;
	var quiet = 1;
	//This toggles the growl messages
	//TODO: Can make this configurable later.
	var noiseLevel = quiet;
/**
 * GLOBALS END
 */

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
		displayNotificationMessage(error, "Error: " + msg, jqxhr.responseText, noisy);
	}).done(function() {
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
	for ( var key in params) {
		var parameter = params[key];
		if (typeof parameter == "string") {
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
 * This will display any messages we want to show up on the screen
 */
function displayGrowlNotificationMessage(type, heading, message) {
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
/**
 * This will display any HubSpot Messaging message we want to show up on the screen
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
	}
	Messenger().post({
		message : output,
		type : type,
		showCloseButton : true
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
	if (projectId == null) {
		displayNotificationMessage(error, "Error getting project ID", "Project ID cannot be determined", noisy);
	}
	return projectId;
}

/**
 * Performs the logout for the user
 */
$("#logoutButton").on('click', function() {
	$.ajax({
		type : 'POST',
		url : 'logout',
		success : function(msg) {
			console.log(msg);
		},
		error : function(msg) {
			console.log(msg);
		}
	});
});