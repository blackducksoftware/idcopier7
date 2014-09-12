/**
 * 
 */
package com.blackducksoftware.proserv.idcopier.login;

import java.util.Date;

import com.blackducksoftware.proserv.idcopier.config.IDCProperties;
import com.blackducksoftware.protex.sdk.idcopier.domain.ICUser;
import com.blackducksoftware.sdk.fault.SdkFault;
import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxyV6_2;
import com.blackducksoftware.sdk.protex.user.UserApi;

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
