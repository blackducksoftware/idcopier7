/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;

/**
 * @author Ari Kamen
 * @date Sep 18, 2014
 * 
 */
public class IDCSessionListener implements HttpSessionListener
{
    static Logger log = Logger.getLogger(IDCSessionListener.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void sessionCreated(HttpSessionEvent event)
    {
	int timeout = -1;

	/**
	 * OH boy, this is a weird one. Unlike the controllers and other Spring
	 * classes, there is no way to autowire our IDCConfig class because
	 * Spring is unaware of it from a context standpoint.
	 * 
	 * So we are forced to get the Bean explicitly from the web application
	 * context.
	 * 
	 * Unfortunately, for whatever reason the convenient methods (possibly
	 * due to Java Config setup) give us null web contexts, so we have to
	 * look it up via attribute.
	 * 
	 * TODO: If there is a way around this mess, find it.
	 */
	ServletContext sc = event.getSession().getServletContext();
	// Lookup our specific servlet, using constants to make it a bit less
	// hard-coded
	WebApplicationContext ctx = (WebApplicationContext) sc
		.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT."
			+ IDCViewModelConstants.IDC_WEB_CONTEXT);

	IDCConfig configBean = ctx.getBean(IDCConfig.class);
	String timeOutStr = configBean.getSessionTimeOut();
	if (timeOutStr != null)
	{
	    timeout = Integer.valueOf(timeOutStr);
	    log.info("Setting custom timeout : " + timeout);
	}

	event.getSession().setMaxInactiveInterval(timeout);
	log.info("Max active timeout: "
		+ event.getSession().getMaxInactiveInterval());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event)
    {
    }
}