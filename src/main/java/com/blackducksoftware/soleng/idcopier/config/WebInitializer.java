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
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCConfig;
import com.blackducksoftware.soleng.idcopier.service.ProjectService;

public class WebInitializer implements WebApplicationInitializer {

	static Logger log = Logger.getLogger(WebInitializer.class);

	public void onStartup(ServletContext servletContext) throws ServletException {

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(IDCWebConfig.class);
		ctx.register(IDCConfig.class);
		// ctx.register(LoginService.class);
		ctx.register(ProjectService.class);
		ctx.setServletContext(servletContext);

		Dynamic servlet = servletContext.addServlet(IDCViewModelConstants.IDC_WEB_CONTEXT, new DispatcherServlet(ctx));
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);

		// Session stuff
		servletContext.addListener(new IDCSessionListener());
		IDCSessionFilter sessionFilter = new IDCSessionFilter();
		servletContext.addFilter("sessionFilter", sessionFilter).addMappingForUrlPatterns(null, false, "/*");

	}

}
