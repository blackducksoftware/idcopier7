package com.blackducksoftware.protex.sdk.idcopier;

import java.io.*;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class ServletInitializer extends HttpServlet {

	static Logger 	log 					= 	Logger.getLogger(ServletInitializer.class);
	
	public void init() throws ServletException 
	{
		// / Automatically java script can run here
		serverSingleton.getServerSingleton();
		log.info("*** Servlet Initialized successfully ***");
		
		String version = getVersion();
		serverSingleton.setVersion(version);

	}

	/**
	 * Grabs the version from the MANIFEST file
	 * The version is injected through the pom.xml file using the war plugin
	 * @return
	 */
	private String getVersion() 
	{
	    String version = "";
		
		String impVer = getClass().getPackage().getImplementationVersion();
		
		if(impVer == null)
		{
			 Properties prop = new Properties();
			    try {
			    	ServletContext application = getServletConfig().getServletContext();
			        prop.load(application.getResourceAsStream("/META-INF/MANIFEST.MF"));
			        if(prop != null)
			        {
			        	version = prop.getProperty("Implementation-Version");
			        	log.info("Deplying version: " + version);
			        }
			    } catch (IOException e) {
			        log.warn(e.toString());
			    }
		}

		return version;

	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		
	}

}
