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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.ProjectModel;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

/**
 * @author Ari Kamen
 * @date Sep 17, 2014
 * 
 */

@Controller
@SessionAttributes(IDCViewModelConstants.IDC_SESSION) 
public class IDCProjectController
{
    static Logger log = Logger.getLogger(IDCProjectController.class);

    @RequestMapping(value = IDCPathConstants.PROJECT_DISPLAY_TREE)
    public ModelAndView processLogin(
	    @RequestParam(value = "project-id") String projectId,
	    @ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session, 
	    Model model)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Processing project: " + projectId);

	ProjectService ps = new ProjectService(session);
	String jsonTree = ps.getProjectJSON(projectId);

	modelAndView.addObject("jsonTree", jsonTree);

	modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
	return modelAndView;
    }
}
