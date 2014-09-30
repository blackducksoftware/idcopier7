jQuery(document).ready(
		function() {
			var serversJsonURI = "servers";
			var servers;

			$.getJSON(serversJsonURI, function(data) {
				servers = data;

				var dataSet = [];

				$.each(data, function(index, value) {
					dataSet.push([ value.serverName, value.userName,
							getPasswordChars(value.password) ]);

				});

				console.log(dataSet);
				buildDataTable(dataSet);
			});

			function buildDataTable(dataSet) {
				$('#serverConfigurationTable').dataTable({
					"data" : dataSet,
					"paging" : false,
					"bInfo" : false,
					"columns" : [ {
						"title" : "Server"
					}, {
						"title" : "Username",
						"class" : "center"
					}, {
						"title" : "Password",
						"class" : "center"
					} ]
				});
			}

			function getPasswordChars(pw) {
				var out = '';
				for ( var a = 0; a < pw.length; a++) {
					out = out + '*';
				}
				return out;
			}
		});