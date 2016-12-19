package com.blackducksoftware.soleng.idcopier.model;

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
