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
package com.blackducksoftware.tools.idcopier.model;

import com.blackducksoftware.sdk.protex.project.bom.BomComponent;

@SuppressWarnings("unused")
public class IDCBomItem {
	// This is a concatenation of the comp and version Ids.
	// This is also the same ID on the UI and is essential for caching.
	private String uniqueID = "";
	private String componentName;
	private String componentId;
	private String versionName;
	private String versionId;
	private String comment;

	public IDCBomItem(BomComponent componentInfo) {
		this.componentName = componentInfo.getComponentName();
		this.componentId = componentInfo.getComponentKey().getComponentId();

		// With version
		if (componentInfo.getComponentKey().getVersionId() == null) {
			this.versionId = "Unspecified";
			this.versionName = "Unspecified";
		} else {
			versionId = componentInfo.getComponentKey().getVersionId();
			versionName = componentInfo.getVersionName();
		}

		uniqueID = componentId + "_" + versionId;
	}

	public void setComment(String comment) {
		if (comment == null) {
			this.comment = "";
		} else {
			this.comment = comment;
		}
	}

	public String getUniqueID() {
		return uniqueID;
	}
}