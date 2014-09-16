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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.soleng.idcopier.model.ProcessProject;
import com.blackducksoftware.soleng.idcopier.model.ProjectModel;
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
    
    
    @RequestMapping(value="/login")  
    public ModelAndView personPage() {  
        return new ModelAndView("login/login", "protex-server", new ProtexServer());  
    }  
    
    /**
     * Login method, grabs the user's inputs, calls the loginserver
     * and redirects to server info
     * @param student
     * @param model
     * @return
     */
    @RequestMapping(value = "/login.do")
    public ModelAndView processLogin(@ModelAttribute ProtexServer server)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Logging in...: " + server.getServerName());
	// Login
	loginService = getLoginService(server);
	
	modelAndView.addObject("server", server);	
	modelAndView.addObject("project-list", server.getProjects());
	
	modelAndView.setViewName("serverinfo");
	return modelAndView;
    }
  
    /**
     * Displays the project information
     * @param server
     * @return
     */
    @RequestMapping(value = "/processProject.do")
    public ModelAndView processLogin(@RequestParam(value = "project-id", required = false) String projectId,
	    Model model)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Processing project: " + projectId);
	
	modelAndView.setViewName("projectInfo");

	return modelAndView;
    }
    
   
    // Just messing around
    
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
    public ModelAndView showServer()
    {
	List<ProjectModel> projectModelList = new ArrayList<ProjectModel>();
	
	ModelAndView mav = new ModelAndView("serverInfo");
	
	ProtexServer serverInfo = loginService.getServerInfo();
	
	mav.addObject("server", serverInfo);
	mav.addObject("projectList", projectModelList);
	mav.addObject("project-model", new ProjectModel());
	
	return mav;
    }

    
    
    /**
     * @param server 
     * @return
     */
    private LoginService getLoginService(ProtexServer server)
    {
	// TODO Auto-generated method stub
	if(loginService == null || !loginService.isLoggedIn())
	{
	    loginService = new LoginService(server);
	    return loginService;
	}
	else
	{
	    log.info("Already logged in...");
	    return loginService;
	}
    }
    
}
