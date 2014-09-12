package com.blackducksoftware.protex.sdk.idcopier.domain;

import java.util.Date;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxyV6_2;

public class ICUser {
	private String loginName;
	private String email;
	private String password;

	private Date loginTime;
	private Date lastActivity;
	private String httpSession;
	private String protexSession;
	private String server;
    private ProtexServerProxyV6_2 myProtexServer = null;
	

    private boolean isUserAuthorized = false;
    private String loginErrorMessage = null;
    
	public String getEmail() {
	  return email;
	  }

	public void setEmail(String email) {
	  this.email = email;
	  }

	public ProtexServerProxyV6_2 getMyProtexServer() {
		return myProtexServer;
	}

	public void setMyProtexServer(ProtexServerProxyV6_2 myProtexServer) {
		this.myProtexServer = myProtexServer;
	}


	public String getProtexSession() {
		return protexSession;
	}

	public void setProtexSession(String protexSession) {
		this.protexSession = protexSession;
	}

	public String getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(String httpSession) {
		this.httpSession = httpSession;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String name) {
		this.loginName = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getLoginTime() {
		return loginTime;
	}
	
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	public Date getLastActivity() {
		return lastActivity;
	}
	
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}


	public boolean isUserAuthorized() 
	{
		if(loginErrorMessage != null)
			isUserAuthorized = false;
		
		return isUserAuthorized;
	}

	public void setUserAuthorized(boolean isUserAuthorized) 
	{
		
		this.isUserAuthorized = isUserAuthorized;
	}

	public String getLoginErrorMessage() {
		return loginErrorMessage;
	}

	public void setLoginErrorMessage(String loginErrorMessage) {
		this.loginErrorMessage = loginErrorMessage;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}
