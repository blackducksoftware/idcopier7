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

import com.blackducksoftware.tools.idcopier.constants.IDCConfigurationConstants;
import com.blackducksoftware.tools.idcopier.service.LoginService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

/**
 * @author Ari Kamen
 * @date Sep 19, 2014
 *
 */

@Configuration
// TODO: Make this fatal for now, will need to figure out a nicer way to handle
// this later.
@PropertySources({@PropertySource(value = "file:${IDCOPIER_CONFIG}", ignoreResourceNotFound = false)})
public class IDCConfig {
    static Logger log = Logger.getLogger(LoginService.class);

    @Autowired
    private Environment env;

    @Autowired
    ApplicationContext cxt;

    // Options on the main page
    private Boolean deferBomRefresh = false;
    private Boolean overwriteIDs = false;
    private Boolean recursive = false;
    private Boolean partialBom = false;
    private Boolean pullParentIds = false;

    // Options on the comment page
    private Boolean appendComments = false;

    private String serverList = null;

    public IDCConfig() {
        log.info("Configuration file has been loaded");
        init();
    }

    // Initialize default values
    private void init() {

    }

    public String getSessionTimeOut() {
        return getProperty(IDCConfigurationConstants.SESSION_TIMEOUT);
    }

    /**
     * Retrieves the list of server host addresses.
     *
     * @return
     */
    public String getServerListLocation() {
        return getProperty(IDCConfigurationConstants.SERVER_LIST_LOCATION);
    }

    public String getServerList() {
        return getProperty(IDCConfigurationConstants.SERVER_LIST);
    }

    private String getProperty(String key) {
        String someprop = env.getProperty(key);
        if (someprop == null)
            log.warn("The key you requested is not defined: " + key);

        return someprop;
    }

    private Boolean getBooleanProperty(String key) {
        Boolean returnedValue = false;
        String value = getProperty(key);
        if (value != null) {
            returnedValue = Boolean.valueOf(value);
            log.debug("Value for key: " + key + " is: " + returnedValue);
        }

        return returnedValue;
    }

    public Boolean isBomRefreshDefer() {
        deferBomRefresh = getBooleanProperty(IDCConfigurationConstants.OPTION_DEFER_BOM);
        return deferBomRefresh;
    }

    public void setBomRefreshDefer(Boolean bomRefresh) {
        this.deferBomRefresh = bomRefresh;
    }

    public Boolean isOverwriteIDs() {
        return overwriteIDs;
    }

    public void setOverwriteIDs(Boolean overwriteIDs) {
        overwriteIDs = getBooleanProperty(IDCConfigurationConstants.OPTION_OVERWRITE_IDS);
        this.overwriteIDs = overwriteIDs;
    }

    public Boolean isRecursive() {
        recursive = getBooleanProperty(IDCConfigurationConstants.OPTION_RECURSIVE);
        return recursive;
    }

    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    public Boolean istPartialBom() {
        partialBom = getBooleanProperty(IDCConfigurationConstants.OPTION_PARTIAL_BOM_REFRESH);
        return partialBom;
    }

    public void setPartialBom(Boolean partialBom) {
        this.partialBom = partialBom;
    }

    public Boolean isAppendComments() {
        return appendComments;
    }

    public void setAppendComments(Boolean appendComments) {
        appendComments = getBooleanProperty(IDCConfigurationConstants.OPTION_APPEND_COMMENTS);
        this.appendComments = appendComments;
    }

    public Boolean isPullParentIds() {
        pullParentIds = getBooleanProperty(IDCConfigurationConstants.OPTION_PULL_PARENT_IDS);
        return pullParentIds;
    }

}
