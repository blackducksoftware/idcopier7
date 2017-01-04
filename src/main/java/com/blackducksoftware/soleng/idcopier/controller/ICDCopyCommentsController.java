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
package com.blackducksoftware.soleng.idcopier.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.fault.SdkFault;
import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.common.ComponentInfo;
import com.blackducksoftware.sdk.protex.common.ComponentKey;
import com.blackducksoftware.sdk.protex.project.bom.BomApi;
import com.blackducksoftware.sdk.protex.project.bom.BomComponent;
import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCBomItem;
import com.blackducksoftware.soleng.idcopier.model.UserServiceModel;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.google.gson.Gson;

/**
 * @author nmadison
 */

@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class ICDCopyCommentsController {
	static Logger log = Logger.getLogger(ICDAdminController.class);
	private Map<String, BomComponent> sourceBomComments = null;

	@Autowired
	private UserServiceModel userServiceModel;

	@RequestMapping(value = IDCPathConstants.DISPLAY_COMMENTS)
	public ModelAndView displayCopyCommentsPage() {
		return new ModelAndView(IDCViewConstants.COMMENTS_PAGE);
	}

	@RequestMapping(IDCPathConstants.DISPLAY_BILL_OF_MATERIALS)
	public String displayBOMComponents(@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_SERVER) String sourceServer, @RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_NAME) String sourceProjectName,
			@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_ID) String sourceProjectId) {

		String returnMsg = null;

		log.info("Attempting to retrieve the Bill of Materials for: " + sourceProjectName + " [" + sourceProjectId + "]");

		try {
			LoginService loginService = userServiceModel.getLoginService();
			ProtexServerProxy sourceProxy = loginService.getProxy(sourceServer);
			BomApi bomApi = sourceProxy.getBomApi();

			List<BomComponent> bom = bomApi.getBomComponents(sourceProjectId);

			List<IDCBomItem> bomItems = new ArrayList<IDCBomItem>();

			// Want to reset this map every time.
			sourceBomComments = new HashMap<String, BomComponent>();

			for (BomComponent bomComponent : bom) {
				IDCBomItem idcBomItem = new IDCBomItem(bomComponent);

				String comment = bomApi.getComponentComment(sourceProjectId, bomComponent.getComponentKey());

				idcBomItem.setComment(comment);
				bomItems.add(idcBomItem);

				sourceBomComments.put(idcBomItem.getUniqueID(), bomComponent);
			}
			log.info("Retrieved components, count: " + bom.size());
			return new Gson().toJson(bomItems);
		} catch (Exception e) {
			log.error("Unable to get BOM for " + sourceProjectName);
		}

		return returnMsg;
	}

	@RequestMapping(IDCPathConstants.COPY_COMMENTS)
	public String copyComments(@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_SERVER) String sourceServer, @RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_ID) String sourceProjectId,

	@RequestParam(value = IDCViewModelConstants.COPY_TARGET_SERVER) String targetServer, @RequestParam(value = IDCViewModelConstants.COPY_TARGET_PROJECT_ID) String targetProjectId,
			@RequestParam(value = IDCViewModelConstants.COPY_COMMENT_IDS) String[] commentIds, @RequestParam(value = IDCViewModelConstants.COPY_EXPRESS) Boolean expressCopy,
			@RequestParam(value = IDCViewModelConstants.APPEND_COMMENTS_OPTION) Boolean appendComments)

	{
		String returnMsg = "";

		log.info("Copying comments, express copy set to: " + expressCopy);

		if (sourceProjectId.equals(targetProjectId)) {
			String msg = "Project source and target IDs cannot be the same";
			log.warn(msg);
			return msg;
		}

		try {
			LoginService loginService = userServiceModel.getLoginService();
			ProtexServerProxy sourceProxy = loginService.getProxy(sourceServer);
			ProtexServerProxy targetProxy = loginService.getProxy(targetServer);
			BomApi sourceBomApi = sourceProxy.getBomApi();
			BomApi targetBomApi = targetProxy.getBomApi();

			StringBuilder successMsg = new StringBuilder();

			List<BomComponent> sourceBomList = new ArrayList<BomComponent>();
			if (expressCopy) {
				sourceBomList = sourceBomApi.getBomComponents(sourceProjectId);
			} else {
				// These are unique IDs, that were created during the BomItem
				// instantiation.
				for (String id : commentIds) {
					if (id.length() > 0) {
						BomComponent comp = sourceBomComments.get(id);
						if (comp == null)
							log.warn("Internal cache does not contain bom component for id: " + id);
						sourceBomList.add(comp);
					}
				}
			}

			for (BomComponent component : sourceBomList) {
				ComponentKey key = component.getComponentKey();
				try {
					// Get the comment from the existing source
					String comment = sourceBomApi.getComponentComment(sourceProjectId, key);

					if (appendComments)
						comment = appendCommentToTarget(comment, key, targetProjectId, targetBomApi);

					// Apply it to the target
					if (comment != null && comment.length() > 0) {

						targetBomApi.setComponentComment(targetProjectId, key, comment);

						successMsg.append("Copied comment with id: " + key.getComponentId());
						successMsg.append("\n");
					}

				} catch (SdkFault e) {
					successMsg.append("Could not find component key on target server with id: " + key.getComponentId());
					successMsg.append("\n");
				}
			}

			returnMsg = successMsg.toString();
			log.info(returnMsg);

		} catch (Exception e) {
			log.error(e.getMessage());
			returnMsg = "Unable to copy comments, cause: " + e.getMessage();
		}

		return returnMsg;
	}

	/**
	 * Appends the source comment to the target comment.
	 * 
	 * @param comment
	 * @param key
	 * @param targetProjectId
	 * @param targetBomApi
	 * @return
	 */
	private String appendCommentToTarget(String comment, ComponentKey key, String targetProjectId, BomApi targetBomApi) {
		StringBuilder sb = new StringBuilder();
		try {
			String targetComment = targetBomApi.getComponentComment(targetProjectId, key);
			sb.append(targetComment);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate = sdf.format(new Date());

			if (comment != null && comment.length() > 0) {
				sb.append("\n");
				sb.append("---Appended Comment from IDCopier on: " + currentDate + "---");
				sb.append("\n");
				// Now add the source comment to the end
				sb.append(comment);
			}

		} catch (SdkFault e) {
			log.warn("Attempted to append comments, but failed to get target: " + e.getMessage());
		}

		return sb.toString();
	}
}