/**
Copyright (C)2014 Black Duck Software Inc.
http://www.blackducksoftware.com/
All rights reserved. **/

/**
 * 
 */
package com.blackducksoftware.soleng.idcopier.config;

import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.soleng.idcopier.model.IDCServer;
import com.blackducksoftware.soleng.idcopier.model.IDCServerList;
import com.thoughtworks.xstream.XStream;

/**
 * @author Ari Kamen
 * @date Sep 22, 2014
 * 
 */
public class IDCServerParser
{

    static Logger log = LoggerFactory.getLogger(IDCServerParser.class);

    public List<IDCServer> processServerConfiguration(FileReader fileReader)
    {
	IDCServerList serverList = null;

	XStream xstream = new XStream();

	try
	{
	    xstream.processAnnotations(IDCServer.class);
	    xstream.processAnnotations(IDCServerList.class);

	    serverList = (IDCServerList) xstream.fromXML(fileReader);

	    log.debug("Deserialized XML");

	} catch (Exception e)
	{
	    log.error("Unable to process XML", e);
	}

	return serverList.getServers();
    }

}