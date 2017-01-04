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
jQuery(document).ready(
		function() {
			var logPath = 'logging/logData';

			var timeLineEntry = '<article class=\"timeline-entry\">';
			var timeLineEntryInner = '<div class=\"timeline-entry-inner\">';

			var success = 'success';
			var error = 'secondary';
			var info = 'info';
			var warning = 'warning';

			console.log('Reading in log JSON: ' + logPath);

			// Get the JSON data of the Log
			$.ajax({
				type : 'GET',
				url : logPath,
				success : function(data) {
					loadLogData(data);
				},
				error : function(data) {
					console.log('Unable to get the log JSON data');
				}
			});

			// Load the data into a timeline
			function loadLogData(data) {
				$('.timeline-centered').empty();

				$.each(JSON.parse(data), function(index, value) {
					if (value !== null) {
						// var json = JSON.parse(value);

						var level = value.level;
						var category = value.category;
						var message = value.message;

						var style = info;

						console.log('* ' + level);

						if (level.toUpperCase() === 'ERROR') {

							style = error;
						} else if (level.toUpperCase() === 'WARN') {
							style = warning;
						} else {
							style = info;
						}

						/*
						 * console.log(timeLineEntry);
						 * console.log(timeLineEntryInner); console.log("<div
						 * class=" + style + "><i class=\"entypo-suitcase\"></i></div>");
						 * console.log("<div class=\"timeline-label\">");
						 * console.log("<h4>" + category + "</h4>");
						 * console.log("<p>" + message + "</p>");
						 * console.log("</div></div></article>");
						 * console.log("");
						 */

						var output = timeLineEntry;
						output = output + timeLineEntryInner;
						output = output + "<div class=\"timeline-icon bg-"
								+ style + "\">";
						output = output + "<i class=\"entypo-suitcase\"></i>";
						output = output + "</div>";
						output = output + "<div class=\"timeline-label\">";
						output = output + "<h4>" + level.toUpperCase() + ": "
								+ category + "</h4>";
						output = output + "<p>" + message + "</p>";
						output = output + "</div></div></article>";
						$(".timeline-centered").append(output);

						/*
						 * $(".timeline-centered").append(timeLineEntry);
						 * $(".timeline-centered").append(timeLineEntryInner);
						 * $(".timeline-centered").append( "<div class=" +
						 * style + ">"); $(".timeline-centered").append( "<i
						 * class=\"entypo-suitcase\"></i>");
						 * $(".timeline-centered").append("</div>");
						 * $(".timeline-centered").append( "<div
						 * class=\"timeline-label\">");
						 * $(".timeline-centered").append( "<h4>" + category + "</h4>");
						 * $(".timeline-centered") .append("<p>" + message + "</p>");
						 * $(".timeline-centered") .append("</div></div></article>");
						 */

					}
				});
			}
		});
