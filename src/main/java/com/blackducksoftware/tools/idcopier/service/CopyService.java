/**
 * IDCopier
 * <p>
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 *
 */
package com.blackducksoftware.tools.idcopier.service;

import org.apache.log4j.Logger;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.common.BomRefreshMode;
import com.blackducksoftware.sdk.protex.project.codetree.identification.IdentificationApi;
import com.blackducksoftware.tools.idcopier.model.IDCConfig;

/**
 * Class responsible for copying IDs
 *
 * @author Ari Kamen
 * @date Sep 25, 2014
 *
 */
public class CopyService {
	private IDCConfig config;

	static Logger log = Logger.getLogger(CopyService.class);

	public CopyService(IDCConfig config) {
		this.config = config;
	}

	public void performCopy(ProtexServerProxy sourceProxy, String sourceProjectId, String targetProjectId,
			String sourcePath, String targetPath, Boolean overWrite) throws Exception {
		try {
			IdentificationApi iApi = sourceProxy.getIdentificationApi();

			// Always skip the refresh This is because the Controller will
			// perform the refresh call if necessary

			iApi.copyIdentifications(sourceProjectId, sourcePath, targetProjectId, targetPath, config.isRecursive(), overWrite, BomRefreshMode.SKIP);

			log.info("Finished copying from source path: " + sourcePath);

		} catch (Exception e) {
			log.error("Error copying: " + e.getMessage());
			throw new Exception("Error copying", e);
		}
	}

	/*
	public void performEffectiveCopy(ProtexServerProxy sourceProxy, String sourceProjectId, String targetProjectId,
			String sourcePath, String targetPath, Boolean overWrite) throws Exception {
		try {
			IdentificationApi iApi = sourceProxy.getIdentificationApi();

			// Always skip the refresh This is because the Controller will
			// perform the refresh call if necessary

			iApi.copyEffectiveIdentifications(sourceProjectId, sourcePath, targetProjectId, targetPath,
					config.isRecursive(), overWrite, BomRefreshMode.SKIP);

			log.info("Finished copying from source path: " + sourcePath);

		} catch (Exception e) {
			log.error("Error copying: " + e.getMessage());
			throw new Exception("Error copying", e);
		}
	}
	*/
}
