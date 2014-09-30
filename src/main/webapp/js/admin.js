jQuery(document).ready(
	function () {
	var serversJsonURI = "servers";
	var servers;

	$.getJSON(serversJsonURI, function (data) {
		servers = data;

		var dataSet = [];

		$.each(data, function (index, value) {
			dataSet.push([value.serverName, value.userName,
					getPasswordChars(value.password)]);

		});

		buildDataTable(dataSet);
	});

	$('#serverConfigurationTable').on(
		'click',
		'tr',
		function () {
		var selectedRow = $('#serverConfigurationTable')
			.DataTable().row(this).data();
		var selectedServerName = selectedRow[0];

		var selectedServer;

		for (var a = 0; a < servers.length; a++) {
			if (selectedServerName === servers[a].serverName) {
				selectedServer = servers[a];
			}
		}

		var serverURI = selectedServer.serverURI;
		var serverName = selectedServer.serverName;
		var userName = selectedServer.userName;
		var password = selectedServer.password;

		console.log(' > ' + selectedServer);
		$('#serverNameHeader').text(selectedServer.serverName);
		$('#serverURI').val(serverURI);
		$('#userName').val(userName);
		$('#password').val(password);
		$('#cPassword').val(password);
	});

	function buildDataTable(dataSet) {
		$('#serverConfigurationTable').dataTable({
			"paging" : false,
			"bInfo" : false,
			"data" : dataSet,
			"columns" : [{
					"title" : "Server"
				}, {
					"title" : "Username"
				}, {
					"title" : "Password",
					"width" : "10%"
				}
			]
		});
	}

	function getPasswordChars(pw) {
		var out = '';
		for (var a = 0; a < pw.length; a++) {
			out = out + '*';
		}
		return out;
	}
});