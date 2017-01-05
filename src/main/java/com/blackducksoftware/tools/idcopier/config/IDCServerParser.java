/**
 * IDCopier
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 *
 */
package com.blackducksoftware.tools.idcopier.config;

import com.blackducksoftware.tools.idcopier.model.IDCServer;
import com.blackducksoftware.tools.idcopier.model.IDCServerList;
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