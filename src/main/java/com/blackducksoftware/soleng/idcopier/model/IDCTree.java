package com.blackducksoftware.soleng.idcopier.model;

import java.util.ArrayList;
import java.util.List;

public class IDCTree {
	private String id;
	private String text;
	private String liClass;
	private boolean isFolder;
	private boolean isExpanded;
	private List<IDCTree> children;

	public IDCTree(String id, String text, boolean isFolder) {
		super();
		this.id = id;
		this.text = text;
		this.isFolder = isFolder;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLiClass() {
		return liClass;
	}

	public void setLiClass(String liClass) {
		this.liClass = liClass;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
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
