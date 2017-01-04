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
package com.blackducksoftware.soleng.idcopier.constants;

public class IDCPathConstants {

	/**
	 * Login
	 */
	public static final String LOGIN_MAIN_PATH = "/login";
	public static final String LOGOUT_MAIN_PATH = "/logout";
	public static final String LOGIN_PROCESS_PATH = "/login.do";
	public static final String LOGIN_REDIRECT = "/redirect";
	public static final String LOGIN_NEW_SERVER = "/reloginServer";
	public static final String LOGIN_CONFIG = "/configSettings";
	/**
	 * Projects
	 */
	public static final String PROJECT_DISPLAY_TREE = "/processProject.do";
	public static final String GET_PROJECTS = "/getProjects";
	public static final String REFRESH_BOM = "/bomRefresh";
	public static final String REFRESH_BOM_STATUS = "/bomRefreshStatus";
	public static final String SESSION_INFO = "/sessionInfo";
	/**
	 * Tree
	 */
	public static final String TREE_EXPAND_NODE = "/treeExpandNode";
	public static final String TREE_UPDATE_NODES = "/treeUpdateNodes";

	/**
	 * Logging
	 */
	public static final String LOG_PATH = "/log";
	public static final String LOG_DATA_PATH = "logging/logData";

	/**
	 * Admin
	 */
	public static final String ADMIN_PATH = "/admin";

	/**
	 * Comments
	 */
	public static final String DISPLAY_BILL_OF_MATERIALS = "/displayBom";
	public static final String DISPLAY_COMMENTS = "/displayComments";
	public static final String COPY_COMMENTS = "/copyComments";

	/**
	 * Copying
	 */
	public static final String COPY_IDS = "/copyIDs";

	/**
	 * RESTful
	 */
	public static final String SERVERS = "/servers";

	public static final String TREE_NODES = "/targetNodes";
	public static final String SERVER = "/server";
	public static final String SERVER_NAME = "/{serverName}";
	// Either source or target
	public static final String LOCATION = "/{location}";
}
