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
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.service.LoginService;

/**
 *  Controller to handle display of login page and authentication
 *  
 *  @author Ari Kamen
 *  @date Sep 17, 2014
 *
 */

@Controller
@SessionAttributes(IDCViewModelConstants.IDC_SESSION) 
public class IDCLoginController
{
    static Logger log = Logger.getLogger(IDCLoginController.class);

    // Internal
    private static LoginService loginService = null;

    @RequestMapping(value = IDCPathConstants.LOGIN_MAIN_PATH)
    public ModelAndView personPage()
    {
	return new ModelAndView(IDCViewConstants.LOGIN_FORM, IDCViewModelConstants.IDC_SESSION,
		new IDCSession());
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
    public ModelAndView processLogin(@ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Logging in...: " + session.getServerName());
	// Login
	loginService = getLoginService(session);
	session = loginService.getSessionObject();
	
	modelAndView.addObject(IDCViewModelConstants.IDC_SESSION, session);
	modelAndView.addObject(IDCViewModelConstants.PROJECT_LIST, session.getProjects());

	modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
	return modelAndView;
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
}
