/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.ProjectApi;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;

/**
 * @author Ari Kamen
 * @date Sep 15, 2014
 * 
 */
public class LoginService {
	static Logger log = Logger.getLogger(LoginService.class);

	private boolean loggedIn = false;

	private IDCSession serverInfo = new IDCSession();
	private ProtexServerProxy protexProxy = null;

	public LoginService(IDCSession session) {
		try {
			serverInfo = session;

			protexProxy = new ProtexServerProxy(session.getServerName(), session.getUserName(), session.getPassword());

			ProjectApi pApi = protexProxy.getProjectApi();
			List<ProjectInfo> projects = pApi.getProjectsByUser(session.getUserName());

			session.setProjects(projects);
			session.setProxy(protexProxy);

			loggedIn = true;
			log.info("Login successful");

		} catch (Exception e) {
			log.error("Error establishing login service: " + e.getMessage());
			serverInfo.setError(e.getCause().getMessage());
		}
	}

	public Boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @return
	 */
	public IDCSession getSessionObject() {
		return serverInfo;
	}

}
