package com.blackducksoftware.protex.sdk.idcopier;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Date;

import com.blackducksoftware.sdk.protex.project.ProjectInfo;

public class idcSingleton {

	private static idcSingleton myIDCs = null;
	
	private Long cacheTimeout = 3000000L;
	private HashMap<String, List<ProjectInfo>> projectListCache = null;
	private TreeMap<Long, String> cacheTimer = null;
	
	private idcSingleton() {
	  projectListCache = new HashMap<String, List<ProjectInfo>>();
	  cacheTimer = new TreeMap<Long, String>();
	  }
	
	private synchronized void purgeOldProjectLists() {
	  Long cutoffTime = new Date().getTime() - cacheTimeout;
	  if(cacheTimer!=null && cacheTimer.size()>0) {
	    while(cacheTimer.size()>0 && cacheTimer.firstKey()<cutoffTime) {
	      projectListCache.remove(cacheTimer.get(cacheTimer.firstKey()));
	      cacheTimer.remove(cacheTimer.firstKey());
	      }
	    }
	  }
	
	public HashMap<String, List<ProjectInfo>> getProjectListCache() 
	{
		purgeOldProjectLists();
		return projectListCache;
	}

	public synchronized void addToProjListCache(String sessionId, List<ProjectInfo> pList) 
	{
	  projectListCache.put(sessionId, pList);
	  cacheTimer.put(new Date().getTime(), sessionId);
	}
	
	public void setProjectListCache(HashMap<String, List<ProjectInfo>> projectListCache) 
	{
		this.projectListCache = projectListCache;
	 }

	public static synchronized idcSingleton getIdcSingleton() 
	{		
		  if(myIDCs == null) 
		  {
			  myIDCs = new idcSingleton();
		  }
		  
		  return myIDCs;
	}
}
