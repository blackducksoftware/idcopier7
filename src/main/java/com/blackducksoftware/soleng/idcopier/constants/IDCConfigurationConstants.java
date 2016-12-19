/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.constants;

/**
 * Constants found in the properties file
 * 
 * @author Ari Kamen
 * @date Sep 22, 2014
 *
 */
public class IDCConfigurationConstants {
	// Property file keys
	public static final String SERVER_LIST_LOCATION = "idcopier.servers.location";
	public static final String SERVER_LIST = "idcopier.server.list";
	public static final String SESSION_TIMEOUT = "idcopier.web.timeout";

	// Options
	public static final String OPTION_DEFER_BOM = "idcopier.defer.bom.refresh";
	public static final String OPTION_RECURSIVE = "idcopier.recursive";
	public static final String OPTION_OVERWRITE_IDS = "idcopier.overwrite.ids";
	public static final String OPTION_PARTIAL_BOM_REFRESH = "idcopier.partial.bom.refresh";
	public static final String OPTION_PULL_PARENT_IDS = "idcopier.pull.parent.ids";
	// Options - Comments
	public static final String OPTION_APPEND_COMMENTS = "idcopier.append.comments";

	// Server Configuration file
	public static final String SERVER_CONFIG_SERVER_LIST_NODE = "servers";
	public static final String SERVER_CONFIG_SERVER_NODE = "server";
	public static final String SERVER_CONFIG_SERVER_URI = "uri";
	public static final String SERVER_CONFIG_SERVER_ALIAS = "alias";
	public static final String SERVER_CONFIG_SERVER_USERNAME = "user.name";
	public static final String SERVER_CONFIG_SERVER_PASSWORD = "password";
}
