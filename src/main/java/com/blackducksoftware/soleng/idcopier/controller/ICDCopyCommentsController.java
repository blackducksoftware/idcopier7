package com.blackducksoftware.soleng.idcopier.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;

/**
 * @author nmadison
 */

@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class ICDCopyCommentsController {
	static Logger log = Logger.getLogger(ICDAdminController.class);

	@RequestMapping(value = IDCPathConstants.COPY_COMMENTS)
	public ModelAndView displayCopyCommentsPage() {
		return new ModelAndView(IDCViewConstants.COMMENTS_PAGE);
	}
}