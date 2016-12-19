/**
 * Copyright (C)2014 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 */

/**
 *
 */
package com.blackducksoftware.soleng.idcopier.model;

import java.util.Comparator;

/**
 * To sort the nodes in the tree.
 *
 * @author Ari Kamen
 * @date Dec 22, 2014
 *
 */
public class NodeComparator implements Comparator<String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String o1, String o2) {
		String firstNode = o1.toLowerCase();
		String secondNode = o2.toLowerCase();

		// ascending order
		return firstNode.compareTo(secondNode);
	}

}
