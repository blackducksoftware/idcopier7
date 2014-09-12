package com.blackducksoftware.protex.sdk.idcopier.domain;

public class voFolderItem  {

	private String name;
	private String fullpath;
	private String type;
	boolean display;
	int level;
	int matchcount;
	long pendingCount;
	long pendingCMCount;
	long pendingSSCount;
	long pendingDepCount;
	long idCMCount;
	long idSSCount;
	long idDepCount;

	public long getPendingCMCount() {
		return pendingCMCount;
	}

	public void setPendingCMCount(long pendingCMCount) {
		this.pendingCMCount = pendingCMCount;
	}

	public long getPendingSSCount() {
		return pendingSSCount;
	}

	public void setPendingSSCount(long pendingSSCount) {
		this.pendingSSCount = pendingSSCount;
	}

	public long getPendingDepCount() {
		return pendingDepCount;
	}

	public void setPendingDepCount(long pendingDepCount) {
		this.pendingDepCount = pendingDepCount;
	}

	public long getIdSSCount() {
		return idSSCount;
	}

	public void setIdSSCount(long idSSCount) {
		this.idSSCount = idSSCount;
	}

	public long getIdDepCount() {
		return idDepCount;
	}

	public void setIdDepCount(long idDepCount) {
		this.idDepCount = idDepCount;
	}

	public long getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(long pendingCount) {
		this.pendingCount = pendingCount;
	}

	public long getIdCMCount() {
		return idCMCount;
	}

	public void setIdCMCount(long idCount) {
		this.idCMCount = idCount;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public int getMatchcount() {
		return matchcount;
	}

	public void setMatchcount(int matchcount) {
		this.matchcount = matchcount;
	}

	public String getFullpath() {
		return fullpath;
	}

	public void setFullpath(String fullpath) {
		this.fullpath = fullpath;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
