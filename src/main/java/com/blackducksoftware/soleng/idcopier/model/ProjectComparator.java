/**
 * Copyright (C)2014 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 */

/**
 *
 */
package com.blackducksoftware.soleng.idcopier.model;

import com.blackducksoftware.sdk.protex.project.ProjectInfo;

import java.util.Comparator;

/**
 * @author Ari Kamen
 * @date Dec 10, 2014
 *
 */
public class ProjectComparator implements Comparator<ProjectInfo> {
	@Override
	public int compare(ProjectInfo o1, ProjectInfo o2) {
		String firstName = o1.getName().toLowerCase();
		String secondName = o2.getName().toLowerCase();
		return firstName.compareTo(secondName);
	}
}
