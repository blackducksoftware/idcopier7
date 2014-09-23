package com.blackducksoftware.soleng.idcopier.model;

import java.util.ArrayList;
import java.util.List;

public class IDCTree {
	private String key;
	private String title;
	private boolean isFolder;
	private List<IDCTree> children;

	public IDCTree(String key, String title, boolean isFolder) {
		super();
		this.key = key;
		this.title = cleanName(title);
		this.isFolder = isFolder;
	}

	private String cleanName(String original) {
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

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
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
}
