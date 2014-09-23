/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

/**
 * @author Ari Kamen
 * @date Sep 17, 2014
 * 
 */

@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class IDCProjectController
{
    static Logger log = Logger.getLogger(IDCProjectController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = IDCPathConstants.PROJECT_DISPLAY_TREE)
    public ModelAndView displayProjectTree(
	    @RequestParam(value = IDCViewModelConstants.PROJECT_SOURCE_ID) String projectId,
	    @RequestParam(value = IDCViewModelConstants.IDC_SERVER_NAME) String serverName,
	    @ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
	    Model model)
    {
	ModelAndView modelAndView = new ModelAndView();

	log.info("Processing project: " + projectId);

	try
	{
	    ProtexServerProxy proxy = loginService.getProxy(serverName);
	    ProjectService ps = new ProjectService(proxy);
	    String jsonTree = ps.getProjectJSON(projectId);

	    modelAndView.addObject(IDCViewModelConstants.PROJECT_JSON_TREE,
		    jsonTree);

	    modelAndView.setViewName(IDCViewConstants.PROJECT_PAGE);
	} catch (Exception e)
	{
	    log.error("Unable to display project tree", e);
	}

	return modelAndView;
    }

    @RequestMapping(value = "/{serverName}/{projectId}/{path}")
    public String getProjectNodes(
	    @PathVariable String serverName,
	    @PathVariable String projectId,
	    @PathVariable String path,
	    @ModelAttribute(IDCViewModelConstants.IDC_SESSION) IDCSession session,
	    Model model)
    {
	path = path.replaceFirst("ROOT", "/");
	log.info("Generating: " + serverName + projectId + path);

	String jsonTree = "";
	try
	{
	    ProtexServerProxy proxy = loginService.getProxy(serverName);
	    ProjectService ps = new ProjectService(proxy);
	    jsonTree = ps.getFolderJSON(projectId, path);
	} catch (Exception e)
	{
	    log.error("Connection not established");
	}

	return jsonTree;
    }
}
