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
// Globals
var checkedNode;
var tree;

/**
* Initializes the tree in the main well.
 * 
 * @param sender
 * @param serverName
 * @param projectId
 */
function initFancyTree(sender) 
{
	// Clear our the selected paths
	var selectedPathDiv = "." + sender + "SelectedPath";
	$(selectedPathDiv).empty();
	// Activate the IDCProjectController.expandPathNode()
	// Pass in an argument representing the node
	var path = "";
	var setAsCheckBox = false;
	if (sender === target) {
		setAsCheckBox = true;
	}

	$('#' + sender.toLowerCase() + 'CodeTree').fancytree({
		title : sender + 'Code Tree',
		extensions : [ "glyph","persist" ],
		glyph : {
			map : {
				doc : "glyphicon glyphicon-file document-node",
				docOpen : "glyphicon glyphicon-file document-node",
				checkbox : "glyphicon glyphicon-unchecked",
				checkboxSelected : "glyphicon glyphicon-check",
				checkboxUnknown : "glyphicon glyphicon-share",
				error : "glyphicon glyphicon-warning-sign",
				expanderClosed : "glyphicon glyphicon-plus-sign",
				expanderLazy : "glyphicon glyphicon-plus-sign",
				// expanderLazy: "glyphicon glyphicon-expand",
				expanderOpen : "glyphicon glyphicon-minus-sign",
				// expanderOpen: "glyphicon glyphicon-collapse-down",
				folder : "glyphicon glyphicon-folder-close folder-node",
				folderOpen : "glyphicon glyphicon-folder-open folder-node",
				loading : "glyphicon glyphicon-refresh loading-node"
			}
		},
		activeVisible : true, // Make sure, active nodes are visible (expanded).
		autoActivate : true, // Automatically activate a node when it is focused (using keys).
		autoCollapse : false, // Automatically collapse all siblings, when a node is expanded.
		autoScroll : true, // Automatically scroll nodes into visible area.
		clickFolderMode : 3, // 1:activate, 2:expand, 3:activate and expand, 4:activate (dblclick expands)
		checkbox : setAsCheckBox, // Show checkboxes.
		debugLevel : 1, // 0:quiet, 1:normal, 2:debug
		generateIds : false, // Generate id attributes like <span id='fancytree-id-KEY'>
		idPrefix : "ft_", // Used to generate node id´s like <span id='fancytree-id-<key>'>.
		icons : true, // Display node icons.
		keyboard : true, // Support keyboard navigation.
		keyPathSeparator : "_", // Used by node.getKeyPath() and tree.loadKeyPath().
		minExpandLevel : 1, // 1: root node is not collapsible
		selectMode : 2, // 1:single, 2:multi, 3:multi-hier
		titlesTabbable : true, // Node titles can receive keyboard focus
		source : {
			url : "/"
		},
		persist: 
		{
			expandLazy: true,
	        overrideSource: true, // true: cookie takes precedence over `source` data attributes.
	        store: "auto"
		},	
		renderNode: function(event, data) 
		{
			var nodeData = data.node.data;
			if(nodeData != null)
			{		
				// If we determine a node has pending items
				// We apply a pending-node css (main.css) to make it stand out
				if(nodeData.hasPending)
				{
					var $nodeSpan = $(data.node.span);
					$nodeSpan.find('> span.fancytree-title').text(data.node.title).addClass('pending-node');	
				}
			}			
		},
		ajaxDefaults : { // Used by initAjax option
			cache : false, // false: Append random '_' argument to the request url to prevent caching.
			timeout : 1, // >0: Make sure we get an ajax error for invalid URLs
			dataType : "json" // Expect json format and pass json object to callbacks.
		},
		activate : function(event, data) 
		{
			console.log("Node key: " + data.node.key);
			console.log("Node key path: " + data.node.getKeyPath());
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
		select : function(event, data) 
		{
			if (sender === target) 
			{
				var node = data.node;

				// Toggle parent/children checkboxes
				toggleCheckBoxes(node);

				// Build selection paths			
				var selNodes = node.tree.getSelectedNodes();
				selNodes = node.tree.getSelectedNodes();
				
				// convert to title/key
				var selKeys = $.map(selNodes, function(node) {
					return "/" + node.key;
				});
				
				targetPaths = selectedPaths = selKeys.join(", ");
				$('.' + sender.toLowerCase() + 'SelectedPath').text(selectedPaths);
			}
		}
	});
}

/**
 * Loads new URLs into the existing trees
 * @param sender
 * @param serverName
 * @param projectId
 */
function loadFancyTree(sender, serverName, projectId)
{
	var tree = 	$('.' + sender.toLowerCase() + 'CodeTree').fancytree("getTree");	
	var path = "treeExpandNode/" + serverName + '/' + projectId + "/?tree-node-path=";
	
	var url =  path + '/';
	
	var lazyLoadFunction  = function(event, data) {
		var node = data.node;
		var finalPath = path + encodeURIComponent(node.key);
		data.result = $.ajax({
			url : finalPath,
			dataType : "json"
		});
	}

	tree.options.source.url = url;
	tree.options.lazyLoad = lazyLoadFunction;
	tree.reload();
	return;
}

/**
 * Utilizes the internal persistence mechanism to reload the entire tree
 * Called by refresh mechanism
 * @param sender
 */
function refreshNodes(sender)
{
	var tree = 	$('.' + sender.toLowerCase() + 'CodeTree').fancytree("getTree");	
	tree.reload();
}

/**
 * Toggles children and parents of the checked node
 * So if selection has a parent, the parent is deselected
 * If parent has selected child, that child is deselected.
 * @param node
 */
function toggleCheckBoxes(node)
{
	// Visit all children and deselect them
	node.visit(function(visitedNode)
	{
		if(node.isSelected())
		{
			visitedNode.setSelected(false);
		}
	});	
		
	// Visit all parents and deselect them				
	node.visitParents(function(visitedNode)
	{
		if(node.isSelected())
		{
			visitedNode.setSelected(false);
		}
	});
}


function showSelectedPath(node) {
	$('.sourceSelectedPath').text("/" + node.key);
}

/**
 * Attempts to retrieve all paths specified by the user.
 * This is still a work in progress
 * 
 * @param sender
 * @param path
 */
function fetchPaths(sender, key) 
{
	var performScroll = true;
	var tree = 	$('.' + sender.toLowerCase() + 'CodeTree').fancytree("getTree");	
	tree.loadKeyPath(key, function(node, status) 
	{
		if (status == "loaded") {
			node.setActive();
	
		} else if (status == "ok") {
			node.setActive();
			node.toggleExpanded();	
		} else if (status == "notfound") {
			var seg = arguments[2], isEndNode = arguments[3];
		}
		else if(status == "error")
		{
			displayNotificationMessage(warning, "Could not expand tree", "Does path exist?");
			performScroll = false;
		}
		else
		{
			console.log("Unknown error with tree expansion: " + status);
		}
		
	});
	
	// Once selected, focus in.
	// Grab the HTML node
	if(performScroll)
	{
	    var activeNodeLI = tree.getActiveNode().li;
	    // Grab the HTMl container of our tree
		var treeContainer = tree.$container;
		treeContainer.animate(
		{ 
			scrollTop: $(activeNodeLI).offset().top - treeContainer.offset().top + treeContainer.scrollTop()
		}, 'slow');
	}
	
}