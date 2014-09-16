/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.service;

import org.apache.log4j.Logger;

/**
 *  @author Ari Kamen
 *  @date Sep 16, 2014
 *
 */
public class ProjectService
{
    static Logger log = Logger.getLogger(ProjectService.class);

    
    private LoginService ls;
    public ProjectService(LoginService ls)
    {
	this.ls = ls;
    }
    
    public String getProjectJSON(String projectID)
    {

	String jsonTree = ls.getProjectCodeTree(projectID);
	
	log.debug("Got Tree: " + jsonTree);
	
	return jsonTree;
    }
}
