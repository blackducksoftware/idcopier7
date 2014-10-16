package com.blackducksoftware.soleng.idcopier.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.controller.IDCLoginController;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;
import com.blackducksoftware.soleng.idcopier.service.LoginService;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

public class WebInitializer implements WebApplicationInitializer
{

    static Logger log = Logger.getLogger(WebInitializer.class);

    public void onStartup(ServletContext servletContext)
	    throws ServletException
    {

	AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
	ctx.register(IDCWebConfig.class);
	ctx.register(IDCConfig.class);
	//ctx.register(LoginService.class);
	ctx.register(ProjectService.class);
	ctx.setServletContext(servletContext);

	Dynamic servlet = servletContext.addServlet(IDCViewModelConstants.IDC_WEB_CONTEXT,
		new DispatcherServlet(ctx));
	servlet.addMapping("/");
	servlet.setLoadOnStartup(1);
	
	// Session stuff
	servletContext.addListener(new IDCSessionListener());
	IDCSessionFilter sessionFilter = new IDCSessionFilter();
	servletContext.addFilter("sessionFilter", sessionFilter).addMappingForUrlPatterns(null, false, "/*");
	


    }

}
