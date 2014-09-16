/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxyV6_2;
import com.blackducksoftware.sdk.protex.project.Project;
import com.blackducksoftware.sdk.protex.project.ProjectApi;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNode;
import com.blackducksoftware.sdk.protex.project.codetree.PartialCodeTree;
import com.blackducksoftware.sdk.protex.util.CodeTreeUtilities;
import com.blackducksoftware.soleng.idcopier.model.ProtexServer;
import com.google.gson.Gson;

/**
 *  @author Ari Kamen
 *  @date Sep 15, 2014
 *
 */
public class LoginService
{
    static Logger log = Logger.getLogger(LoginService.class);

    private boolean loggedIn = false;
    
    private ProtexServer serverInfo = new ProtexServer();
    private ProtexServerProxyV6_2 protexProxy = null;
    
    
    public LoginService(ProtexServer protexServer)
    {
	try{
	    serverInfo = protexServer;
	    
	    protexProxy = new ProtexServerProxyV6_2
		    (protexServer.getServerName(), protexServer.getUserName(), protexServer.getPassword());
	    
	    ProjectApi pApi = protexProxy.getProjectApi();
	    List<ProjectInfo> projects = pApi.getProjectsByUser(protexServer.getUserName());
	    
	    protexServer.setProjects(projects);
	    
	    
	    loggedIn = true;
	    log.info("Login successful");
	    
	  
	    
	} catch (Exception e)
	{
	    log.error("Error establishing login service: " + e.getMessage());
	    serverInfo.setError(e.getCause().getMessage());
	}
    }
    
    public Boolean isLoggedIn()
    {
	return loggedIn;
    }
    
    /**
     * @return
     */
    public ProtexServer getServerInfo()
    {
	return serverInfo;
    }

    /**
     * Returns the JSON code path for the project
     * @param projectID
     * @return
     */
    public String getProjectCodeTree(String projectID)
    {
	String json = "";
	try{
	    PartialCodeTree tree =  protexProxy.getCodeTreeApi().getCodeTree(projectID, "/", CodeTreeUtilities.INFINITE_DEPTH, true);
	    List<CodeTreeNode> nodes = tree.getNodes();
	    
	    log.info("Got top level nodes, count: " + nodes.size());
	    
	    Gson gson = new Gson();
		
	    json = gson.toJson(nodes);
	    
	}
	
	catch (Exception e)
	{
	    log.error("Could not convert project tree to JSON", e);
	}
	
	return json;
    }
    
}
