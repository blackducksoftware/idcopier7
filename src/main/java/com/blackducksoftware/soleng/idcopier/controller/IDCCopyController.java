/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;
import com.blackducksoftware.soleng.idcopier.model.UserServiceModel;
import com.blackducksoftware.soleng.idcopier.service.CopyService;
import com.blackducksoftware.soleng.idcopier.service.LoginService;

/**
 * This controller handles all copy requests whether through buttons or drap and drop functionality.
 * 
 * @author Ari Kamen
 * @date Sep 25, 2014
 * 
 */
@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class IDCCopyController {
	static Logger log = Logger.getLogger(IDCCopyController.class);

	@Autowired
	private IDCConfig config;

	@Autowired
	private UserServiceModel userServiceModel;

	@RequestMapping(IDCPathConstants.COPY_IDS)
	public String copyIDs(@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_SERVER) String sourceServer, @RequestParam(value = IDCViewModelConstants.COPY_TARGET_SERVER) String targetServer,
			@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_ID) String sourceProjectId, @RequestParam(value = IDCViewModelConstants.COPY_TARGET_PROJECT_ID) String targetProjectId,
			@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PATH) String sourcePath,
			@RequestParam(value = IDCViewModelConstants.COPY_TARGET_PATHS) String targetPaths,
			// Optional
			@RequestParam(value = IDCViewModelConstants.COPY_OVERWRITE_OPTION) Boolean overWriteOption, @RequestParam(value = IDCViewModelConstants.COPY_DEFER_BOM_REFRESH_OPTION) Boolean deferBomRefreshOption,
			@RequestParam(value = IDCViewModelConstants.COPY_RECURSIVE_OPTION) Boolean recursiveOption, @RequestParam(value = IDCViewModelConstants.COPY_RECURSIVE_OPTION) Boolean partialBomOption,
			@RequestParam(value = IDCViewModelConstants.PULL_PARENT_IDS_OPTION) Boolean pullParentIdsOption) throws Exception {

		StringBuilder returnMsg = new StringBuilder();

		StringBuilder sb = new StringBuilder();
		sb.append("Preparing ID Copy with the following parameters: ");
		sb.append("\n Source server: " + sourceServer);
		sb.append("\n Target server: " + targetServer);
		sb.append("\n Source project: " + sourceProjectId);
		sb.append("\n Target project: " + targetProjectId);
		sb.append("\n Source path: " + sourcePath);
		sb.append("\n Target path(s): " + targetPaths);
		sb.append("\n OPTIONS: ");
		sb.append("\n Over-write : " + overWriteOption);
		sb.append("\n Defer BOM Refresh: " + deferBomRefreshOption);
		sb.append("\n Recursive: " + recursiveOption);
		sb.append("\n Partial BOM Refresh: " + partialBomOption);
		sb.append("\n Pull Parent IDs: " + pullParentIdsOption);

		if (!sourceServer.equalsIgnoreCase(targetServer)) {
			returnMsg.append("Servers mismatch, functionality not supported!");
			log.error(returnMsg);
			throw new Exception(returnMsg.toString());
		} else {
			log.info("Attempting to copy: " + sb.toString());

			try {
				// Set the options
				config.setBomRefreshDefer(deferBomRefreshOption);
				config.setOverwriteIDs(overWriteOption);
				config.setRecursive(recursiveOption);
				config.setPartialBom(partialBomOption);

				LoginService loginService = userServiceModel.getLoginService();

				ProtexServerProxy sourceProxy = loginService.getProxy(sourceServer);

				CopyService copyService = new CopyService(config);

				String[] targetPathArray = targetPaths.split(",");

				if (targetPathArray.length > 1) {
					log.warn("Multiple target paths selected, defering BOM refresh");
					config.setBomRefreshDefer(true);
				}

				for (String targetPath : targetPathArray) {
					targetPath = targetPath.trim();
					log.info("Preparing to copy to target path: " + targetPath);

					/**
					 * Pulling parent IDs requires us to provide all sub-parents of the source path.
					 */

					List<String> sourcePaths = getAllPaths(sourcePath, pullParentIdsOption);
					int dirCount = 0;
					for (String pathSegment : sourcePaths) {
						// In the case where pull parent is selected
						// we do not want to overwrite the parents, just the first path
						boolean overWrite = config.isOverwriteIDs();
						if (pullParentIdsOption && dirCount != 0)
							overWrite = false;

						copyService.performCopy(sourceProxy, sourceProjectId, targetProjectId, pathSegment, targetPath, overWrite);
						dirCount++;
					}

					log.info("Finished copying from source path: " + sourcePath + " to  target path: " + targetPath);
					returnMsg.append("[" + sourcePath + "] Finished Copying IDs, directories examined: " + dirCount);
					returnMsg.append("\n");
				}
			} catch (NullPointerException npe) {
				returnMsg.append("Error during copying...check logs for errors.");
			} catch (Exception e) {
				returnMsg.append(e.getMessage());
			}

		}

		return returnMsg.toString();

	}

	/**
	 * @param sourcePath
	 * @return
	 */
	private List<String> getAllPaths(String sourcePath, boolean pullParentIdsOption) {
		List<String> allPaths = new ArrayList<String>();

		if (pullParentIdsOption) {
			allPaths = getAllParentPaths(allPaths, sourcePath);
		} else {
			allPaths.add(sourcePath);
		}
		return allPaths;
	}

	/**
	 * Recursively collects all the parents
	 * 
	 * @param allPaths
	 * @param sourcePath
	 * @return
	 */
	private List<String> getAllParentPaths(List<String> allPaths, String sourcePath) {
		sourcePath = sourcePath.replace("\\", "/"); // For Protex, only forward slash is acceptable
		allPaths.add(sourcePath);
		File fullPath = new File(sourcePath);
		String parentPath = fullPath.getParent();
		if (parentPath == null) {
			return allPaths;
		} else {
			return getAllParentPaths(allPaths, parentPath);
		}
	}
}
