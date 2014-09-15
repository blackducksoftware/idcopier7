/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.service;

import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxyV6_2;
import com.blackducksoftware.soleng.idcopier.model.ProtexServer;

/**
 *  @author Ari Kamen
 *  @date Sep 15, 2014
 *
 */
public class LoginService
{
    static Logger log = Logger.getLogger(LoginService.class);

    private ProtexServer serverInfo = new ProtexServer();
    private ProtexServerProxyV6_2 protexProxy = null;
    
    
    public LoginService()
    {
	String uri = "http://se-menger.blackducksoftware.com/";
	String user = "akamen@blackducksoftware.com";
	String password = "blackduck";
	try{
	    protexProxy = new ProtexServerProxyV6_2
		    (uri, user, password);
	    
	    log.info("Login successful");
	    
	    serverInfo.setServerName(uri);
	    serverInfo.setUserName(user);
	    
	} catch (Exception e)
	{
	    log.error("Error establishing login service", e);
	}
    }
    
    /**
     * @return
     */
    public ProtexServer getServerInfo()
    {
	return serverInfo;
    }

    
    
}
