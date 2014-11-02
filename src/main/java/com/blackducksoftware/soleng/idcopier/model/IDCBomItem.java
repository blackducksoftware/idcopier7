package com.blackducksoftware.soleng.idcopier.model;

import com.blackducksoftware.sdk.protex.project.bom.BomComponent;

@SuppressWarnings("unused")
public class IDCBomItem {
	private String componentId;
	private String componentName;
	private String versionId;
	private String versionName;
	private String comment;

	public IDCBomItem(BomComponent bomComponent) {
		this.componentId = bomComponent.getComponentKey().getComponentId();
		this.componentName = bomComponent.getComponentName();
		this.versionId = bomComponent.getComponentKey().getVersionId();
		this.versionName = bomComponent.getVersionName();
	}

	public void setComment(String comment) {
		if (comment == null) {
			this.comment = "";
		} else {
			this.comment = comment;
		}
	}
}