/**
 * Copyright (C)2014 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 */

/**
 *
 */
package com.blackducksoftware.soleng.idcopier.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.soleng.idcopier.model.IDCServer;

/**
 * @author Ari Kamen
 * @date Sep 15, 2014
 *
 */

public class LoginService implements Serializable {
	static Logger log = Logger.getLogger(LoginService.class);

	private boolean loggedIn = false;

	// Map of servers by their names, where names are hostnames
	private HashMap<String, IDCServer> serverMap = new HashMap<String, IDCServer>();
	// Map of established proxies, a proxy is established upon a successful
	private HashMap<String, ProtexServerProxy> proxyMap = new HashMap<String, ProtexServerProxy>();

	public LoginService() {
		serverMap = new HashMap<String, IDCServer>();
		proxyMap = new HashMap<String, ProtexServerProxy>();
	}

	/**
	 * The first login
	 *
	 * @param session
	 */
	public IDCServer getServerByName(String serverName) throws Exception {
		IDCServer server = serverMap.get(serverName);
		if (server == null)
			throw new Exception("Unable to find server with name: " + serverName);
		return server;
	}

	/**
	 * Retrieves a connection for that server
	 *
	 * @param serverName
	 * @return
	 * @throws Exception
	 */
	public ProtexServerProxy getProxy(String serverName) throws Exception {
		ProtexServerProxy proxy = null;
		IDCServer server = serverMap.get(serverName);
		if (server == null)
			throw new Exception("Unable to determine IDCServer with name: " + serverName);

		proxy = proxyMap.get(serverName);
		if (proxy == null) {
			proxy = createProxy(server);
			proxyMap.put(serverName, proxy);
		}
		return proxy;
	}

	/**
	 * Retrieves a proxy object by URI
	 *
	 * @param serverURI
	 * @return
	 * @throws Exception
	 */
	public ProtexServerProxy getProxyByServerURI(String serverURI) throws Exception {
		IDCServer ourServer = null;
		ProtexServerProxy proxy = null;
		String hostName = IDCServer.getHostFromURI(serverURI);
		ourServer = serverMap.get(hostName);

		if (ourServer == null)
			throw new Exception("Could not find server with name: " + ourServer);
		proxy = proxyMap.get(ourServer.getServerName());
		if (proxy == null) {
			proxy = createProxy(ourServer);
			proxyMap.put(ourServer.getServerName(), proxy);
		}

		return proxy;
	}

	/**
	 * This provides the list of server objects for the service to manage Populates the internal map for future lookup
	 */
	public void setServers(List<IDCServer> servers) {
		for (IDCServer server : servers) {
			serverMap.put(server.getServerName(), server);
		}
	}

	/**
	 * Establish a new proxy
	 *
	 * @param server
	 * @return
	 */
	private ProtexServerProxy createProxy(IDCServer server) {
		ProtexServerProxy proxy = null;
		try {
			proxy = new ProtexServerProxy(server.getServerURI(), server.getUserName(), server.getPassword());

			proxy.validateCredentials();

			server.setLoggedIn(true);
			log.info("Login successful");

		} catch (Exception e) {
			log.error("Error logging in: " + e.getMessage());
			server.setLoginError(e.getCause().getMessage());
		}

		return proxy;
	}

	public Boolean isLoggedIn() {
		return loggedIn;
	}
}
