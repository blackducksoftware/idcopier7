package com.blackducksoftware.soleng.idcopier.model;

import java.io.Serializable;

public class IDCServer implements Serializable {
	private String ServerName;

	public IDCServer(String server) {
		this.ServerName = server;
	}

	public String getServerName() {
		return ServerName;
	}
}
