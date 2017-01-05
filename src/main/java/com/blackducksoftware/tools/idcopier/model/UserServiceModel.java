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
/**
 *
 */
package com.blackducksoftware.tools.idcopier.model;

import com.blackducksoftware.tools.idcopier.service.LoginService;
import com.blackducksoftware.tools.idcopier.service.ProjectService;

import java.io.Serializable;

/**
 * This is the model that houses user specific services (login, project, etc)
 *
 * Because it is scoped as a session object in our WebConfig, each user has his/her own dedicated services and caching.
 *
 * @author Ari Kamen
 * @date Oct 16, 2014
 *
 */
public class UserServiceModel implements Serializable {
	private LoginService loginService;
	private ProjectService projectService;

	public LoginService getNewLoginService() {
		loginService = new LoginService();
		projectService = new ProjectService();
		return loginService;
	}

	/**
	 * Returns the model for this view-model.
	 */
	public LoginService getLoginService() {
		if (loginService == null) {
			throw new RuntimeException("Login Service has not been initialized yet.");
		}
		return loginService;
	}

	public ProjectService getProjectService() {
		if (projectService == null)
			return new ProjectService();
		else
			return projectService;
	}
}
