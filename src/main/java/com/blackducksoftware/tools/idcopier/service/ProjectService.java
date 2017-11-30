/**
 * IDCopier
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 *
 */
package com.blackducksoftware.tools.idcopier.service;

import com.blackducksoftware.sdk.fault.SdkFault;
import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.Project;
import com.blackducksoftware.sdk.protex.project.ProjectApi;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;
import com.blackducksoftware.sdk.protex.project.bom.BomApi;
import com.blackducksoftware.sdk.protex.project.bom.BomProgressStatus;
import com.blackducksoftware.sdk.protex.project.codetree.*;
import com.blackducksoftware.sdk.protex.util.CodeTreeUtilities;
import com.blackducksoftware.tools.idcopier.model.IDCServer;
import com.blackducksoftware.tools.idcopier.model.IDCSession;
import com.blackducksoftware.tools.idcopier.model.IDCTree;
import com.blackducksoftware.tools.idcopier.model.ProjectComparator;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Service layer to handle project requests
 *
 * @author Ari Kamen
 * @date Sep 16, 2014
 */
public class ProjectService implements Serializable {
    static Logger log = Logger.getLogger(ProjectService.class);

    private IDCSession session;
    private ProtexServerProxy proxy;
    private String ROOT = "/";

    // Map of projects for caching lookup
    private Map<String, List<ProjectInfo>> projectMap;
    // Map of BOM Apis per proxy
    private Map<ProtexServerProxy, BomApi> bomApis;

    public ProjectService() {
        projectMap = new HashMap<String, List<ProjectInfo>>();
        bomApis = new HashMap<ProtexServerProxy, BomApi>();
    }

    /**
     * Gets projects from internal map
     *
     * @param server
     * @param proxy
     * @return
     * @throws Exception
     */
    public List<ProjectInfo> getProjectsByServer(ProtexServerProxy proxy, IDCServer server) throws Exception {
        this.proxy = proxy;
        List<ProjectInfo> projects = projectMap.get(server.getServerName());
        if (projects == null) {
            projects = getProjectsByUser(proxy, server.getUserName());
            for (ProjectInfo pinfo : projects) {
                String projectName = pinfo.getName();
                String strippedName = StringUtils.stripAccents(projectName);
                pinfo.setName(strippedName);
            }
            projectMap.put(server.getServerName(), projects);
        } else {
            return projects;
        }
        Collections.sort(projects, new ProjectComparator());
        return projects;
    }

    public String getProjectJSON(ProtexServerProxy proxy, String projectID) {
        this.proxy = proxy;
        String jsonTree = getProjectCodeTree(projectID);

        log.debug("Got Tree: " + jsonTree);

        return jsonTree;
    }

    public String getFolderJSON(ProtexServerProxy proxy, String projectID, String path) {
        this.proxy = proxy;
        log.debug("PATH=" + path);

        String jsonTree = getProjectCodeTreeNodesWithCount(projectID, path);

        log.debug("Got Tree: " + jsonTree);

        return jsonTree;
    }

    @Deprecated
    public List<ProjectInfo> getProjectsByUser(ProtexServerProxy proxy, String userName) throws Exception {
        this.proxy = proxy;
        ProjectApi pApi = proxy.getProjectApi();

        List<ProjectInfo> projects = null;
        try {
            projects = pApi.getProjectsByUser(userName);
        } catch (SdkFault e) {
            throw new Exception("Unable to get projects for user" + e.getMessage());
        }

        return projects;
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
        } catch (Exception e) {
            log.error("Could not convert project tree to JSON", e);
        }

