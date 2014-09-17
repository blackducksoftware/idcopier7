/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import java.io.Serializable;
import java.util.List;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;

/**
 * Session object containing relevant information
 * 
 * @author Ari Kamen
 * @date Sep 15, 2014
 *
 */
public class IDCSession implements Serializable {
	private String serverName;
	private String userName;
	private String password;
	private String error;
	private List<ProjectInfo> projects;
	private ProtexServerProxy proxy;
	
	public IDCSession() {

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<ProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectInfo> projects) {
		this.projects = projects;
	}

	public ProtexServerProxy getProxy()
	{
	    return proxy;
	}

	public void setProxy(ProtexServerProxy proxy)
	{
	    this.proxy = proxy;
	}
}
