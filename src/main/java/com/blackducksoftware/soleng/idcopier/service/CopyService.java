/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.common.BomRefreshMode;
import com.blackducksoftware.sdk.protex.project.codetree.identification.IdentificationApi;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;

/**
 * Class responsible for copying IDs
 * 
 * @author Ari Kamen
 * @date Sep 25, 2014
 * 
 */
public class CopyService
{
    private IDCConfig config;
    
    static Logger log = Logger.getLogger(CopyService.class);

    public CopyService(IDCConfig config)
    {
	this.config = config;
    }

    public void performCopy(ProtexServerProxy sourceProxy,
	    String sourceProjectId, String targetProjectId, String sourcePath,
	    String targetPath) throws Exception
    {
	try
	{
	    IdentificationApi iApi = sourceProxy.getIdentificationApi();

	    /**
	     * Always skip the refresh
	     * This is because the Controller will perform the refresh call if necessary
	     */
	    iApi.copyIdentifications(sourceProjectId, sourcePath, targetProjectId,
		    targetPath, config.isRecursive(), config.isOverwriteIDs(), BomRefreshMode.SKIP);
	    
	    log.info("Finished copying");
	    
	} catch (Exception e)
	{
	    log.error("Error copying: " +  e.getMessage());
	    throw new Exception("Error copying", e);
	}
    }
}
