jQuery(document).ready(
		function() {
			/**
			 * String constants for the JS
			 */
			// UI text
			var selectSourceServer = "Select Source Server";
			var selectTargetServer = "Select Target Server";
			
			var selectSourceProject = "Select Source Project";
			var selectTargetProject = "Select Target Project";
			// Variables
			var servers = "servers";
			var source = "Source";
			var target = "Target";
			// The locations array will be used to auto trigger internal jQuery functions
			var locations = [source, target];
			// Constants
			var ROOT = "root";
			/**
			 * Init the trees. They will be empty, but will not look ugly, so
			 * that's nice.
			 */
			// var sourceTree = $('#sourceCodeTree').easytree();
			// var targetTree = $('#targetCodeTree').easytree();
			/**
			 * Skipping a bunch in here for now, because we are only working
			 * with one server. So I am going to just load all of the projects
			 * to the drop downs and worry about the rest later
			 * 
			 * -NM
			 */
			$.getJSON(servers, function(data) {
				if (data.length === 1) {
					$('.selectSourceServer').empty();
					$('.selectTargetServer').empty();
					$.each(data, function(index, value) {
						$('.selectSourceServer').append(
								"<option>" + value.serverName + "</option>");
						$('.selectTargetServer').append(
								"<option>" + value.serverName + "</option>");
					});
				} else {
					$('.selectSourceServer').empty();
					$('.selectTargetServer').empty();
					$('.selectSourceServer').append(
							"<option>" + selectSourceServer + "</option>");
					$('.selectTargetServer').append(
							"<option>" + selectTargetServer + "</option>");
					$.each(data, function(index, value) {
						$('.selectSourceServer').append(
								"<option>" + value.serverName + "</option>");
						$('.selectTargetServer').append(
								"<option>" + value.serverName + "</option>");
					});
				}
			});
			
			/**
			 * Populate the project pulldown
			 */
			function setProjects(sender, message, data) {
				$('.select' + sender + 'Project').empty();
				$('.select' + sender + 'Project').append("<option>" + message + "</option>");
				$.each(data, function(index, value) {
					console.log("Outputting project: " + value);
					$('.select' + sender + 'Project').append(
							"<option id=\"" + value.projectId + "\">"
									+ value.name + "</option>");
				});
			}
			/**
			 * Populates the pulldowns
			 * Assign onChange behavior
			 */
			var populateWidgets = (function()
			{			
				console.log("Populating project pulldows");
				$.each(locations, function (index, locationValue)
				{
					console.log("Processing location: " + locationValue);
					
					/**
					 * Populate the projects
					 */
					// Build the path, lower case it to match Controller
					var projectPath = locationValue.toLowerCase() + "Projects";
					var messageProject = "Select " + locationValue + " Project";
					$.getJSON(projectPath, function(data) 
					{
						console.log("Invoking path: " + projectPath);
						setProjects(locationValue, messageProject, data)
					});
					
					/**
					 * Assign server pulldown behavior
					 */
					// This is the div id of the pulldown
					var serverSelectorDiv = ".select" + locationValue + "Server";
					// This is the default non-value message of the selector
					var messageServer = "Select " + locationValue + " Server";
					$(serverSelectorDiv).change(function() {
						var serverName = $(serverSelectorDiv).children(
								":selected").text();
						var path = "reloginServer/" + source + "/?server-name="
								+ serverName;

						console.log("Sending  relogin path: " + path);
						$.getJSON(path, function(data) {
							setProjects(locationValue, messageServer, data)
						});
						// Wipe out code tree

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

				var currentlySelected = $(
						'.' + sender.toLowerCase() + 'CodeTree :selected')
						.val();
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

					tree = $('.' + sender.toLowerCase() + 'CodeTree').easytree(
							{
								allowActivate : true,
								dataUrl : path,
								openLazyNode : openLazyNode,
								lazyUrl : path
							});

					$('.' + sender.toLowerCase() + 'CodeTree').click(
							function() {
								loadSelectBox(sender);
							});
				}
			}
			function loadDynaTree(sender, serverName, projectId) {
				if (projectId !== null) {
					var path = serverName + '/' + projectId;

					$('.' + sender.toLowerCase() + 'CodeTree').dynatree(
							{
								title : "Source Code Tree",
								fx : {
									height : "toggle",
									duration : 200
								},
								clickFolderMode : 3,
								autoFocus : false,
								initAjax : {
									url : path + '/' + ROOT
								},

								onActivate : function(node) {

									if (!node.hasChildren()) {

										console.error("Path = " + path + '/'
												+ node.data.key);
										node.expand();
										node.appendAjax({
											url : path + '/' + node.data.key,
											data : {
												"mode" : "all"
											},
											success : function(node) {
												node.expand();
											},
											debugLazyDelay : 750
										});

									}
								}
							});
				}
			}

			$(".selectSourceProject").change(
					function() {
						var projectId = $(this).children(":selected")
								.attr("id");
						var sourceServerName = $('.selectSourceServer')
								.children(":selected").text();
						// loadProject(source, sourceServerName, projectId);
						loadDynaTree(source, sourceServerName, projectId);
					});
			$(".selectTargetProject").change(
					function() {
						var projectId = $(this).children(":selected")
								.attr("id");
						var targetServerName = $('.selectTargetServer')
								.children(":selected").text();
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
				$('.targetSelectedPath').text(this.value);
			});
		});
