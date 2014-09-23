/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.fault.SdkFault;
import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.Project;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNode;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeRequest;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeType;
import com.blackducksoftware.soleng.idcopier.model.IDCSession;
import com.blackducksoftware.soleng.idcopier.model.IDCTree;
import com.google.gson.Gson;

/**
 * @author Ari Kamen
 * @date Sep 16, 2014
 *
 */
public class ProjectService {
	static Logger log = Logger.getLogger(ProjectService.class);

	private IDCSession session;
	private ProtexServerProxy proxy;
	private String ROOT = "/";

	public ProjectService(IDCSession session) {
		this.session = session;
		this.proxy = session.getProxy();
	}

	public String getProjectJSON(String projectID) {
		ProtexServerProxy proxy = session.getProxy();
		String jsonTree = getProjectCodeTree(projectID);

		log.debug("Got Tree: " + jsonTree);

		return jsonTree;
	}

	public String getFolderJSON(String projectID, String path) {
		System.err.println("PATH=" + path);

		ProtexServerProxy proxy = session.getProxy();
		String jsonTree = getProjectCodeTreeNodes(projectID, path);

		log.debug("Got Tree: " + jsonTree);

		return jsonTree;
	}

	/**
	 * Returns the JSON code path for the project
	 * 
	 * @param projectID
	 * @return
	 */
	public String getProjectCodeTree(String projectID) {
		String json = "";
		try {
			CodeTreeNodeRequest ctrRequest = new CodeTreeNodeRequest();
			ctrRequest.setDepth(-1);
			ctrRequest.setIncludeParentNode(true);
			List<CodeTreeNodeType> nodeTypes = ctrRequest.getIncludedNodeTypes();

			nodeTypes.add(CodeTreeNodeType.FILE);
			nodeTypes.add(CodeTreeNodeType.FOLDER);

			List<CodeTreeNode> nodes = proxy.getCodeTreeApi().getCodeTreeNodes(projectID, "/", ctrRequest);

			log.info("Got top level nodes, count: " + nodes.size());

			Gson gson = new Gson();
			json = gson.toJson(nodes);
		}

		catch (Exception e) {
			log.error("Could not convert project tree to JSON", e);
		}

		return json;
	}

	public String getProjectCodeTreeNodes(String projectID, String path) {
		String json = "";

		try {
			CodeTreeNodeRequest ctrRequest = new CodeTreeNodeRequest();
			ctrRequest.setDepth(1);
			ctrRequest.setIncludeParentNode(true);
			List<CodeTreeNodeType> nodeTypes = ctrRequest.getIncludedNodeTypes();

			nodeTypes.add(CodeTreeNodeType.FILE);
			nodeTypes.add(CodeTreeNodeType.FOLDER);

			List<CodeTreeNode> nodes = proxy.getCodeTreeApi().getCodeTreeNodes(projectID, "/", ctrRequest);

			log.info("Got top level nodes, count: " + nodes.size());

			List<String> folderNodes = new ArrayList<String>();
			List<String> fileNodes = new ArrayList<String>();

			HashMap<String, CodeTreeNode> folderLookup = new HashMap<String, CodeTreeNode>();
			HashMap<String, CodeTreeNode> fileLookup = new HashMap<String, CodeTreeNode>();

			for (CodeTreeNode codeTreeNode : nodes) {
				String name = codeTreeNode.getName().toLowerCase();

				if (!name.isEmpty()) {
					if (codeTreeNode.getNodeType() == CodeTreeNodeType.FILE) {
						fileNodes.add(name);
						fileLookup.put(name, codeTreeNode);
					} else {
						folderNodes.add(name);
						folderLookup.put(name, codeTreeNode);
					}
				}
			}

			Collections.sort(folderNodes);
			Collections.sort(fileNodes);

			List<IDCTree> treeNodes = new ArrayList<IDCTree>();

			for (String currentNodeString : folderNodes) {
				CodeTreeNode currentTreeNode = folderLookup.get(currentNodeString);
				String name = currentTreeNode.getName();
				String filePath = path + name;

				IDCTree node = new IDCTree(filePath, name, true);
				node.setLiClass("isLazy isFolder");

				treeNodes.add(node);
			}

			for (String currentNodeString : fileNodes) {
				CodeTreeNode currentTreeNode = fileLookup.get(currentNodeString);
				String name = currentTreeNode.getName();
				String filePath = path + name;

				treeNodes.add(new IDCTree(filePath, name, false));
			}

			Gson gson = new Gson();

			if (path.equals(ROOT)) {
				List<IDCTree> projectNodes = new ArrayList<IDCTree>();

				IDCTree rootNode = new IDCTree(ROOT, getProjectName(projectID), true);
				rootNode.addChildren(treeNodes);
				rootNode.setExpanded(true);
				projectNodes.add(rootNode);
				json = gson.toJson(projectNodes);
			} else {
				json = gson.toJson(treeNodes);
			}
		} catch (Exception e) {
			log.error("Could not convert project tree to JSON", e);
		}

		return json;
	}

	public String getProjectName(String projectID) {
		String projectName = "";

		try {
			Project project = proxy.getProjectApi().getProjectById(projectID);

			return project.getName();
		} catch (SdkFault e) {
			log.error("Unable to get project name", e);
		}

		return projectName;
	}
}
