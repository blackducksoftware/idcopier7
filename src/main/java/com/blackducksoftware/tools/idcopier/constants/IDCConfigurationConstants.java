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
package com.blackducksoftware.tools.idcopier.constants;

public class IDCConfigurationConstants {
	// Property file keys
	public static final String SERVER_LIST_LOCATION = "idcopier.servers.location";
	public static final String SERVER_LIST = "idcopier.servers.location";
	public static final String SESSION_TIMEOUT = "idcopier.web.timeout";

	// Options
	public static final String OPTION_DEFER_BOM = "idcopier.defer.bom.refresh";
	public static final String OPTION_RECURSIVE = "idcopier.recursive";
	public static final String OPTION_OVERWRITE_IDS = "idcopier.overwrite.ids";
	public static final String OPTION_PARTIAL_BOM_REFRESH = "idcopier.partial.bom.refresh";
	public static final String OPTION_PULL_PARENT_IDS = "tools.pull.parent.ids";
	// Options - Comments
	public static final String OPTION_APPEND_COMMENTS = "tools.append.comments";

	// Server Configuration file
	public static final String SERVER_CONFIG_SERVER_LIST_NODE = "servers";
	public static final String SERVER_CONFIG_SERVER_NODE = "server";
	public static final String SERVER_CONFIG_SERVER_URI = "uri";
	public static final String SERVER_CONFIG_SERVER_ALIAS = "alias";
	public static final String SERVER_CONFIG_SERVER_USERNAME = "user.name";
	public static final String SERVER_CONFIG_SERVER_PASSWORD = "password";
}
