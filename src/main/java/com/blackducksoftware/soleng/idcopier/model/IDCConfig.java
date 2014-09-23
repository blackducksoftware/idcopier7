/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.model;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.blackducksoftware.soleng.idcopier.service.LoginService;

/**
 * @author Ari Kamen
 * @date Sep 19, 2014
 *
 */

@Configuration
// TODO: Make this fatal for now, will need to figure out a nicer way to handle this later.
@PropertySources({ @PropertySource(value = "file:${IDCOPIER_CONFIG}", ignoreResourceNotFound = false) })
public class IDCConfig {
	static Logger log = Logger.getLogger(LoginService.class);

	@Autowired
	private Environment env;

	@Autowired
	ApplicationContext cxt;

	public IDCConfig() {
		log.info("Configuration file has been loaded");
	}

	public String getSessionTimeOut() throws Exception {
		String someprop = env.getProperty("idcopier.web.timeout");
		if (someprop == null)
			throw new Exception("Unable to load session time out, property not defined");
		return someprop;
	}

	public String getProperty(String key) {
		PropertySourcesPlaceholderConfigurer rsp = new PropertySourcesPlaceholderConfigurer();
		log.info("Fetching property with key: " + key);
		String value = env.getProperty(key);
		log.warn("Property key, does not exist: " + key);

		return value;
	}
}
