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
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNode;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeRequest;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeType;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.google.gson.Gson;

/**
 *  @author Ari Kamen
 *  @date Sep 16, 2014
 *
 */
public class ProjectService
{
    static Logger log = Logger.getLogger(ProjectService.class);

    
    private IDCSession session;
    private ProtexServerProxy proxy;
    
    public ProjectService(IDCSession session)
    {
	this.session = session;
	this.proxy = session.getProxy();
    }
    
    public String getProjectJSON(String projectID)
    {
	ProtexServerProxy proxy = session.getProxy();
	String jsonTree = getProjectCodeTree(projectID);
	
	log.debug("Got Tree: " + jsonTree);
	
	return jsonTree;
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

	    List<CodeTreeNode> nodes = proxy.getCodeTreeApi()
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
