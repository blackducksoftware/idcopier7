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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.soleng.idcopier.model.ProtexServer;
import com.blackducksoftware.soleng.idcopier.service.LoginService;

/**
 * Just a silly test controller to make sure spring-mvc works.
 * 
 * @author Ari Kamen
 * @date Sep 12, 2014
 * 
 */

@Controller
public class IDCopierController
{
    static Logger log = Logger.getLogger(IDCopierController.class);

    
    // Internal 
    private static LoginService loginService = null;
    
    @RequestMapping(value = "/hai", method = RequestMethod.GET)
    public String hi(
	    @RequestParam(value = "name", required = false) String name,
	    Model model)
    {
	String message = name + "!";
	log.info("check for message: " + message);
	model.addAttribute("message", message);
	return "idcopier7";
    }
    
    @RequestMapping(value = "/server", method = RequestMethod.GET)  
    public String showServer(ModelMap model)
    {
	loginService = getLoginService();
	ProtexServer serverInfo = loginService.getServerInfo();
	
	model.addAttribute("server", serverInfo);
	
	return "serverinfo";
    }

    
    
    /**
     * @return
     */
    private LoginService getLoginService()
    {
	// TODO Auto-generated method stub
	if(loginService == null)
	{
	    loginService = new LoginService();
	    return loginService;
	}
	else
	    return loginService;
    }
    
}
