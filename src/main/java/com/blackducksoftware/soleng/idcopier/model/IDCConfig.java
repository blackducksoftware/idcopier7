/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *  @author Ari Kamen
 *  @date Sep 19, 2014
 *
 */

@Configuration
//Specifies which package to scan
@PropertySource("classpath:config.properties")
public class IDCConfig
{
    @Autowired
    private Environment env;
    
    public String getSessionTimeOut()
    {
	String someprop = env.getProperty("idcopier.web.timeout");
	return someprop;
    }
}
