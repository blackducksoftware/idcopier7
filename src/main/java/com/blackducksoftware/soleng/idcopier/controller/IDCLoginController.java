/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCServer;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
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

    // Internal
    private static LoginService loginService = null;
    private static List<IDCServer> servers = null;
    private static List<ProjectInfo> sourceProjects = null;
    private static List<ProjectInfo> destinationProjects = null;

    @RequestMapping(value = IDCPathConstants.LOGIN_MAIN_PATH)
    public ModelAndView displayLoginPage(
	    @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_SERVER, required = false) String serverCookie,
	    @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_USER, required = false) String userNameCookie,
	    @CookieValue(value = IDCViewModelConstants.IDC_COOKIE_PASSWORD, required = false) String passwordCookie)
    {
	IDCSession session = new IDCSession();

	/**
	 * This seems like a painful way to handle cookies...:-/
	 * TODO:  Find an alternate way
	 * -- AK
	 */
	if (serverCookie != null)
	{
	    session.setServerName(serverCookie);
	}
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
     * redirects to server info
     * 
     * @param student
     * @param model
     * @return
     */
    @RequestMapping(value = IDCPathConstants.LOGIN_PROCESS_PATH)
    public ModelAndView processLogin(
	    @ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
	    @RequestParam(value = IDCViewModelConstants.REMEMBER_COOKIES, required = false, defaultValue = "false") Boolean rememberCookie,
	    HttpServletResponse response)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Logging in...: " + session.getServerName());
	servers = new ArrayList<IDCServer>();
	servers.add(new IDCServer(session.getServerName()));

	// Login
	loginService = getLoginService(session);
	session = loginService.getSessionObject();

	// Process cookie information
	processCookies(rememberCookie, session, response);

	modelAndView.addObject(IDCViewModelConstants.IDC_SESSION, session);
	modelAndView.addObject(IDCViewModelConstants.PROJECT_LIST,
		session.getProjects());

	sourceProjects = session.getProjects();
	destinationProjects = session.getProjects();

	modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
	return modelAndView;
    }

    @RequestMapping("/servers")
    public String processServers()
    {
	return new Gson().toJson(servers);
    }

    @RequestMapping("/sourceProjects")
    public String processSourceProjects()
    {
	System.out.println(new Gson().toJson(sourceProjects));

	return new Gson().toJson(sourceProjects);
    }

    @RequestMapping("/destinationProjects")
    public String processDestinationProjects()
    {
	return new Gson().toJson(destinationProjects);
    }

    /**
     * @param server
     * @return
     */
    private LoginService getLoginService(IDCSession session)
    {
	loginService = new LoginService(session);
	return loginService;
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
	if (rememberCookie != null)
	{
	    Cookie serverCookie = new Cookie(
		    IDCViewModelConstants.IDC_COOKIE_SERVER,
		    session.getServerName());
	    response.addCookie(serverCookie);
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
