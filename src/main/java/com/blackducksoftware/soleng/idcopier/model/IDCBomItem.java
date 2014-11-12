package com.blackducksoftware.soleng.idcopier.model;

import com.blackducksoftware.sdk.protex.common.ComponentInfo;
import com.blackducksoftware.sdk.protex.project.bom.BomComponent;

@SuppressWarnings("unused")
public class IDCBomItem
{
    // This is a concatenation of the comp and version Ids.
    // This is also the same ID on the UI and is essential for caching.
    private String uniqueID = "";
    private String componentName;
    private String componentId;
    private String versionName;
    private String versionId;
    private String comment;

    public IDCBomItem(BomComponent componentInfo)
    {
	this.componentName = componentInfo.getComponentName();
	this.componentId = componentInfo.getComponentKey().getComponentId();

	// With version 
	if (componentInfo.getComponentKey().getVersionId() == null)
	{
	    this.versionId = "Unspecified";
	    this.versionName = "Unspecified";
	} else
	{
	    versionId = componentInfo.getComponentKey().getVersionId();
	    versionName = componentInfo.getVersionName();
	}

	uniqueID = componentId + "_" + versionId;
    }

    public void setComment(String comment)
    {
	if (comment == null)
	{
	    this.comment = "";
	} else
	{
	    this.comment = comment;
	}
    }

    public String getUniqueID()
    {
	return uniqueID;
    }
}