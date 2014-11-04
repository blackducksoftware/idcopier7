package com.blackducksoftware.soleng.idcopier.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
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

	@Autowired
	private UserServiceModel userServiceModel;

	@RequestMapping(value = IDCPathConstants.COPY_COMMENTS)
	public ModelAndView displayCopyCommentsPage() {
		return new ModelAndView(IDCViewConstants.COMMENTS_PAGE);
	}

	@RequestMapping(IDCPathConstants.BILL_OF_MATERIALS)
	public String copyIDs(@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_SERVER) String sourceServer,
			@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_NAME) String sourceProjectName,
			@RequestParam(value = IDCViewModelConstants.COPY_SOURCE_PROJECT_ID) String sourceProjectId) {

		String returnMsg = null;

		log.info("Attempting to retrieve the Bill of Materials for: " + sourceProjectName + " [" + sourceProjectId + "]");

		try {
			LoginService loginService = userServiceModel.getLoginService();

			ProtexServerProxy sourceProxy = loginService.getProxy(sourceServer);

			BomApi bomApi = sourceProxy.getBomApi();
			List<BomComponent> bom = bomApi.getBomComponents(sourceProjectId);
			List<IDCBomItem> bomItems = new ArrayList<IDCBomItem>();

			for (BomComponent current : bom) {
				IDCBomItem bomItem = new IDCBomItem(current);
				String comment = bomApi.getComponentComment(sourceProjectId, current.getComponentKey());
				bomItem.setComment(comment);
				bomItems.add(bomItem);
			}

			return new Gson().toJson(bomItems);
		} catch (Exception e) {
			log.error("Unable to get BOM for " + sourceProjectName);
		}

		return returnMsg;
	}
}