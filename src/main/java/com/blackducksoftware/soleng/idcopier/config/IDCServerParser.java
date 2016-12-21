/**
 * Copyright (C)2014 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 */

/**
 *
 */
package com.blackducksoftware.soleng.idcopier.config;

import com.blackducksoftware.soleng.idcopier.model.IDCServer;
import com.blackducksoftware.soleng.idcopier.model.IDCServerList;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.util.List;

/**
 * @author Ari Kamen
 * @date Sep 22, 2014
 *
 */
public class IDCServerParser {

	static Logger log = Logger.getLogger(IDCServerParser.class);

	public List<IDCServer> processServerConfiguration(FileReader fileReader) {
		IDCServerList serverList = new IDCServerList();

		XStream xstream = new XStream();

		try {
			xstream.processAnnotations(IDCServer.class);
			xstream.processAnnotations(IDCServerList.class);

			serverList = (IDCServerList) xstream.fromXML(fileReader);

			log.debug("Deserialized XML");

		} catch (Exception e) {
			log.error("Unable to process XML", e);
		}

		return serverList.getServers();
	}

}