package com.blackducksoftware.soleng.idcopier.model;

import com.blackducksoftware.sdk.protex.project.bom.BomComponent;

@SuppressWarnings("unused")
public class IDCBomItem {
	private String componentName;
	private String componentId;
	private String versionName;
	private String versionId;
	private String comment;

	public IDCBomItem(BomComponent bomComponent) {
		this.componentName = bomComponent.getComponentName();
		this.componentId = bomComponent.getComponentKey().getComponentId();

		// Set the version Name such that if it is null, it will now read 'Unspecified'
		if (bomComponent.getVersionName() == null) {
			this.versionName = "Unspecified";
		} else {
			this.versionName = bomComponent.getVersionName();
		}

		// Set the Version ID to be able to handle unspecified versions
		if (bomComponent.getComponentKey().getVersionId() == null) {
			this.versionId = "";
		} else {
			versionId = bomComponent.getComponentKey().getVersionId();
		}
	}

	public void setComment(String comment) {
		if (comment == null) {
			this.comment = "";
		} else {
			this.comment = comment;
		}
	}
}