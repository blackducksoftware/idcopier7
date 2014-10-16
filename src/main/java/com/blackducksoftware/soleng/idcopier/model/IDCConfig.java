/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.blackducksoftware.soleng.idcopier.constants.IDCConfigurationConstants;
import com.blackducksoftware.soleng.idcopier.service.LoginService;

/**
 * @author Ari Kamen
 * @date Sep 19, 2014
 * 
 */

@Configuration
// TODO: Make this fatal for now, will need to figure out a nicer way to handle
// this later.
@PropertySources(
{ @PropertySource(value = "file:${IDCOPIER_CONFIG}", ignoreResourceNotFound = false) })
public class IDCConfig
{
    static Logger log = Logger.getLogger(LoginService.class);

    @Autowired
    private Environment env;

    @Autowired
    ApplicationContext cxt;

    private Boolean bomRefresh = false;
    private Boolean overwriteIDs = false;
    private Boolean recursive = false;
    private Boolean partialBom = false;
    
    private String serverList = null;
    
    public IDCConfig()
    {
	log.info("Configuration file has been loaded");
    }

    public String getSessionTimeOut()
    {
	return getProperty(IDCConfigurationConstants.SESSION_TIMEOUT);
    }

    /**
     * Retrieves the list of server host addresses.
     * @return
     */
    public String getServerListLocation()
    {
	return getProperty(IDCConfigurationConstants.SERVER_LIST_LOCATION);
    }

    public String getServerList()
    {
	return getProperty(IDCConfigurationConstants.SERVER_LIST);
    }
    
    private String getProperty(String key)
    {
	String someprop = env.getProperty(key);
	if (someprop == null)
	    log.warn("The key you requested is not defined: " + key);

	return someprop;
    }


    public Boolean isBomRefreshDefer()
    {
	return bomRefresh;
    }

    public void setBomRefreshDefer(Boolean bomRefresh)
    {
	this.bomRefresh = bomRefresh;
    }

    public Boolean isOverwriteIDs()
    {
	return overwriteIDs;
    }

    public void setOverwriteIDs(Boolean overwriteIDs)
    {
	this.overwriteIDs = overwriteIDs;
    }

    public Boolean isRecursive()
    {
	return recursive;
    }

    public void setRecursive(Boolean recursive)
    {
	this.recursive = recursive;
    }

    public Boolean istPartialBom()
    {
	return partialBom;
    }

    public void setPartialBom(Boolean partialBom)
    {
	this.partialBom = partialBom;
    }



}
