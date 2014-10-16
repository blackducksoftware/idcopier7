/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.client.util.ServerAuthenticationException;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.soleng.idcopier.constants.IDCConfigurationConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;
import com.blackducksoftware.soleng.idcopier.model.IDCServer;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.model.UserServiceModel;
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
public class IDCLoginController
{
    static Logger log = Logger.getLogger(IDCLoginController.class);

    // This injects the configuration file
    @Autowired
    private IDCConfig config;
    @Autowired
    private UserServiceModel userServiceModel;


    // Internal
    private static List<IDCServer> servers = null;

    @RequestMapping(value = IDCPathConstants.LOGIN_MAIN_PATH)
    public ModelAndView displayLoginPage(
	    @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_SERVER, required = false) String serverCookie,
	    @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_USER, required = false) String userNameCookie,
	    @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_PASSWORD, required = false) String passwordCookie)
    {

	IDCSession session = new IDCSession();

	/**
	 * This seems like a painful way to handle cookies...:-/ TODO: Find an
	 * alternate way -- AK
	 */

	if (userNameCookie != null)
	{
	    session.setUserName(userNameCookie);
	}
	if (passwordCookie != null)
	{
	    session.setPassword(passwordCookie);
	}

	return new ModelAndView(IDCViewConstants.LOGIN_FORM,
		IDCViewModelConstants.IDC_SESSION, session);
    }

    @RequestMapping(value = IDCPathConstants.LOGIN_REDIRECT)
    public ModelAndView redirect()
    {
	return new ModelAndView(IDCViewConstants.LOGIN_REDIRECT);
    }

    /**
     * Login method, grabs the user's inputs, calls the loginserver and
     * redirects to the project display page
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
	    HttpServletResponse response, HttpSession httpSession)
    {
	ModelAndView modelAndView = new ModelAndView();
	LoginService loginService = userServiceModel.getNewLoginService();
	
	log.info("Logging in...: " + session.getUserName());
	httpSession.removeAttribute(IDCViewModelConstants.IDC_SESSION);
	ProtexServerProxy proxy;
	try
	{
	    // Before we log in, process the servers
	    servers = processServerListConfiguration(session);

	    if (servers == null || servers.size() == 0)
	    {
		throw new Exception("No servers configured.  Please see README");
	    }

	    /**
	     * This is a hokey implementation, but we are essentially logging in
	     * to the first server on the list. This is more of a courtesy than
	     * anything concrete.
	     */
	    IDCServer firstServer = servers.get(0);
	    proxy = loginService
		    .getProxyByServerURI(firstServer.getServerURI());

	    try
	    {
		proxy.validateCredentials();
	    } catch (ServerAuthenticationException e)
	    {
		log.error("Login failed: " + e.getMessage());
		String message = "Failed to authenticate against server: ["
			+ firstServer.getServerURI() + "]";
		message = message + " Cause: " + e.getCause();
		throw new Exception(message);
	    }

	    // Process cookie information
	    processCookies(rememberCookie, session, response);

	    modelAndView.addObject(IDCViewModelConstants.IDC_SESSION, session);
	    modelAndView.addObject(IDCViewModelConstants.SERVER_LIST, servers);
	} catch (Exception e)
	{
	    /**
	     * Capture login errors and redirect back to the form
	     */
	    String msg = e.getMessage();
	    if(e.getCause() != null)
		msg = e.getCause().getMessage();
	    modelAndView.addObject(IDCViewModelConstants.LOGIN_ERROR_MSG,msg);
	    log.error("Error logging in", e);
	    modelAndView.setViewName(IDCViewConstants.LOGIN_FORM);
	    return modelAndView;
	}

	
	
	modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
	return modelAndView;
    }

    /**
     * Processes a server change from the dropdown Return JSON project list
     * TODO: This actually retreives a project list, consider refactoring?
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = IDCPathConstants.LOGIN_NEW_SERVER
	    + IDCPathConstants.LOCATION)
    public String processLoginForServer(
	    @ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
	    @RequestParam(value = IDCViewModelConstants.IDC_SERVER_NAME) String serverName,
	    @PathVariable String location, HttpServletResponse response)
    {

	log.info("Processing a new login for server: " + serverName);
	LoginService loginService = userServiceModel.getLoginService();
	ProjectService projectService = userServiceModel.getProjectService();
	List<ProjectInfo> projects = null;
	try
	{
	    IDCServer server = loginService.getServerByName(serverName);
	    ProtexServerProxy proxy = loginService.getProxy(server
		    .getServerName());
	    projects = projectService.getProjectsByServer(proxy, server);

	} catch (Exception e)
	{
	    String msg = e.getMessage();
	    log.error("Unable to authenticate new server: " + serverName
		    + " cause: " + e.getMessage());
	    if(e.getCause() != null)
		msg = e.getCause().getMessage();
	    
	    return msg;
	}

	String projectJson = new Gson().toJson(projects);
	log.debug("Sending project JSON:" + projectJson);
	return projectJson;
    }

    /**
     * Parses the property key server.list which contains a list of comma
     * separated addresses. Uses the logged in user's credentials to fill out a
     * list of server beans.
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private List<IDCServer> processServerListConfiguration(IDCSession session)
	    throws Exception
    {
	servers = new ArrayList<IDCServer>();
	log.info("Processing server configuration");
	String serverListLocation = config.getServerListLocation();

	if (serverListLocation != null)
	{

	    File f = new File(serverListLocation);
	    if (f.exists())
	    {

		try(BufferedReader br = new BufferedReader(new FileReader(f)))
		{	
		    String server = br.readLine();
		    while(server != null)
		    {
			    IDCServer idcServer = new IDCServer(server,
				    session.getUserName(), session.getPassword());
			    servers.add(idcServer);
			    server = br.readLine();
		    }			    
		} catch (IOException e)
		{
		    log.warn("Parsing error", e);
		    throw new Exception(e.getMessage());
		}
		 log.info("Parsed server list, count: " + servers.size());
		
	    } else
	    {
		log.warn("Server configuration file does not exist at: " + f);
	    }
	} else
	{
	    throw new Exception("No server list available, looking for key: "
		    + IDCConfigurationConstants.SERVER_LIST);
	}

	// Key step that creates internal server maps
	LoginService loginService = userServiceModel.getLoginService();
	loginService.setServers(servers);
	
	return servers;
    }

    @RequestMapping(IDCPathConstants.SERVERS)
    public String processServers()
    {
	return new Gson().toJson(servers);
    }

    /**
     * @param rememberCookie
     * @param session
     * @param response
     */
    private void processCookies(Boolean rememberCookie, IDCSession session,
	    HttpServletResponse response)
    {
	// User logged in, save it
	if (rememberCookie)
	{
	    Cookie userCookie = new Cookie(
		    IDCViewModelConstants.IDC_COOKIE_USER,
		    session.getUserName());
	    response.addCookie(userCookie);
	    Cookie passwordCookie = new Cookie(
		    IDCViewModelConstants.IDC_COOKIE_PASSWORD,
		    session.getPassword());
	    response.addCookie(passwordCookie);
	}

    }
}
