/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.ProtexServer;
import com.blackducksoftware.soleng.idcopier.service.LoginService;

/**
 *  Controller to handle display of login page and authentication
 *  
 *  @author Ari Kamen
 *  @date Sep 17, 2014
 *
 */

@Controller
@SessionAttributes(IDCViewModelConstants.PROTEX_SERVER) 
public class IDCLoginController
{
    static Logger log = Logger.getLogger(IDCLoginController.class);

    // Internal
    private static LoginService loginService = null;

    @RequestMapping(value = IDCPathConstants.LOGIN_MAIN_PATH)
    public ModelAndView personPage()
    {
	return new ModelAndView(IDCViewConstants.LOGIN_FORM, IDCViewModelConstants.PROTEX_SERVER,
		new ProtexServer());
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
    public ModelAndView processLogin(@ModelAttribute(IDCViewModelConstants.PROTEX_SERVER) ProtexServer server)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Logging in...: " + server.getServerName());
	// Login
	loginService = getLoginService(server);
	server = loginService.getServerInfo();
	
	modelAndView.addObject(IDCViewModelConstants.PROTEX_SERVER, server);
	modelAndView.addObject(IDCViewModelConstants.PROJECT_LIST, server.getProjects());

	modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
	return modelAndView;
    }
    
    /**
     * @param server
     * @return
     */
    private LoginService getLoginService(ProtexServer server)
    {
	loginService = new LoginService(server);
	return loginService;
    }
}
