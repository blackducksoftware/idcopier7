/**
 * IDCopier
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 Copyright (C)2014 Black Duck Software Inc.
 http://www.blackducksoftware.com/
 All rights reserved. **/

/**
 *
 */
package com.blackducksoftware.tools.idcopier.model;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.tools.idcopier.constants.IDCConfigurationConstants;
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
public class IDCServerList {
    /** The server list. */
    @XStreamImplicit(itemFieldName = IDCConfigurationConstants.SERVER_CONFIG_SERVER_NODE)
    private List<IDCServer> serverList = new ArrayList<IDCServer>();

    /**
     * Instantiates a new server bean list.
     */
    public IDCServerList() {

    }

    /**
     * Adds the.
     *
     * @param bean
     *            the bean
     */
    public void add(IDCServer bean) {
        serverList.add(bean);
    }

    /**
     * Gets the servers.
     *
     * @return the servers
     */
    public List<IDCServer> getServers() {
        return serverList;
    }

    /**
     * Sets the servers.
     *
     * @param serverList
     *            the new servers
     */
    public void setServers(List<IDCServer> serverList) {
        this.serverList = serverList;
    }
}
