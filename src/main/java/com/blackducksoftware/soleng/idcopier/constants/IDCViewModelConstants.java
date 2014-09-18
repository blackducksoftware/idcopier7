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
     * login/auth related variables
     */
    // Session object
    public static final String IDC_SESSION = "server";

    /**
     * Project related variables
     */
    // List of projects belonging to a particular server
    public static final String PROJECT_LIST = "project-list";
    public static final String PROJECT_SOURCE_ID = "project-source-id";
    // Tree representing a particular project
    public static final String PROJECT_JSON_TREE = "projectJsonTree";

}
