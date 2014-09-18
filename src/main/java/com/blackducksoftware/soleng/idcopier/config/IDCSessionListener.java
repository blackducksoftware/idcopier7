/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 *  @author Ari Kamen
 *  @date Sep 18, 2014
 *
 */

public class IDCSessionListener implements HttpSessionListener
{
    static Logger log = Logger.getLogger(IDCSessionListener.class);

    
    @Override
    public void sessionCreated(HttpSessionEvent event)
    {
	// TODO:  Make this configurable through our property files
	// TODO: For now default to 5
	int timeout = 60 * 5;

	event.getSession().setMaxInactiveInterval(timeout);
	log.info("Max active timeout: " + event.getSession().getMaxInactiveInterval());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event)
    {
    }
}