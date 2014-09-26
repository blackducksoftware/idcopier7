jQuery(document).ready(function() {
	/**
	 * String constants for the JS
	 */
	// Constants
	var servers = "servers";
	var source = "Source";
	var target = "Target";
	// The locations array will be used to auto trigger internal jQuery functions
	var locations = [ source, target ];
	/**
	 * Internal variables
	 */
	// The list of selected target paths
	var targetPaths;
	var sourcePath;
	/**
	 * Populate the project pulldown
	 */
	function setProjects(sender, message, data) {
		$('.select' + sender + 'Project').empty();
		$('.select' + sender + 'Project').append("<option>" + message + "</option>");
		$.each(data, function(index, value) {
			console.log("Outputting project: " + value);
			$('.select' + sender + 'Project').append("<option id=\"" + value.projectId + "\">" + value.name + "</option>");
		});
	}
	/**
	 * Populates the pulldowns Assign onChange behavior
	 */
	var populateWidgets = (function() {
		console.log("Populating project pulldows");
		$.each(locations, function(index, locationValue) {
			console.log("Processing location: " + locationValue);
			// Variables used for all processes
			// This is the div id of the pulldown
			var serverSelectorDiv = ".select" + locationValue + "Server";
			// This is the default non-value message of the selector
			var messageServer = "Select " + locationValue + " Server";
			/**
			 * Populate server pulldown
			 */
			$.getJSON(servers, function(data) {
				$(serverSelectorDiv).empty();
				$(serverSelectorDiv).append("<option>" + messageServer + "</option>");
				$.each(data, function(index, value) {
					$(serverSelectorDiv).append("<option>" + value.serverName + "</option>");
				});
			});
			/**
			 * Assign server pulldown behavior
			 */
			$(serverSelectorDiv).change(function() {
				var serverName = $(serverSelectorDiv).children(":selected").text();
				var path = "reloginServer/" + source + "/?server-name=" + serverName;
				console.log("Sending  relogin path: " + path);
				$.getJSON(path, function(data) {
					setProjects(locationValue, messageServer, data)
				});
				// Wipe out code tree
			});
			/**
			 * Populate the projects
			 */
			// Build the path, lower case it to match Controller
			var serverName = $(serverSelectorDiv).children(":selected").text();
			var projectPath = locationValue.toLowerCase() + "Projects" + "/?server-name=" + serverName;
			var messageProject = "Select " + locationValue + " Project";
			$.getJSON(projectPath, function(data) {
				console.log("Invoking path: " + projectPath);
				setProjects(locationValue, messageProject, data)
			});
		});
	})(); // populateWidgets
	function openLazyNode(event, nodes, node, hasChildren) {
		if (hasChildren) {
			return false;
		}
		// node.lazyUrlJson = node.id;
	}
	function loadSelectBox(sender) {
		// alert($('.' + sender.toLowerCase() + 'CodeTree
		// :selected').val());
		var currentlySelected = $('.' + sender.toLowerCase() + 'CodeTree :selected').val();
		console.log(sourceTree.getNode(currentlySelected));
	}
	/**
	 * Loads the project for a: - Specific server - Specific project ID
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
			// var currentlySelected = $('#lstNodes :selected').val();
			tree = $('.' + sender.toLowerCase() + 'CodeTree').easytree({
				allowActivate : true,
				dataUrl : path,
				openLazyNode : openLazyNode,
				lazyUrl : path
			});
			$('.' + sender.toLowerCase() + 'CodeTree').click(function() {
				loadSelectBox(sender);
			});
		}
	}
	function loadDynaTree(sender, serverName, projectId) {
		if (projectId !== null) {
			// Active the IDCProjectController.expandPathNode()
			// Pass in an argument representing the node
			var path = "treeExpandNode/" + serverName + '/' + projectId + "/?tree-node-path=";
			var setAsCheckBox = false;
			if (sender === target) {
				setAsCheckBox = true;
			}
			$('.' + sender.toLowerCase() + 'CodeTree').dynatree({
				title : "Source Code Tree",
				fx : {
					height : "toggle",
					duration : 200
				},
				checkbox : setAsCheckBox,
				selectMode : 2,
				autoFocus : false,
				initAjax : {
					// Gets the root node for the project
					url : path + '/'
				},
				// onActivate: function(node)
				onClick : function(dtnode) {
					if (dtnode.data.isFolder && !dtnode.hasChildren()) {
						dtnode.expand();
						// This is the full Controller path with the node attached to the end
						var finalPath = path + dtnode.data.key;
						console.log("Passing in final RESTful path for node expansion: " + finalPath);
						dtnode.appendAjax({
							url : finalPath,
							data : {
								"mode" : "all"
							},
							success : function(node) {
								node.expand();
							},
							debugLazyDelay : 750
						});
					}
					if (sender === source) {
						$('.' + sender.toLowerCase() + 'SelectedPath').text("/" + dtnode.data.key);
					}
				},
				onSelect : function(select, node) {
					if (sender === target) {
						var selNodes = node.tree.getSelectedNodes();
						// convert to title/key array
						var selKeys = $.map(selNodes, function(node) {
							return "/" + node.data.key;
						});
						targetPaths = selectedPaths = selKeys.join(", ");
						$('.' + sender.toLowerCase() + 'SelectedPath').text(selectedPaths);
					}
				},
			});
			$('.' + sender.toLowerCase() + 'CodeTree').dynatree("getTree").reload();
		}
	}
	/**
	 * Submit copy button Note the # lookup for non-div
	 */
	$("#submitCopyButton").on('click', function() {
		console.log("Submitting copy IDs...");
		var sourceServer = $('.selectSourceServer').children(":selected").text();
		var targetServer = $('.selectTargetServer').children(":selected").text();
		var sourceProjectId = $('.selectSourceProject').children(":selected").attr("id");
		var targetProjectId = $('.selectTargetProject').children(":selected").attr("id");
		var params = {
			'copy-source-server' : sourceServer,
			'copy-target-server' : targetServer,
			'copy-source-project-id' : sourceProjectId,
			'copy-target-project-id' : targetProjectId,
			'copy-source-path' : sourcePath, // Set inside the dynatree behavior
			'copy-target-paths' : targetPaths, // Also inside the dynatree behavior
		};
		$.ajax({
			type : 'POST',
			url : 'copyIDs',
			data : params,
			success : function(msg) {
				console.log(msg);
				alert('Copy Result: ' + msg);
			},
			error : function(msg) {
				console.log(msg);
				alert("General error: " + msg)
			}
		});
	});
	$(".selectSourceProject").change(function() {
		var projectId = $(this).children(":selected").attr("id");
		var sourceServerName = $('.selectSourceServer').children(":selected").text();
		// loadProject(source, sourceServerName, projectId);
		loadDynaTree(source, sourceServerName, projectId);
	});
	$(".selectTargetProject").change(function() {
		var projectId = $(this).children(":selected").attr("id");
		var targetServerName = $('.selectTargetServer').children(":selected").text();
		loadDynaTree(target, targetServerName, projectId);
	});
	$(".userSourcePathInput").tooltip({
		'show' : true,
		'placement' : 'bottom',
		'title' : "Type source path here"
	});
	$(".userSourcePathInput").keyup(function() {
		$('.sourceSelectedPath').empty();
		$('.sourceSelectedPath').text(this.value);
	});
	$(".userTargetPathInput").tooltip({
		'show' : true,
		'placement' : 'bottom',
		'title' : "Type target path here"
	});
	$(".userTargetPathInput").keyup(function() {
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
