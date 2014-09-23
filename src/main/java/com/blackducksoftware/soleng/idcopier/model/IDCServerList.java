/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.soleng.idcopier.constants.IDCConfigurationConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Bean representing the <servers></servers> definition
 * 
 * @author Ari Kamen
 * @date Sep 22, 2014
 * 
 */

@XStreamAlias(IDCConfigurationConstants.SERVER_CONFIG_SERVER_LIST_NODE)
public class IDCServerList
{
    /** The server list. */
    @XStreamImplicit(itemFieldName = IDCConfigurationConstants.SERVER_CONFIG_SERVER_NODE)
    private List<IDCServer> serverList = new ArrayList<IDCServer>();

    /**
     * Instantiates a new server bean list.
     */
    public IDCServerList()
    {

    }

    /**
     * Adds the.
     * 
     * @param bean
     *            the bean
     */
    public void add(IDCServer bean)
    {
	serverList.add(bean);
    }

    /**
     * Gets the servers.
     * 
     * @return the servers
     */
    public List<IDCServer> getServers()
    {
	return serverList;
    }

    /**
     * Sets the servers.
     * 
     * @param serverList
     *            the new servers
     */
    public void setServers(List<IDCServer> serverList)
    {
	this.serverList = serverList;
    }
}
