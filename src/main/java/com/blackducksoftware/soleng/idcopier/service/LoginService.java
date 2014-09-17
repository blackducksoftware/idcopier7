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

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.ProjectApi;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNode;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeRequest;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeType;
import com.blackducksoftware.soleng.idcopier.model.ProtexServer;
import com.google.gson.Gson;

/**
 * @author Ari Kamen
 * @date Sep 15, 2014
 * 
 */
public class LoginService
{
    static Logger log = Logger.getLogger(LoginService.class);

    private boolean loggedIn = false;

    private ProtexServer serverInfo = new ProtexServer();
    private ProtexServerProxy protexProxy = null;

    public LoginService(ProtexServer protexServer)
    {
	try
	{
	    serverInfo = protexServer;

	    protexProxy = new ProtexServerProxy(protexServer.getServerName(),
		    protexServer.getUserName(), protexServer.getPassword());

	    ProjectApi pApi = protexProxy.getProjectApi();
	    List<ProjectInfo> projects = pApi.getProjectsByUser(protexServer
		    .getUserName());

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
     * 
     * @param projectID
     * @return
     */
    public String getProjectCodeTree(String projectID)
    {
	String json = "";
	try
	{
	    CodeTreeNodeRequest ctrRequest = new CodeTreeNodeRequest();
	    ctrRequest.setDepth(-1);
	    ctrRequest.setIncludeParentNode(true);
	    List<CodeTreeNodeType> nodeTypes = ctrRequest
		    .getIncludedNodeTypes();

	    nodeTypes.add(CodeTreeNodeType.FILE);
	    nodeTypes.add(CodeTreeNodeType.FOLDER);

	    List<CodeTreeNode> nodes = protexProxy.getCodeTreeApi()
		    .getCodeTreeNodes(projectID, "/", ctrRequest);

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
