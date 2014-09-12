/**
 * 
 */
package com.blackducksoftware.proserv.idcopier.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * All purpose config loader
 * @author Ari Kamen
 *
 */
public class IDCProperties extends IDCConstants {
	
	static Logger 	log 					= 	Logger.getLogger(IDCProperties.class);
	private Properties prop = null;
	
	private ArrayList<String> serverList = new ArrayList<String>();
	
	private String protexUser;
	private String protexPassword;
	private String protexServer;
	
	public IDCProperties () throws Exception
	{
		loadProps();
		populateServerList();
	}

	private void loadProps() throws Exception 
	{
		prop = new Properties();

		try 
		{
				prop.load(this.getClass().getClassLoader().getResourceAsStream(IDCConstants.ID_COPIER_CONFIG_FILE));
				
		} catch (IOException e)
		{
			throw new Exception("Unable to properly load configuration file!");
		}
		
	}

	/**
	 * Grab the location of the server list, then read it.
	 * If location of server list yields nothing, use internal file.
	 * If internal file is empty, leave list blank.
	 */
	private void populateServerList() 
	{
		String serverListLocation = (String)prop.get(ID_COPIER_SERVER_LIST_LOCATION);
		File slLocationPath = new File(serverListLocation);
		
		if(slLocationPath.exists())
			populateInternalServerList(slLocationPath);
		else
		{
			URL internalServerListURL = this.getClass().getClassLoader().getResource(SERVER_LIST_FILE_NAME);
			if(internalServerListURL != null)
			{
				try{
					File f = new File(internalServerListURL.toURI());
					populateInternalServerList(f);
				} catch (Exception e)
				{
					log.warn("Unable to process server list URL: " + internalServerListURL.toString());
					log.warn("Problem: " + e.getMessage());
				}
			}
			else
			{
				log.warn("Unable to load internal resource file for server list!");
			}
		}
		
	}


	private void populateInternalServerList(File slLocationPath) 
	{
		FileInputStream in = null;
		BufferedReader br = null;
		
		try
		{
			  in = new FileInputStream(slLocationPath);
			  br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			 
			  while((strLine = br.readLine())!= null)
			  {
				  strLine = strLine.trim();
				  serverList.add(strLine);		  
			  }
			 
			  in.close();
			  br.close();
			  
			  }catch(Exception e)
			  {
				  log.warn("Unable to read server list file at location: " + slLocationPath);
				  log.warn("Potential problem: " + e.getMessage());
			  }
		finally
		{
			try
			{
				in.close();
				br.close();
			} catch (Exception ignore){}
		}
		
	}

	public String getProperty(String propName) throws Exception
	{
		String value = null;
		
		if(prop == null)
			loadProps();
		
		value = prop.getProperty(propName);
		log.info("Got the following: " + value + " for property: " + propName);
		
		
		return value;
		
	}

	public ArrayList<String> getServerList() {
		return serverList;
	}
}
