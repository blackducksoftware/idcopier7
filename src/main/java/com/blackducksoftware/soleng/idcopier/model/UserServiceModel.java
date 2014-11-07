/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

/**
 *  This is the model that houses user specific services 
 *  (login, project, etc)
 *  
 *  Because it is scoped as a session object in our WebConfig, each user
 *  has his/her own dedicated services and caching.
 * 
 *  @author Ari Kamen
 *  @date Oct 16, 2014
 *
 */
public class UserServiceModel implements Serializable
{
    private LoginService loginService;
    private ProjectService projectService;
    
    
    public LoginService getNewLoginService() {  
	loginService = new LoginService();  
	projectService = new ProjectService();
        return loginService;  
    }  
    /** 
     * Returns the model for this view-model. 
     */  
    public LoginService getLoginService() {  
        if (loginService == null) {  
            throw new RuntimeException("Login Service has not been initialized yet.");  
        }  
        return loginService;  
    }  
    
    public ProjectService getProjectService()
    {
	if(projectService == null)
	    return new ProjectService();
	else
	    return projectService;
    }
}
