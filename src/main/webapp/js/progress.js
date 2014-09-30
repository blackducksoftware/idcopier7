var progressDiv = $("#progressBar");

jQuery(document).ready(function () 
	{

	   /**
	    * Init progress bar
	    */
	   var progressLoader = progressDiv.percentageLoader({
	     width: 140,
	     height: 140,
	     progress: 0,
	     value: '0%'
	   })
	   progressDiv.hide();
	 
	  var previousPercent = -1;
	 
		 /**
	    * Performs a refresh on target project
	    */
	   $("#performRefreshButton").on('click', function () 
	   {
		 progressDiv.show();
	     var targetServer = $('.selectTargetServer').children(":selected").text();
	     var targetProjectId = $('.selectTargetProject').children(":selected").attr("id");

	     if (targetProjectId == null) {
	       displayNotificationMessage(warning, 'Unable to refresh BOM', 'Please select a target project!');
	       return false;
	     }

	     var refreshPath = 'bomRefresh' + '/' + targetServer + '/' + targetProjectId;
	     console.log("Submitting AJAX request to: " + refreshPath);
	     // Submit the refresh request
	     $.ajax({
	       type: 'POST',
	       url: refreshPath,
	       error: function (msg) {
	         console.log("Error submitting refresh: " + msg);
	       }
	     });
	     // Grab the status
	     getRefreshStatusForProject(
	     targetServer, targetProjectId);
	   });

	   function getRefreshStatusForProject(targetServer, targetProjectId) {
	     var statusPath = 'bomRefreshStatus' + '/' + targetServer + '/' + targetProjectId;
	     $.ajax({
	       type: 'GET',
	       url: statusPath,
	       success: function (jsonStatusResult) {
	         console.log('Status result: ' + jsonStatusResult);
	         updateRefreshProgress(jsonStatusResult, targetServer, targetProjectId);
	       },
	       error: function (msg) {
	         console.log(msg);
	       }
	     });
	   }

	   function updateRefreshProgress(jsonStatusString, server, projectId) {
	     var jsonObj = JSON.parse(jsonStatusString);
	     if (jsonObj == null) {
	       console.log("WARN: No JSON status information");
	       return false;
	     }

	     var percentComplete = jsonObj.percentComplete;
	     var refreshStage = jsonObj.refreshStage;

	     if (percentComplete === 100 && previousPercent === -1) {
	       progressLoader.setValue('0%');
	       progressLoader.setProgress(0);

	       getRefreshStatusForProject(server, projectId);
	     }

	     previousPercent = percentComplete;

	     progressLoader.setValue(percentComplete + '%' + " [" + refreshStage + "]");
	     progressLoader.setProgress(percentComplete / 100);

	     if (percentComplete < 100) {
	       getRefreshStatusForProject(server, projectId);
	     } else {
	       previousPercent = -1;
	     }
	   }
 });