/**
 * Loads the tree in the main well.
 * 
 * @param sender
 * @param serverName
 * @param projectId
 */
function loadFancyTree(sender, serverName, projectId) {
	if (projectId !== null) {
		// Clear our the selected paths
		var selectedPathDiv = "." + sender + "SelectedPath";
		$(selectedPathDiv).empty();
		// Activate the
		// IDCProjectController.expandPathNode()
		// Pass in an argument representing the node
		var path = "treeExpandNode/" + serverName + '/' + projectId + "/?tree-node-path=";
		var setAsCheckBox = false;
		if (sender === target) {
			setAsCheckBox = true;
		}
		$('#' + sender.toLowerCase() + 'CodeTree').fancytree({
			title : sender + 'Code Tree',
			extensions : [ "glyph" ],
			glyph : {
				map : {
					doc : "glyphicon glyphicon-file",
					docOpen : "glyphicon glyphicon-file",
					checkbox : "glyphicon glyphicon-unchecked",
					checkboxSelected : "glyphicon glyphicon-check",
					checkboxUnknown : "glyphicon glyphicon-share",
					error : "glyphicon glyphicon-warning-sign",
					expanderClosed : "glyphicon glyphicon-plus-sign",
					expanderLazy : "glyphicon glyphicon-plus-sign",
					// expanderLazy: "glyphicon glyphicon-expand",
					expanderOpen : "glyphicon glyphicon-minus-sign",
					// expanderOpen: "glyphicon glyphicon-collapse-down",
					folder : "glyphicon glyphicon-folder-close",
					folderOpen : "glyphicon glyphicon-folder-open",
					loading : "glyphicon glyphicon-refresh"
				}
			},
			persist : true,
			activeVisible : true, // Make sure, active nodes are visible (expanded).
			autoActivate : true, // Automatically activate a node when it is focused (using keys).
			autoCollapse : false, // Automatically collapse all siblings, when a node is expanded.
			autoScroll : true, // Automatically scroll nodes into visible area.
			clickFolderMode : 3, // 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
			checkbox : setAsCheckBox, // Show checkboxes.
			debugLevel : 0, // 0:quiet, 1:normal, 2:debug
			generateIds : false, // Generate id attributes like <span id='fancytree-id-KEY'>
			idPrefix : "ft_", // Used to generate node id´s like <span id='fancytree-id-<key>'>.
			icons : true, // Display node icons.
			keyboard : true, // Support keyboard navigation.
			keyPathSeparator : "/", // Used by node.getKeyPath() and tree.loadKeyPath().
			minExpandLevel : 1, // 1: root node is not collapsible
			selectMode : 2, // 1:single, 2:multi, 3:multi-hier
			titlesTabbable : true, // Node titles can receive keyboard focus
			source : {
				url : path + '/'
			},
			lazyLoad : function(event, data) {
				var node = data.node;
				var finalPath = path + node.key;
				console.log(data);
				data.result = $.ajax({
					url : finalPath,
					dataType : "json"
				});
			},
			ajaxDefaults : { // Used by initAjax option
				cache : true, // false: Append random '_' argument to the request url to prevent caching.
				timeout : 0, // >0: Make sure we get an ajax error for invalid URLs
				dataType : "json" // Expect json format and pass json object to callbacks.
			},
			activate : function(event, data) {
				if (sender === source) {
					showSelectedPath(data.node);
				}
			},
			click : function(event, data) {
				if (sender === source) {
					showSelectedPath(data.node);
				}
			},
			expand : function(event, data) {
				if (sender === source) {
					showSelectedPath(data.node);
				}
			},
			select : function(event, data) {
				if (sender === target) {
					var node = data.node;
					var selNodes = node.tree.getSelectedNodes();
					var uniqueNodes = [];
					var key = node.data.key;
					selNodes = node.tree.getSelectedNodes();
					// convert to title/key
					// array
					var selKeys = $.map(selNodes, function(node) {
						return "/" + node.key;
					});
					targetPaths = selectedPaths = selKeys.join(", ");
					$('.' + sender.toLowerCase() + 'SelectedPath').text(selectedPaths);
				}
			}
		});
	}
}
function showSelectedPath(node) {
	$('.sourceSelectedPath').text("/" + node.key);
}
