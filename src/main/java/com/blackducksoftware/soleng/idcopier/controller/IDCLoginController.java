/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.soleng.idcopier.config.IDCServerParser;
import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;
import com.blackducksoftware.soleng.idcopier.model.IDCServer;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;
import com.google.gson.Gson;

/**
 * Controller to handle display of login page and authentication
 * 
 * @author Ari Kamen
 * @date Sep 17, 2014
 * 
 */

@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class IDCLoginController {
	static Logger log = Logger.getLogger(IDCLoginController.class);

	// This injects the configuration file
	@Autowired
	private IDCConfig config;
	@Autowired
	private LoginService loginService;
	@Autowired
	private ProjectService projectService;

	// Internal
	private static List<IDCServer> servers = null;

	@RequestMapping(value = IDCPathConstants.LOGIN_MAIN_PATH)
	public ModelAndView displayLoginPage(
			@CookieValue(value = IDCViewModelConstants.IDC_COOKIE_SERVER, required = false) String serverCookie,
			@CookieValue(value = IDCViewModelConstants.IDC_COOKIE_USER, required = false) String userNameCookie,
			@CookieValue(value = IDCViewModelConstants.IDC_COOKIE_PASSWORD, required = false) String passwordCookie) {

		IDCSession session = new IDCSession();

		/**
		 * This seems like a painful way to handle cookies...:-/ TODO: Find an alternate way -- AK
		 */
		if (serverCookie != null) {
			session.setServerURI(serverCookie);
		}
		if (userNameCookie != null) {
			session.setUserName(userNameCookie);
		}
		if (passwordCookie != null) {
			session.setPassword(passwordCookie);
		}

		return new ModelAndView(IDCViewConstants.LOGIN_FORM, IDCViewModelConstants.IDC_SESSION, session);
	}

	@RequestMapping(value = IDCPathConstants.LOGIN_REDIRECT)
	public ModelAndView redirect() {
		return new ModelAndView(IDCViewConstants.LOGIN_REDIRECT);
	}

	/**
	 * Login method, grabs the user's inputs, calls the loginserver and redirects to the project display page
	 * 
	 * This is the first and only login process that uses the user credentials
	 * 
	 * @param student
	 * @param model
	 * @return
	 */
	@RequestMapping(value = IDCPathConstants.LOGIN_PROCESS_PATH)
	public ModelAndView processLogin(
			@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
			@RequestParam(value = IDCViewModelConstants.REMEMBER_COOKIES, required = false, defaultValue = "false") Boolean rememberCookie,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();

		// Before we log in, process the servers
		servers = processServerListConfiguration(session);

		log.info("Logging in...: " + session.getServerURI());
		ProtexServerProxy proxy;
		try {
			/***
			 * This is the one time where a Proxy is received via server URI This is because the login page, at this time, does not provide aliases/names
			 */
			proxy = loginService.getProxyByServerURI(session.getServerURI());
			List<ProjectInfo> projects = projectService.getProjectsByUser(proxy, session.getUserName());

			// Process cookie information
			processCookies(rememberCookie, session, response);

			modelAndView.addObject(IDCViewModelConstants.IDC_SESSION, session);
			modelAndView.addObject(IDCViewModelConstants.SERVER_LIST, servers);
		} catch (Exception e) {
			log.error("Error logging in", e);
		}

		modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
		return modelAndView;
	}

	/**
	 * Processes a server change from the dropdown Return JSON project list TODO: This actually retreives a project list, consider refactoring?
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = IDCPathConstants.LOGIN_NEW_SERVER + IDCPathConstants.LOCATION)
	public String processLoginForServer(@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
			@RequestParam(value = IDCViewModelConstants.IDC_SERVER_NAME) String serverName,
			@PathVariable String location, HttpServletResponse response) {

		log.info("Processing a new login for server: " + serverName);

		List<ProjectInfo> projects = null;
		try {
			IDCServer server = loginService.getServerByName(serverName);
			ProtexServerProxy proxy = loginService.getProxy(server.getServerName());
			projects = projectService.getProjectsByServer(proxy, server);

		} catch (Exception e) {
			log.error("Unable to authenticate new server: " + serverName);
		}

		String projectJson = new Gson().toJson(projects);
		log.debug("Sending project JSON:" + projectJson);
		return projectJson;
	}

	/**
	 * Invokes the XML parser to process the server configuration file If parsing fails or is missing Session is defaulted and transformed into a server object
	 * 
	 * @param session
	 * @return
	 */
	private List<IDCServer> processServerListConfiguration(IDCSession session) {
		servers = new ArrayList<IDCServer>();
		log.info("Processing server configuration");
		String serverListLocation = config.getServerListLocation();

		if (serverListLocation != null) {
			File f = new File(serverListLocation);
			if (f.exists()) {
				IDCServerParser parser = new IDCServerParser();
				try {
					FileReader fr = new FileReader(f);
					servers = parser.processServerConfiguration(fr);
				} catch (FileNotFoundException e) {
					log.warn("Parsing error", e);
				}
			} else {
				log.warn("Server configuration file does not exist at: " + f);
			}
		} else {
			log.warn("No server list location available");
		}

		if (servers.size() == 0) {
			log.warn("No server configurations determined, defaulting...");
			IDCServer server = new IDCServer(session.getServerURI(), session.getUserName(), session.getPassword());
			servers.add(server);
		}

		// Key step!
		loginService.setServers(servers);

		return servers;
	}

	@RequestMapping(IDCPathConstants.SERVERS)
	public String processServers() {
		return new Gson().toJson(servers);
	}

	/**
	 * @param rememberCookie
	 * @param session
	 * @param response
	 */
	private void processCookies(Boolean rememberCookie, IDCSession session, HttpServletResponse response) {
		// User logged in, save it
		if (rememberCookie) {
			Cookie serverCookie = new Cookie(IDCViewModelConstants.IDC_COOKIE_SERVER, session.getServerURI());
			response.addCookie(serverCookie);
			Cookie userCookie = new Cookie(IDCViewModelConstants.IDC_COOKIE_USER, session.getUserName());
			response.addCookie(userCookie);
			Cookie passwordCookie = new Cookie(IDCViewModelConstants.IDC_COOKIE_PASSWORD, session.getPassword());
			response.addCookie(passwordCookie);
		}

	}
}
