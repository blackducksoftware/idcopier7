/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.constants;

/**
 * These are the object names that are passed back to the views
 * 
 *  - Variables that are requests will be lower case separated by dashed
 *  - Variables being sent to the view will be camel cased following Java convention
 *  
 * @author Ari Kamen
 * @date Sep 17, 2014
 * 
 */
public class IDCViewModelConstants
{
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
    public static final String PROJECT_SOURCE_ID = "selected-project-source-id";
    // Tree representing a particular project
    public static final String PROJECT_JSON_TREE = "projectJsonTree";

}
