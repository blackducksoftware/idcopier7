/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import java.io.Serializable;
import java.util.List;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;

/**
 * Session object containing relevant information
 * 
 * @author Ari Kamen
 * @date Sep 15, 2014
 * 
 */
public class IDCSession implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -797003971186382394L;
    private String serverURI;
    private String userName;
    private String password;
    private String error;
    private IDCServer sourceServer;
    private IDCServer targetServer;

    public IDCSession()
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

    public String getServerURI()
    {
	return serverURI;
    }

    public void setServerURI(String serverURI)
    {
	this.serverURI = serverURI;
    }

    public String getError()
    {
	return error;
    }

    public void setError(String error)
    {
	this.error = error;
    }

    public IDCServer getSourceServer()
    {
	return sourceServer;
    }

    public void setSourceServer(IDCServer sourceServer)
    {
	this.sourceServer = sourceServer;
    }

    public IDCServer getTargetServer()
    {
	return targetServer;
    }

    public void setTargetServer(IDCServer targetServer)
    {
	this.targetServer = targetServer;
    }
}
