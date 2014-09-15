/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

/**
 *  @author Ari Kamen
 *  @date Sep 15, 2014
 *
 */
public class ProtexServer
{
    private String serverName;
    private String userName;
    private String password;
    private String error;
    
    public ProtexServer()
    {
	
    }
    
    public String getPassword()
    {
	return password;
    }
    public void setPassword(String password)
    {
	this.password = password;
    }
    public String getUserName()
    {
	return userName;
    }
    public void setUserName(String userName)
    {
	this.userName = userName;
    }
    public String getServerName()
    {
	return serverName;
    }
    public void setServerName(String serverName)
    {
	this.serverName = serverName;
    }

    public String getError()
    {
	return error;
    }

    public void setError(String error)
    {
	this.error = error;
    }
}
