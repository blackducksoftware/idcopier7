/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.constants;

/**
 * List of all the URLs referenced within controllers
 * 
 * @author Ari Kamen
 * @date Sep 17, 2014
 *
 */
public class IDCPathConstants {

	/**
	 * Login
	 */
	public static final String LOGIN_MAIN_PATH = "/login";
	public static final String LOGIN_PROCESS_PATH = "/login.do";
	public static final String LOGIN_REDIRECT = "/redirect";
	public static final String LOGIN_NEW_SERVER = "/reloginServer";
	/**
	 * Projects
	 */
	public static final String PROJECT_DISPLAY_TREE = "/processProject.do";

	/**
	 * RESTful
	 */
	public static final String SERVERS = "/servers";
	public static final String SOURCE_PROJECTS = "/sourceProjects";
	public static final String TARGET_PROJECTS = "/targetProjects";
	public static final String TREE_NODES = "/targetNodes";
	public static final String SERVER = "/server";
	public static final String SERVER_NAME = "/{serverName}";
	// Either source or target
	public static final String LOCATION = "/{location}";
}
