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

/**
 * These are the object names that are passed back to the views
 * 
 * - Variables that are requests will be lower case separated by dashed - Variables being sent to the view will be camel cased following Java convention
 */
public class IDCViewModelConstants {
	/**
	 * Context
	 */
	public static final String IDC_WEB_CONTEXT = "idcopier7";
	/**
	 * login/auth related variables
	 */
	// Session object
	public static final String IDC_SESSION = "server";

	// This is the name of the server against which all connections are to be established
	public static final String IDC_SERVER_NAME = "server-name";

	// User select to remember or not remember cookies
	public static final String REMEMBER_COOKIES = "remember-cookies";

	// An error if there is one
	public static final String LOGIN_ERROR_MSG = "loginErrorMsg";

	// Cookie to remember the login information
	public static final String IDC_COOKIE_SERVER = "idcServerCookie";
	public static final String IDC_COOKIE_USER = "idcUserCookie";
	public static final String IDC_COOKIE_PASSWORD = "idcPasswordCookie";

	/**
	 * Server related variables
	 */

	public static final String LOCATION_SOURCE = "source";
	public static final String LOCATION_TARGET = "target";

	/**
	 * Project related variables
	 */
	// List of servers defined by the user
	public static final String SERVER_LIST = "serverList";
	// List of projects belonging to a particular server
	public static final String PROJECT_LIST = "projectList";
	public static final String PROJECT_ID = "project-id";
	public static final String PROJECT_SOURCE_ID = "selected-project-source-id";
	// Tree representing a particular project
	public static final String PROJECT_JSON_TREE = "projectJsonTree";
	// Config settings
	public static final String CONFIG_SETTINGS = "configSettings";
	/**
	 * Copy related variables
	 */
	// These are required
	public static final String COPY_SOURCE_SERVER = "copy-source-server";
	public static final String COPY_TARGET_SERVER = "copy-target-server";
	public static final String COPY_SOURCE_PROJECT_ID = "copy-source-project-id";
	public static final String COPY_SOURCE_PROJECT_NAME = "copy-source-project-name";
	public static final String COPY_TARGET_PROJECT_ID = "copy-target-project-id";
	public static final String COPY_SOURCE_PATH = "copy-source-path";
	public static final String COPY_TARGET_PATHS = "copy-target-paths";
	// This is for copying comments
	public static final String COPY_EXPRESS = "copy-express";
	public static final String COPY_COMMENT_IDS = "copy-comment-ids";

	// // These are optional
	// Should the copying recursively traverse the children
	public static final String COPY_RECURSIVE_OPTION = "recursive-option";
	// Do we defer the BOM Refresh?
	public static final String COPY_DEFER_BOM_REFRESH_OPTION = "defer-bom-option";
	// Do existing identifications get overwritten?
	public static final String COPY_OVERWRITE_OPTION = "overwrite-option";
	// Should the BOM Refresh do the whole project or only those files that changed recently?
	public static final String PARTIAL_BOM_OPTION = "partial-bom-option";
	// Pull parent IDs
	public static final String PULL_PARENT_IDS_OPTION = "pull-parent-ids-option";
	// Should comments on the comment copying page be appended?
	public static final String APPEND_COMMENTS_OPTION = "append-comments-option";

	/**
	 * Tree related variables
	 */
	public static final String TREE_NODE_PATH = "tree-node-path";
	public static final String TREE_LOADED_PATH = "tree-loaded-path";
	public static final String TREE_LAST_LOADED_PATH = "tree-last-loaded-path";
	/**
	 * Session related variables
	 */
	public static final String SESSION_USERNAME = "username";
	public static final String SESSION_VERSION = "version";
	/**
	 * Logout related variable
	 */
	public static final String LOGOUT_MESSAGE = "msg";
}
