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
		this.title = title;
		this.isFolder = isFolder;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return title;
	}

	public void setText(String title) {
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
