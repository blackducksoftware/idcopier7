/**
 * Copyright (C)2014 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 */

/**
 *
 */
package com.blackducksoftware.soleng.idcopier.model;

import java.io.Serializable;

/**
 * Session object containing relevant information
 *
 * @author Ari Kamen
 * @date Sep 15, 2014
 *
 */
public class IDCSession implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -797003971186382394L;

	private String userName;
	private String password;
	private String version;
	private String error;

	public IDCSession() {

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
