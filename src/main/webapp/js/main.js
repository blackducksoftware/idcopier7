jQuery(document).ready(
		function() {
			var selectSourceServer = "Select Source Server";
			var selectDestinationServer = "Select Destinaton Server";
			var selectSourceProject = "Select Source Project";
			var selectDestinationProject = "Select Destinaton Project";

			/**
			 * Skipping a bunch in here for now, because we are only working
			 * with one server. So I am going to just load all of the projects
			 * to the drop downs and worry about the rest later
			 * 
			 * -NM
			 */
			$.getJSON('servers',
					function(data) {
						if (data.length === 1) {
							$('.selectSourceServer').empty();
							$('.selectDestinationServer').empty();

							$.each(data, function(index, value) {
								$('.selectSourceServer').append(
										"<option>" + value.ServerName
												+ "</option>");

								$('.selectDestinationServer').append(
										"<option>" + value.ServerName
												+ "</option>");
							});
						} else {
							$('.selectSourceServer').empty();
							$('.selectDestinationServer').empty();

							$('.selectSourceServer').append(
									"<option>" + selectSourceServer
											+ "</option>");

							$('.selectDestinationServer').append(
									"<option>" + selectDestinationServer
											+ "</option>");

							$.each(data, function(index, value) {
								$('.selectSourceServer').append(
										"<option>" + value.ServerName
												+ "</option>");

								$('.selectDestinationServer').append(
										"<option>" + value.ServerName
												+ "</option>");
							});
						}
					});

			/**
			 * Set the source projects
			 */
			$.getJSON('sourceProjects', function(data) {
				$('.selectSourceProject').empty();

				$('.selectSourceProject').append(
						"<option>" + selectSourceProject + "</option>");

				$.each(data, function(index, value) {
					$('.selectSourceProject').append(
							"<option id=\"" + value.projectId + "\">"
									+ value.name + "</option>");
				});

			});

			/**
			 * Set the destination projects
			 */
			$.getJSON('destinationProjects', function(data) {
				$('.selectDestinationProject').empty();

				$('.selectDestinationProject').append(
						"<option>" + selectDestinationProject + "</option>");

				$.each(data, function(index, value) {
					$('.selectDestinationProject').append(
							"<option id=\"" + value.projectId + "\">"
									+ value.name + "</option>");
				});
			});

			$(".selectSourceServer").change(function() {
				// Wont do anything since we're only working with one
				// server so no change will happen
				if (this.value !== selectSourceServer) {
					console.log("source server = " + this.value);
				}
			});

			$(".selectDestinationServer").change(function() {
				// Wont do anything since we're only working with one
				// server so no change will happen
				if (this.value !== selectDestinationServer) {
					console.log("destination server = " + this.value);
				}
			});

			$(".selectSourceProject")
					.change(
							function() {
								if (this.value !== selectSourceProject) {
									console.log("source project = "
											+ this.value);
									console.log("source project ID = "
											+ $(this).children(":selected")
													.attr("id"));
								}
							});

			$(".selectDestinationProject")
					.change(
							function() {
								if (this.value !== selectDestinationProject) {
									console.log("source project = "
											+ this.value);
									console.log("source project ID = "
											+ $(this).children(":selected")
													.attr("id"));
								}
							});
		});

/*
 * jQuery(document).ready(function() { $.ajax({ url : 'servers', success :
 * function(data) { alert(data[1]); // $("#selectSourceServer").append($("<option></option>").attr("value", //
 * data[0].ServerName).text(server.ServerName); } }); });
 */