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

import java.util.ArrayList;
import java.util.List;

public class IDCTree {
	private String key;
	private String title;
	private int count;
	private boolean folder;
	private boolean expanded;
	private boolean lazy;
	private boolean hasPending = false;
	private List<IDCTree> children;

	public IDCTree(String key, String title, boolean isFolder) {
		super();
		this.key = key;
		this.title = cleanName(title);
		this.folder = isFolder;
		this.lazy = isFolder;
	}

	public IDCTree(String key, String title, boolean isFolder, int count) {
		super();
		this.key = key;
		this.title = cleanName(title, count);
		this.folder = isFolder;
		this.lazy = isFolder;
		this.count = count;
	}

	private String cleanName(String original) {
		if (original.contains("/")) {
			String[] values = original.split("/");

			return values[values.length - 1];
		}

		return original;
	}

	private String cleanName(String original, int count) {
		if (count > 0) {
			original = original + " (" + count + ")";
			hasPending = true;
		}

		if (original.contains("/")) {
			String[] values = original.split("/");
			return values[values.length - 1];
		}

		return original;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean isFolder) {
		this.folder = isFolder;
	}

	public boolean isExpand() {
		return expanded;
	}

	public void setExpand(boolean expand) {
		this.expanded = expand;
	}

	public List<IDCTree> getChildren() {
		return children;
	}

	public void setChildren(List<IDCTree> children) {
		this.children = children;
	}

	public void addChildren(List<IDCTree> children) {
		this.children = children;
	}

	public void addChild(IDCTree child) {
		if (this.children == null) {
			children = new ArrayList<IDCTree>();
		}

		this.children.add(child);
	}

	public boolean isHasPending() {
		return hasPending;
	}

	public void setHasPending(boolean hasPending) {
		this.hasPending = hasPending;
	}
}
