/**
 * Globals
 */
// The list of selected target paths
var targetPaths;
var servers = "servers";
var source = "Source";
var target = "Target";

// The locations array will be used to auto trigger internal
// jQuery
// functions
var locations = [
        source, target
    ];

// Growl Types
var success = 'success';
var info = 'info';
var warning = 'warning';
var danger = 'danger';

jQuery(document).ready(function () {
    /**
    * Populate the project pulldown
    */
    function setProjects(sender, message, data) {
        $('.select' + sender + 'Project').empty();
        $('.select' + sender + 'Project').append("<option>" + message + "</option>");
        $.each(data, function (index, value) {
            console.log("Outputting project: " + value);
            $('.select' + sender + 'Project').append("<option id=\"" + value.projectId + "\">" + value.name + "</option>");
        });
    }

    /**
    * Populates the pulldowns Assign onChange behavior
    */
    var populateWidgets = (function () {
            console.log("Populating project pulldows");
            $.each(locations, function (index, locationValue) {
                console.log("Processing location: " + locationValue);
                // Variables used for all processes
                // This is the div id of the
                // pulldown
                var serverSelectorDiv = ".select" + locationValue + "Server";
                // This is the dev id of the project
                // pulldown
                var projectSelectorDiv = ".select" + locationValue + "Project";
                // This is the default non-value
                // message of the selector
                var messageServer = "Select " + locationValue + " Server";
                /**
        * Populate server pulldown
        */
	            processJSON(servers, locationValue, "Servers").done(function (data)
	    		{
	                $(serverSelectorDiv).empty();
	                $(serverSelectorDiv).append("<option>" + messageServer + "</option>");
	                $.each(data, function (index, value) {
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
	                processJSON(path, locationValue, "Server Projects").done(function (data)
	        		{
	                	setProjects(locationValue, messageServer, data)
	        		});
	            });
                /**
        * Populate the projects
        */
                // Build the path, lower case it to      // match Controller
                var serverName = $(serverSelectorDiv).children(":selected").text();
                var projectPath = locationValue.toLowerCase() + "Projects" + "/?server-name=" + serverName;
                var messageProject = "Select " + locationValue + " Project";
                if(serverName != null && serverName.length > 0)
                {
	                processJSON(projectPath, locationValue, "Getting Projects").done(function (data)
	                {
	                    console.log("Getting projects: " + projectPath);
	                    setProjects(locationValue, messageProject, data)
	                });
            	}
                /**
        * Assign project pulldown behavior
        */
                $(projectSelectorDiv).change(function () {
                    var projectId = $(this).children(":selected").attr("id");
                    var serverName = $(serverSelectorDiv).children(":selected").text();
                    loadDynaTree(locationValue, serverName, projectId);
                    progressDiv.hide();
                });

            });
        })(); // populateWidgets

    function loadSelectBox(sender) {
        // alert($('.' + sender.toLowerCase() + 'CodeTree
        // :selected').val());
        var currentlySelected = $('.' + sender.toLowerCase() + 'CodeTree :selected').val();
        console.log(sourceTree.getNode(currentlySelected));
    }
    /**
    * Loads the project for a: - Specific server - Specific
    * project ID
    */

    function loadProject(sender, serverName, projectId) {
        if (projectId !== null) {
            var path = serverName + '/' + projectId + '/' + ROOT;
            // Modify the HTML
            $('.user' + sender + 'PathInput').empty();
            $('.user' + sender + 'PathInput').val('/');
            $('.' + sender.toLowerCase() + 'SelectedPath').empty();
            $('.' + sender.toLowerCase() + 'SelectedPath').text('/');
            // Set the EasyTree
            var tree;
            if (sender == source) {
                tree = sourceTree;
            } else {
                tree = targetTree
            }
            // var currentlySelected = $('#lstNodes
            // :selected').val();
            tree = $('.' + sender.toLowerCase() + 'CodeTree').easytree({
                allowActivate: true,
                dataUrl      : path,
                openLazyNode : openLazyNode,
                lazyUrl      : path
            });
            $('.' + sender.toLowerCase() + 'CodeTree').click(function () {
                loadSelectBox(sender);
            });
        }
    }

    /**
    * Submit copy button Note the # lookup for non-div Grab the
    * options on the main page as they are part of the copy
    * functionality.
    */
    $("#submitCopyButton").on('click', function () {
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

        var params = {
                'copy-source-server'    : sourceServer,
                'copy-target-server'    : targetServer,
                'copy-source-project-id': sourceProjectId,
                'copy-target-project-id': targetProjectId,
                'copy-source-path'      : selectedSourcePath,
                'copy-target-paths'     : targetPaths,
                // Also
                // inside
                // the
                // dynatree
                // behavior
                // Check boxes
                'defer-bom-option'      : deferBOMOption,
                'recursive-option'      : recursiveCopyOption,
                'overwrite-option'      : overwriteIDsOption
            };

        var verified = verifyCopyParameters(params);
        if (!verified) {
            return false;
        }

        $.ajax({
            type   : 'POST',
            url    : 'copyIDs',
            data   : params,
            success: function (msg) {
                console.log(msg);
                displayNotificationMessage(success, 'Successfully copied identifications', msg);
                // alert('Copy Result: '
                // + msg);
            },
            error  : function (msg) {
                console.log(msg);
                displayNotificationMessage(danger, 'Failed to copy identifications', msg);
                // alert("General error:
                // " + msg)
            }
        });
    });

    $(".userSourcePathInput").tooltip({
        'show'     : true,
        'placement': 'bottom',
        'title'    : "Type source path here"
    });
    $(".userSourcePathInput").keyup(

    /**
    * TODO:  Niles, what does this event do?  Should this be in tree.js?
    */
    function (event) {
        $('.sourceSelectedPath').empty();
        $('.sourceSelectedPath').text(this.value);
        console.log("this.value = " + this.value);

        if (event.which == 13 || event.keyCode == 13) {
            var path = $('.userSourcePathInput').val();
            console.log("Loading tree for user entered path: " + path);

            var pathParts = path.split("/");
            var runningPaths = [];
            var currentPath = "";

            for (i = 0; i < pathParts.length; i++) {
                if (!currentPath) {
                    currentPath = pathParts[i];
                } else {
                    currentPath += "/" + pathParts[i];
                }
                runningPaths.push(currentPath);
            }

            for (i = runningPaths.length - 1; i >= 0; i--) {
                // console.log("Running path: "
                // + runningPaths[i]);
                var testName = $('.sourceCodeTree').dynatree("getTree").getNodeByKey(runningPaths[i]);

                if (testName != null) {
                    testName.activate();
                    testName.expand(true);

                    console.log("Name: " + testName.data.title + " (" + path + ")");
                }
            }

            /*
        * $('.sourceCodeTree').dynatree("getTree").loadKeyPath(path,
        *
        * function (node, status) { if
        * (status == "loaded") { // 'node'
        * is a parent that was just
        * traversed.// If we call expand() //
        * here, then all nodes will be //
        * expanded // as we go //
        * node.expand(); } else if (status ==
        * "ok") { // 'node' is the end node
        * of our // path. // If we call
        * activate()or makeVisible()here,
        * then the // whole branch will be
        * exoanded now node.activate(); }
        * else if (status == "notfound") {
        * var seg = arguments[2], isEndNode =
        * arguments[3];
        *
        * var testName =
        * $('.sourceCodeTree').dynatree("getTree").getNodeByKey(path);
        *
        * console.log("Name: " + testName + " (" +
        * path + ")"); console.log("Path
        * doesn't exist! [" + path +
        * "]\r\n" + seg); } });
        */
        }
    });

    $(".userTargetPathInput").tooltip({
        'show'     : true,
        'placement': 'bottom',
        'title'    : "Type target path here"
    });
    $(".userTargetPathInput").keyup(function () {
        $('.targetSelectedPath').empty();
        // $('.targetSelectedPath').text(this.value);
        if (targetPaths.length === 0) {
            $('.targetSelectedPath').text(this.value);
        } else if (this.value.length === 0) {
            $('.targetSelectedPath').text(targetPaths);
        } else {
            $('.targetSelectedPath').text(targetPaths + ", " + this.value);
        }
    });
    
});

/**
 * Uses the jQuery deferred/promise mechanism to wait until our JSON is retrieved
 * This function handles all success/fail calls in one location
 * @param path
 * @returns
 */
function processJSON( path , source, widgetName)
{
	var msg = "Processed " + source + " " + widgetName;
	var deferred = $.Deferred();
	  $.getJSON(path, function (data) {
		  deferred.resolve(data)
	  }).fail(function (jqxhr, textStatus, error) {
          displayNotificationMessage(danger, "Error: "+ msg, jqxhr.responseText);
      }).done(function ()
      {
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
    for (var key in params) {
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
        title  : '<br /><h4>' + heading + '</h4>',
        message: message
    }, {
        type     : type,
        placement: {
            from : "bottom",
            align: "right"
        },
        offset   : {
            x: 20,
            y: 20
        },
        icon_type: 'class'
    });
}