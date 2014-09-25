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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

/**
 * This controller handles all copy requests whether through buttons or drap and
 * drop functionality.
 * 
 * @author Ari Kamen
 * @date Sep 25, 2014
 * 
 */
@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class IDCCopyController
{
    static Logger log = Logger.getLogger(IDCCopyController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(IDCPathConstants.COPY_IDS)
    public String copyIDs(
	    @RequestParam(value = IDCViewModelConstants.COPY_SOURCE_SERVER) String sourceServer,
	    @RequestParam(value = IDCViewModelConstants.COPY_TARGET_SERVER) String targetServer,
	    @RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_ID) String sourceProjectId,
	    @RequestParam(value = IDCViewModelConstants.COPY_TARGET_PROJECT_ID) String targetProjectId,
	    @RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PATH) String sourcePath,
	    @RequestParam(value = IDCViewModelConstants.COPY_TARGET_PATHS) String targetPath,
	    Model model)
    {
	String returnMsg = null;
	
	StringBuilder sb = new StringBuilder();
	sb.append("\n Source server: " + sourceServer);
	sb.append("\n Target server: " + targetServer);
	sb.append("\n Source project: " + sourceProjectId);
	sb.append("\n Target project: " + targetProjectId);
	sb.append("\n Source path: " + sourcePath);
	sb.append("\n Target path(s): " + targetPath);
	
	if(!sourceServer.equalsIgnoreCase(targetServer))
	{
	    log.error("Functionality not supported");
	    returnMsg = "Servers mismatch, functionality not supported!";
	    return returnMsg;
	}
	
	log.info("Attempting to copy: " + sb.toString());

	returnMsg = "Success!!!";
	
	return returnMsg;
	
    }
}
