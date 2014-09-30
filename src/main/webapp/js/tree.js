/**
 * Loads the tree in the main well.
 * @param sender
 * @param serverName
 * @param projectId
 */
function loadDynaTree(sender, serverName, projectId) 
{
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
   $('.' + sender.toLowerCase() + 'CodeTree').dynatree({
     title: "Source Code Tree",
     fx: {
       height: "toggle",
       duration: 200
     },
     checkbox: setAsCheckBox,
     // TODO: This does not appear to
     // be doing anything
     perist: true,
     // TODO: What is select mode?
     // What is '2'?
     selectMode: 2,
     autoFocus: false,
     initAjax: {
       // Gets the root node for
       // the project
       url: path + '/'
     },
     onActivate: function (dtnode) 
     {
    	 expandNode(dtnode, path, sender);
     },
     onExpand: function (dtnode) 
     {
    	 expandNode(dtnode, path, sender);
     },
     onClick: function (dtnode) 
     {
    	 expandNode(dtnode, path, sender);
     },

     onSelect: function (select, node) {
       if (sender === target) {
         var selNodes = node.tree.getSelectedNodes();
         var uniqueNodes = [];
         var key = node.data.key;

         /*
          * This doesn't do what
          * we wanted, and I
          * realized the flaw in
          * my thinking. I was
          * doing a simple check
          * to see if one path
          * contained the other,
          * but that won't be
          * correct.
          *
          * In reality what we
          * want is to:
          *
          * 1. Check to make sure
          * that the selected
          * path we are looking
          * at, isn't he path
          * being checked against
          * in the selected
          * paths.
          *
          * 2. If there is a file
          * name, strip that out.
          * We only want to deal
          * with folder names. If
          * we try for file
          * names, we will run
          * into situations where
          * we uncheck items with
          * very similar names
          *
          * Path 1:
          * /com/niles/test/path/README
          * Path 2:
          * /com/niles/test/path/README2
          *
          * Path 2 will contain
          * Path 1, but not be an
          * accurate check.
          *
          * So we need to check
          * to see if the node
          * type is a file or a
          * folder. If it is a
          * folder, we can just
          * do the check as is.
          * If it's a file, then
          * we will have to take
          * off the file name and
          * then compare the
          * folder that we are
          * checking against.
          * This should make it
          * more accurate.
          */

         /*
          * for (var i = 0; i <
          * selNodes.length; ++i) { //
          * Look to see if the
          * newly selected node
          * lives // inside of an
          * old node, or if an
          * old node // lives
          * inside the selected
          * node if
          * (((key.lastIndexOf(selNodes[i].data.key,
          * 0) === 0) ||
          * (selNodes[i].data.key.lastIndexOf(key,
          * 0) === 0)) &&
          * node.data.key !==
          * selNodes[i].data.key) {
          * if ($('.' +
          * sender.toLowerCase() +
          * 'CodeTree').dynatree("getTree").selectKey(selNodes[i].data.key).isSelected()) {
          * console.log("Unchecking: " +
          * selNodes[i].data.key);
          * $('.' +
          * sender.toLowerCase() +
          * 'CodeTree').dynatree("getTree").selectKey(selNodes[i].data.key).toggleSelect(); } } }
          */

         selNodes = node.tree.getSelectedNodes();
         // convert to title/key
         // array
         var selKeys = $.map(
         selNodes, function (
         node) {
           return "/" + node.data.key;
         });
         targetPaths = selectedPaths = selKeys.join(", ");
         $('.' + sender.toLowerCase() + 'SelectedPath').text(
         selectedPaths);
       }
     },
     debugLevel: 0
     // 0:quiet, 1:normal, 2:debug
   });
   $('.' + sender.toLowerCase() + 'CodeTree').dynatree("getTree").reload();
     }
 }

/**
 * Expands the node using lady loading.
 * Called from onExpand and onActivate
 * @param dtnode - The node of the tree
 * @param path - The RESTful path to the Controller
 * @param sender - Location (either source or target)
 */
function expandNode(dtnode, path, sender)
{
	if(dtnode.data == null)
	{
		console.log("Node data is empty...");
		return;
	}

	if (dtnode.data.isFolder && !dtnode.hasChildren()) 
	{
		dtnode.expand();
		
		// This is the full Controller path with
		var finalPath = path + dtnode.data.key;
		console.log("Passing in final RESTful path for node expansion: "
				+ finalPath);
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
		$('.' + sender.toLowerCase() + 'SelectedPath').text(
				"/" + dtnode.data.key);
	}
}



/**
 * TODO: What does this do?
 * 
 * @param event
 * @param nodes
 * @param node
 * @param hasChildren
 * @returns {Boolean}
 */
function openLazyNode(event, nodes, node, hasChildren) {
    if (hasChildren) {
      return false;
    }
    // node.lazyUrlJson = node.id;
  }