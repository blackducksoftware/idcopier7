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
package com.blackducksoftware.soleng.idcopier.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;


public class IDCSessionListener implements HttpSessionListener {
	static Logger log = Logger.getLogger(IDCSessionListener.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		int timeout = -1;

		/**
		 * OH boy, this is a weird one. Unlike the controllers and other Spring classes, there is no way to autowire our IDCConfig class because Spring is unaware of it from a context standpoint.
		 * 
		 * So we are forced to get the Bean explicitly from the web application context.
		 * 
		 * Unfortunately, for whatever reason the convenient methods (possibly due to Java Config setup) give us null web contexts, so we have to look it up via attribute.
		 * 
		 * TODO: If there is a way around this mess, find it.
		 */
		ServletContext sc = event.getSession().getServletContext();
		// Lookup our specific servlet, using constants to make it a bit less
		// hard-coded
		WebApplicationContext ctx = (WebApplicationContext) sc.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT." + IDCViewModelConstants.IDC_WEB_CONTEXT);

		IDCConfig configBean = ctx.getBean(IDCConfig.class);
		String timeOutStr;
		try {
			timeOutStr = configBean.getSessionTimeOut();
			if (timeOutStr != null) {
				timeout = Integer.valueOf(timeOutStr);
			}

			event.getSession().setMaxInactiveInterval(timeout);

			log.info("Setting custom timeout: " + event.getSession().getMaxInactiveInterval());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
	}
}