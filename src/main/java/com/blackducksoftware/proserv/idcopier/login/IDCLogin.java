/**
 * 
 */
package com.blackducksoftware.proserv.idcopier.login;

import com.blackducksoftware.proserv.idcopier.config.IDCProperties;

/**
 * @author Ari Kamen
 * Set users objects and authenticates server.
 */
public class IDCLogin 
{

	private boolean loginAuthorized = false;
	
	public IDCLogin(IDCProperties idcProperties) 
	{
		
	}
	
	public boolean isAuthorized()
	{
		return loginAuthorized;
	}
	
	
}