        return json;
    }

    /**
     * Returns the status of BOM refresh
     *
     * @param proxy
     * @param projectId
     * @return
     */
    public BomProgressStatus getBOMRefreshStatus(ProtexServerProxy proxy, String projectId) {
        this.proxy = proxy;
        BomProgressStatus status = null;
        BomApi bomApi = getBomApiForProxy(proxy);
        try {
            status = bomApi.getRefreshBomProgress(projectId);
            if (status != null)
                log.debug("Got status object:" + status.getPercentComplete());
        } catch (SdkFault e) {
            log.error("Error getting status for project ID: " + projectId);
            log.error("Reason: " + e.getMessage());
        }

        return status;
    }

    /**
     * Gets the tree with all its paths and pending counts.
     *
     * @param projectID
     * @param path
     * @return
     */
    public String getProjectCodeTreeNodesWithCount(String projectID, String path) {
        String json = "";

        try {
            int pendingCount = 0;

            CodeTreeNodeRequest ctrRequest = new CodeTreeNodeRequest();
            ctrRequest.setDepth(CodeTreeUtilities.DIRECT_CHILDREN);
            ctrRequest.setIncludeParentNode(false);
            ctrRequest.getIncludedNodeTypes().addAll(CodeTreeUtilities.ALL_CODE_TREE_NODE_TYPES);
            ctrRequest.getCounts().add(NodeCountType.PENDING_ID_ALL);

            List<CodeTreeNode> nodes = proxy.getCodeTreeApi().getCodeTreeNodes(projectID, path, ctrRequest);

            log.debug("Got nodes for '" + path + "' (count: " + nodes.size() + ")");

            List<String> folderNodes = new ArrayList<String>();
            List<String> fileNodes = new ArrayList<String>();

            HashMap<String, CodeTreeNode> folderLookup = new HashMap<String, CodeTreeNode>();
            HashMap<String, CodeTreeNode> fileLookup = new HashMap<String, CodeTreeNode>();

            for (CodeTreeNode codeTreeNode : nodes) {
                String name = codeTreeNode.getName();
                pendingCount += getCounts(codeTreeNode.getNodeCounts());

                if (codeTreeNode.getNodeType() == CodeTreeNodeType.FILE) {
                    fileNodes.add(name);
                    fileLookup.put(name, codeTreeNode);
                } else {
                    folderNodes.add(name);
                    folderLookup.put(name, codeTreeNode);
                }
            }

            Collections.sort(folderNodes, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(fileNodes, String.CASE_INSENSITIVE_ORDER);

            List<IDCTree> treeNodes = new ArrayList<IDCTree>();

            CodeTreeNode currentTreeNode;
            String name;
            String filePath;

            for (String currentNodeString : folderNodes) {
                currentTreeNode = folderLookup.get(currentNodeString);
                name = currentTreeNode.getName();
                filePath = name;
                int count = getCounts(currentTreeNode.getNodeCounts());

                treeNodes.add(new IDCTree(filePath, name, true, count));
            }

            for (String currentNodeString : fileNodes) {
                currentTreeNode = fileLookup.get(currentNodeString);
                name = currentTreeNode.getName();
                filePath = name;
                int count = getCounts(currentTreeNode.getNodeCounts());

                treeNodes.add(new IDCTree(filePath, name, false, count));
            }

            Gson gson = new Gson();

            if (path.equals("/")) {
                List<IDCTree> projectNodes = new ArrayList<IDCTree>();

                IDCTree rootNode = new IDCTree("", getProjectName(projectID), true, pendingCount);
                rootNode.setExpand(true);
                rootNode.addChildren(treeNodes);
                projectNodes.add(rootNode);
                json = gson.toJson(projectNodes);
            } else {
                json = gson.toJson(treeNodes);
            }
        } catch (Exception e) {
            // log.error("Could not convert project tree to JSON", e);

            List<IDCTree> projectNodes = new ArrayList<IDCTree>();
            IDCTree rootNode = new IDCTree("", getProjectName(projectID), true, 0);
            projectNodes.add(rootNode);

            return new Gson().toJson(projectNodes);
        }

        return json;
    }

    public boolean isValidPath(String projectID, String path) {
        try {
            List<SourceFileInfoNode> info = proxy.getCodeTreeApi().getFileInfo(projectID, path, 1, false, CharEncoding.BASE_64);
            if (info != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public String constructPath(String path, String name) {
        String out = "";

        log.info(path + name);

        if (path.endsWith("/")) {
            out = path + name;
        } else {
            out = path + "/" + name;
        }

        log.info(out);

        return out;
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

    public int getCounts(List<NodeCount> counts) {
        int count = 0;

        if (counts != null)
            for (NodeCount counter : counts)
                count += counter.getCount();


        return count;
    }

    /**
     * Uses the internal map to quickly fetch a BOM Api
     *
     * @param currentProxy - User supplied proxy
     * @return
     */
    private BomApi getBomApiForProxy(ProtexServerProxy currentProxy) {
        BomApi bomApi = bomApis.get(currentProxy);
        if (bomApi == null) {
            bomApi = currentProxy.getBomApi();
            bomApis.put(currentProxy, bomApi);
        }

        return bomApi;
    }
}
