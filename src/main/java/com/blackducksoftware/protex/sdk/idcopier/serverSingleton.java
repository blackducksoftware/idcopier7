package com.blackducksoftware.protex.sdk.idcopier;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.TreeMap;
import java.util.Date;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.blackducksoftware.proserv.idcopier.config.IDCConstants;
import com.blackducksoftware.proserv.idcopier.config.IDCProperties;

import com.blackducksoftware.protex.sdk.idcopier.domain.*;
import com.blackducksoftware.sdk.fault.SdkFault;

import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxyV6_2;
import com.blackducksoftware.sdk.protex.common.BomRefreshMode;

import com.blackducksoftware.sdk.protex.project.Project;
import com.blackducksoftware.sdk.protex.project.ProjectApi;
import com.blackducksoftware.sdk.protex.project.ProjectInfo;

import com.blackducksoftware.sdk.protex.project.ProjectColumn;

import com.blackducksoftware.sdk.protex.project.bom.BomApi;
import com.blackducksoftware.sdk.protex.project.bom.BomComponent;

import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeApi;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeWithCount;

import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNode;
import com.blackducksoftware.sdk.protex.project.codetree.CodeTreeNodeType;
import com.blackducksoftware.sdk.protex.project.codetree.PartialCodeTree;
import com.blackducksoftware.sdk.protex.project.codetree.PartialCodeTreeWithCount;

import com.blackducksoftware.sdk.protex.user.UserApi;

import com.blackducksoftware.sdk.protex.util.PageFilterFactory;
import com.blackducksoftware.sdk.protex.project.codetree.discovery.DiscoveryApi;

import com.blackducksoftware.sdk.protex.project.codetree.identification.CodeMatchIdentification;

import com.blackducksoftware.sdk.protex.project.codetree.identification.CodeTreeIdentificationInfo;
import com.blackducksoftware.sdk.protex.project.codetree.identification.DeclaredIdentification;

import com.blackducksoftware.sdk.protex.project.codetree.identification.DependencyIdentification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.Identification;
import com.blackducksoftware.sdk.protex.project.codetree.identification.IdentificationApi;
import com.blackducksoftware.sdk.protex.project.codetree.identification.IdentificationType;
import com.blackducksoftware.sdk.protex.project.codetree.identification.StringSearchIdentification;

public class serverSingleton {

	static Logger 	log 					= 	Logger.getLogger(serverSingleton.class);
	
	private static serverSingleton myServerSingleton = null;
	private static IDCProperties idcProperties = null;

	private static HashMap<String, ICUser> activeUsersBySession = new HashMap<String, ICUser>();


	private static TreeMap activeUsersByLastActivity = new TreeMap();
	private static long inactivityTimeout;

	private static long sdkConnectTimeout;
	private static long sdkReadTimeout;

	private static boolean copyIDsInheritsFromHigherDirs = false;

	private static boolean deleteExistingIdentifications = true;
	
	// 
	private static String versionID = "";
	private static boolean loginAttempted = false;

	/**
	 * Generates a singleton and returns to login.jsp page
	 * Singleton is used to make sure that multiple users do not clash.
	 * 
	 * Sets up configuration instance that houses all properties.
	 * The configuration can be retrieved from JSP (currently used in login page)
	 * @return
	 */
	public static synchronized serverSingleton getServerSingleton() 
	{
		if (myServerSingleton == null) 
		{
			log.info("Generating singleton instance");
			myServerSingleton = new serverSingleton();
		} else {
			pruneActiveUsers();

		}
		pruneActiveUsers();
		return myServerSingleton;
	}
	
	/**
	 * Loads the property file and reads relevant properties.
	 * This method is externalized to provide the user the ability to load properties dynamically through a simple page refresh.
	 */
	public IDCProperties loadProperties() 
	{
		try
		{
			Properties prop = new Properties();
			idcProperties = new IDCProperties();
	
			sdkConnectTimeout = Long.parseLong(prop.getProperty(IDCConstants.SDK_CONNECT_TIMEOUT, "0"));
			sdkReadTimeout = Long.parseLong(prop.getProperty(IDCConstants.SDK_READ_TIMEOUT, "0"));
			
			String copyInherits = prop.getProperty(IDCConstants.COPY_ID_INHERITS, "1");
			if ("1".equals(copyInherits)) {
				copyIDsInheritsFromHigherDirs = true;
			}
	
			String deleteExisting = prop.getProperty(IDCConstants.COPY_ID_DELETE_EXISTING, "0");
			if ("1".equals(deleteExisting)) {
				deleteExistingIdentifications = true;
			}
		} catch (Exception e)
		{
			log.error("Unable to load properties: " + e.getMessage());
		}
		
		return idcProperties;
	}

	public boolean isUserAuthenticated(String login, String password,
			String sessionId, String server) 
	{
		ICUser user = null;
		boolean userIsAuth = false;

		if (activeUsersBySession.containsKey(sessionId)) 
		{
			ICUser activeUser = activeUsersBySession.get(sessionId);
			if (activeUser != null) 
			{
				if(activeUser.getLoginName().equals(login) && activeUser.getPassword().equals(password) && activeUser.getServer().equals(server))
					user = activeUser;
			}
		}

		// Create a new user, this will only happen if the session is empty or an active session is trying multiple logins.
		if(user == null)
		{
			user = new ICUser();
			log.info("User not in memory --- login=" + login+ "  server=" + server + " sessionId="+ sessionId);
			user.setHttpSession(sessionId);
			user.setLastActivity(new Date());
			user.setLoginTime(user.getLastActivity());
			user.setLoginName(login);
			user.setPassword(password);
			user.setServer(server);
		}
	
		// Regardless of old or new, attempt to authenticate again.
		user = authenticateUser(user);
	
		activeUsersBySession.put(sessionId, user);
		activeUsersByLastActivity.put(user.getLastActivity(), user);
		
		userIsAuth = user.isUserAuthorized();
		
		return userIsAuth;
	}
	
	private ICUser authenticateUser(ICUser user) 
	{		

		String password = user.getPassword();
		
		ProtexServerProxyV6_2 myProtexServer = null;
		UserApi userApi = null;
		
		// TODO: Move SDK calls into separate layer.
		try{
			// Grab the sdk alternate flag, if set to true, append ProServ backdoor
			String altSdkUsage = idcProperties.getProperty(IDCConstants.PROTEX_SDK_ALTERNATE);
			if(altSdkUsage != null && altSdkUsage.equals("true"))
			{
				password = IDCConstants.SDK_ALTERNATE_BACKDOOR_SWITCH + password;
			}
			
			myProtexServer = new ProtexServerProxyV6_2(user.getServer(), user.getLoginName(), password);
			userApi = myProtexServer.getUserApi(sdkReadTimeout);
			user.setMyProtexServer(myProtexServer);			
			
		
		} catch (Exception e)
		{
			user.setLoginErrorMessage(e.getMessage());
			user.setUserAuthorized(false);
		}
		
		// Test connection
		try
		{
			userApi.getCurrentUserHasServerFileAccess();
			user.setUserAuthorized(true);
		}	catch (Throwable e) 
		{
			log.error("User login failed: " + e.getMessage());
			user.setLoginErrorMessage(e.getMessage());
			user.setUserAuthorized(false);
		} 
		
		return user;
	}

	public static long getSdkConnectTimeout() {
		return sdkConnectTimeout;
	}

	public static long getSdkReadTimeout() {
		return sdkReadTimeout;
	}


	public static long getInactivityTimeout() {
		return inactivityTimeout;
	}

	public void setInactivityTimeout(long newTimeout) {
		inactivityTimeout = newTimeout;
		pruneActiveUsers();
	}

	public boolean sessionStillActive(String sessionId) {
		boolean ret = false;
		pruneActiveUsers();
		if (activeUsersBySession.get(sessionId) != null)
			ret = true;
		return ret;
	}

	private static void pruneActiveUsers() {
		Date currentDate = new Date();
		long thisOne = 0;
		if (activeUsersByLastActivity.size() > 0) {
			while (0 < thisOne
					&& thisOne < (currentDate.getTime() - inactivityTimeout)) {
				ICUser thisUser = (ICUser) activeUsersByLastActivity
						.get(activeUsersByLastActivity.keySet().toArray()[0]);
				activeUsersBySession.remove(thisUser.getHttpSession());
				activeUsersByLastActivity.remove(activeUsersByLastActivity
						.keySet().toArray()[0]);
				thisOne = ((Long) activeUsersByLastActivity.keySet().toArray()[0])
						.longValue();
			}
		}
	}

	public boolean refreshBOM(String host, String projectId, String sessionID) {
		boolean ret = false;

		ProtexServerProxyV6_2 myProtexServer = null;
		BomApi bomApi = null;

		log.info("Refreshing BOM: host=" + host + " projectId="	+ projectId + " sessionID=" + sessionID);
		
		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionID);
		if (thisUser != null) {
		
			myProtexServer = thisUser.getMyProtexServer();

			Object[] keys = activeUsersBySession.keySet().toArray();
			for (int g = 0; g < keys.length; g++) {
				ICUser tmp = activeUsersBySession.get(keys[g]);

			}
		} else
			return false;

		try {
			bomApi = myProtexServer.getBomApi(sdkReadTimeout);
		} catch (RuntimeException e) {
			System.err.println("Connection to server '" + host + "' failed: "
					+ e.getMessage());
			return ret;
		}

		try {
			bomApi.refreshBom(projectId, true, true);
			ret = true;
		} catch (SdkFault e) {
			e.printStackTrace();
		}

		return ret;
	}


	/**
	 * Generates a list of items (folders, files, etc) directly from the SDK.  Uses several calls to grab both the names of the folders and the pendings counts and then loads them into 
	 * a custom voFolderItem object.
	 * 
	 * 
	 * @param sessionID
	 * @param projectID
	 * @param startingPath
	 * @param host
	 * @param aggregate
	 * @param getPending
	 * @param getIdentified
	 * @param getSeperatePendingCounts
	 * @param excludeRootNode
	 * @return
	 * @throws Exception 
	 */
	private voFolderItem[] getCodeTreeInternal(String sessionID,
			String projectID, String startingPath, String host,
			boolean aggregate, boolean getPending, boolean getIdentified,
			boolean getSeperatePendingCounts, boolean excludeRootNode) throws Exception {

		voFolderItem[] ret = null;
		ArrayList<voFolderItem> retAL = new ArrayList<voFolderItem>();
		
		ProtexServerProxyV6_2 myProtexServer = null;

		long cmPending = 0;
		long ssPending = 0;
		long depPending = 0;

		log.info("getCodeTreeInternal with sessionID='" + sessionID + "' aggregate='" + aggregate   	+ "' startingPath='" + startingPath + "'");
		
		// TODO: Figure out what this is
		if (startingPath.contains("runtime")) {
			log.info("Runtime Path: " + startingPath);
		}
		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionID);
		if (thisUser != null) {
			myProtexServer = thisUser.getMyProtexServer();
		} else
			return null;

		// Make a service
		CodeTreeApi codeTreeApi = null;
		IdentificationApi identificationApi = null;
		DiscoveryApi discoveryApi = null;
		try {
			codeTreeApi = myProtexServer.getCodeTreeApi(sdkReadTimeout);
			identificationApi = myProtexServer.getIdentificationApi(sdkReadTimeout);
			discoveryApi = myProtexServer.getDiscoveryApi(sdkReadTimeout);
		} catch (RuntimeException e) {
			log.error("Connection to server '" + host + "' failed: "+ e.getMessage());
			return null;
		}

		PartialCodeTree codeTree = null;
		if (startingPath == null || startingPath.trim().equals(""))
			startingPath = "/";
		else if (!startingPath.startsWith("/"))
			startingPath = "/" + startingPath;
		while (startingPath.startsWith("//"))
			startingPath = startingPath.substring(1);
		try {
			codeTree = codeTreeApi.getCodeTree(projectID, startingPath, IDCConstants.CODE_TREE_DEPTH,	Boolean.TRUE);
		} catch (SdkFault e) {
			log.error("getCodeTree failed: " + e.getMessage());
		}
		if (codeTree == null) {
			log.error("no getCodeTree returned");
		}

		if (codeTree != null) {
			List<CodeTreeNode> nodes = codeTree.getNodes();
			List<CodeTreeNodeWithCount> nodesWithCount = null;
			List<CodeTreeIdentificationInfo> idTree = null;
			if (getPending) {
				try {
					PartialCodeTreeWithCount treeWithCount = discoveryApi.getAllDiscoveriesPendingIdFileCount(projectID, codeTree);

					if (treeWithCount != null) {
						nodesWithCount = treeWithCount.getNodes();
					}
					else
					{
						throw new Exception("Unable to get code tree, tree empty");
					}
				} catch (SdkFault e) {
					throw new Exception("SDK Fault: " + e.getMessage());
				}
			}
			
			
			if (getSeperatePendingCounts) 
			{
				List<CodeTreeNode> ct = codeTree.getNodes();
				while (ct.size() > 1)
					ct.remove(1);

				log.info("getCodeTreeInternal: after pruning tree, node count is "	+ codeTree.getNodes().size());
				
				try {
					PartialCodeTreeWithCount codeMatchDiscoveryCountTree = discoveryApi.getCodeMatchPendingIdFileCount(projectID, codeTree);
					CodeTreeNodeWithCount cc = codeMatchDiscoveryCountTree.getNodes().get(0);
					cmPending = cc.getCount();
				} catch (SdkFault e) 
				{
					log.error("SDK Fault", e);
				}
				try {
					PartialCodeTreeWithCount stringSearchDiscoveryCountTree = discoveryApi.getStringSearchPendingIdFileCount(projectID,	codeTree);
					CodeTreeNodeWithCount cc = stringSearchDiscoveryCountTree.getNodes().get(0);
					ssPending = cc.getCount();
				} catch (SdkFault e) {
					log.error("SDK Fault", e);
				}
				try {
					PartialCodeTreeWithCount dependencyDiscoveryCountTree = discoveryApi.getDependenciesPendingIdFileCount(projectID,codeTree);
					CodeTreeNodeWithCount cc = dependencyDiscoveryCountTree	.getNodes().get(0);
					depPending = cc.getCount();
				} catch (SdkFault e) {
					log.error("SDK Fault", e);
				}

			} // end getSeperatePendingCounts

			if (getIdentified) {
				try {
					idTree = identificationApi.getAppliedIdentifications(projectID, codeTree);
				} catch (SdkFault e) {
					log.error("SDK Fault", e);
				}
			}

			/**
			 * Loop through all the nodes and populate the internal folderItem object
			 * TODO: This is too long of a loop, refactor!!
			 */
			int index = 0;
			for (CodeTreeNodeWithCount treeNode : nodesWithCount) 
			{
				voFolderItem currentFolderItem = new voFolderItem();
				CodeTreeNodeType nodeType = treeNode.getNodeType();
				CodeTreeIdentificationInfo codeTreeIdentificationInfo = null;
				String type = nodeType.toString();
				String name = treeNode.getName();
				
				
				currentFolderItem.setPendingCount(treeNode.getCount());
				
				if (getSeperatePendingCounts) 
				{
					currentFolderItem.setPendingCMCount(cmPending);
					currentFolderItem.setPendingDepCount(depPending);
					currentFolderItem.setPendingSSCount(ssPending);
				}
				
				if (getIdentified) 
				{
					codeTreeIdentificationInfo = idTree.get(index);
					int cmCount = 0;
					int ssCount = 0;
					List<Identification> ids = codeTreeIdentificationInfo.getIdentifications();
					for (int h = 0; h < ids.size(); h++) 
					{
						Identification i = ids.get(h);
						if (i.getType().equals(IdentificationType.CODE_MATCH)) 
						{
							cmCount++;
						} 
						else if (i.getType().equals(IdentificationType.STRING_SEARCH)) 
						{
							ssCount++;
						}
					}
					currentFolderItem.setIdCMCount(cmCount);
					currentFolderItem.setIdSSCount(ssCount);
					if (currentFolderItem.getIdSSCount() > 0) 
					{
						// TODO: Not sure why this is important 
						log.warn("Set SS count to "	+ currentFolderItem.getIdSSCount());
					}
					int depCount = 0;
					List<Identification> idList = codeTreeIdentificationInfo.getIdentifications();
					for (int h = 0; h < idList.size(); h++) {
						Identification i = idList.get(h);
						if (i.getType().equals(IdentificationType.DEPENDENCY)) {
							depCount++;
						}
					}

					currentFolderItem.setIdDepCount(depCount);
				} // getIdentified
				
				if (!excludeRootNode || (!("".equals(name.trim())))) {
					String subPath = startingPath;
					if (!subPath.equals("/"))
						subPath = subPath + "/";

					subPath = subPath + name;

					if (aggregate) {
						currentFolderItem.setType(type.toLowerCase());
						currentFolderItem.setName(name);
						currentFolderItem.setFullpath("");
						currentFolderItem.setLevel(0);
						retAL.add(currentFolderItem);
					} else {
						currentFolderItem.setType(type.toLowerCase());
						currentFolderItem.setName(name);

						if (name == null) {
							name = "";
						}

						StringBuffer newPath = new StringBuffer();

						if (startingPath.equals("") || startingPath.equals("/")) {
							newPath.append("/");
							newPath.append(name);
						} else {
							newPath.append(startingPath);
							newPath.append("/");
							newPath.append(name);
						}

						currentFolderItem.setType(type.toLowerCase());
						currentFolderItem.setName(name);
		
						currentFolderItem.setFullpath(newPath.toString());
		
						String pathCount = newPath.toString();
						currentFolderItem.setLevel(0);
						while (pathCount.indexOf("/") >= 0) {
							currentFolderItem.setLevel(currentFolderItem.getLevel() + 1);
							pathCount = pathCount.substring(pathCount
									.indexOf("/") + 1);
						}
						retAL.add(currentFolderItem);
					}
				}
			} // End node loop
		} else 
		{
			log.error("Empty!?");
		}
		
		try
		{
			/**
			 * Sort the raw list to make sure that it is:
			 * 1) Alphabetic
			 * 2) Places emphasis on folders over files
			 * TODO:  At this moment, the list is sorted twice - there may be a way to do it in one comparator, but I have not found the way. --AK
			 */
			  Collections.sort(retAL, new Comparator<voFolderItem>() 
			  {
			        public int compare(final voFolderItem object1, final voFolderItem object2) 
			        {			        
			        	String name = object1.getName().toLowerCase();
			        	String compareName = object2.getName().toLowerCase();
		
			            return name.compareTo(compareName);
			        }
			   });
			  // Here it is important to compare the second object to the first in order to ensure that folders end up on top!
			  Collections.sort(retAL, new Comparator<voFolderItem>() 
					  {
					        public int compare(final voFolderItem object1, final voFolderItem object2) 
					        {
					        	String type = object1.getType();
					        	String compareType = object2.getType();

					            int result =  compareType.compareTo(type);
					  				        
					            return result;
					        }
					   });

		} catch (Exception e)
		{
			log.warn("Unable to sort folder/file list:" + e.getMessage());
		}
		
		
		Object[] tmpArray = retAL.toArray();
		if (tmpArray != null) {
			ret = new voFolderItem[tmpArray.length];
			for (int w = 0; w < tmpArray.length; w++) {
				ret[w] = (voFolderItem) tmpArray[w];
			}
		}
		
	
		return ret;
	}

	/**
	 * TODO: What is the point of this method?
	 * @param sessionID
	 * @param projectID
	 * @param startingPath
	 * @param host
	 * @param aggregate
	 * @param treeNum
	 * @param getPending
	 * @param getSeperatePendingCounts TODO: What is this for?
	 * @param getIdentified TODO: What is this for?
	 * @return
	 * @throws Exception 
	 */
	public synchronized String getCodeTreeHTML(String sessionID,
			String projectID, String startingPath, String host,
			boolean aggregate, int treeNum, boolean getPending,
			boolean getSeperatePendingCounts, boolean getIdentified) throws Exception 
	{
		String ret = getCodeTreeHTML(sessionID, projectID, startingPath, host,
				aggregate, treeNum, getPending, getSeperatePendingCounts,
				getIdentified, true);

		return ret;
	}

	/**
	 * TODO:  This is one ugly method.  Builds on the fly HTML/Javascript with all sorts of embedded icons and functions.
	 * Painful, needs to be refactored ASAP.
	 * @param sessionID
	 * @param projectID
	 * @param startingPath
	 * @param host
	 * @param aggregate
	 * @param treeNum
	 * @param getPending
	 * @param getSeperatePendingCounts
	 * @param getIdentified
	 * @param excludeRootNode
	 * @return
	 * @throws Exception 
	 */
	public synchronized String getCodeTreeHTML(String sessionID,
			String projectID, String startingPath, String host,
			boolean aggregate, int treeNum, boolean getPending,
			boolean getSeperatePendingCounts, boolean getIdentified,
			boolean excludeRootNode) throws Exception
	{
		StringBuffer retSB = new StringBuffer();
		String protexSessionID = "";
		
		if (protexSessionID == null || protexSessionID.trim().equals(""))
			protexSessionID = sessionID;
		
		log.info("getCodeTreeHTML with sessionID='" + sessionID	+ "' and protexSessionID='" + protexSessionID	+ "' and getPending=" + getPending + " and host=" + host);
		
		voFolderItem[] nodes = getCodeTreeInternal(protexSessionID, projectID,startingPath, host, aggregate, getPending,getSeperatePendingCounts, getIdentified, excludeRootNode);
		
		if (nodes != null) 
		{
			if (startingPath == null || startingPath.trim().length() == 0) {
				// output project header
				Project thisProj = getProject(projectID, protexSessionID);
				if (thisProj != null) {
					retSB.append("<a href='javascript:q(\"" + host + "\",\""
							+ projectID + "\"," + treeNum + ")'>"
							+ thisProj.getName() + "</a>");
					if (!excludeRootNode)
						retSB.append("<font color='gray'>("
								+ nodes[0].getPendingCount() + ")</font>");
					retSB.append("<br />");
				}
			}
			int startingPoint = 0;
			if (!excludeRootNode)
				startingPoint = 1;
			for (int g = startingPoint; g < nodes.length; g++) {
				voFolderItem thisItem = nodes[g];
				System.err.println("node " + g + " name is "
						+ thisItem.getFullpath());
				retSB.append("<img src='images/blankdot.gif' width='"
						+ ((thisItem.getLevel() + 1) * 15) + "' height='12'>");
				if (thisItem.getType().equals("folder") || thisItem.getType().equals("archive") || thisItem.getType().equalsIgnoreCase(	"EXPANDED_ARCHIVE")) 
				{
					retSB.append("<a href=\"javascript:o('"
							+ thisItem.getFullpath().replace("'", "\\'")
							+ "','"
							+ host
							+ "','"
							+ projectID
							+ "',"
							+ treeNum
							+ ")\"><img border='0' id=\"img_"
							+ thisItem.getFullpath()
							+ "_"
							+ treeNum
							+ "\" src='images/spdn_closed.gif' width='12' height='12'></a>");
				} else {
					retSB.append("<img src='images/blankdot.gif' width='12' height='12'>");
				}
				retSB.append("&nbsp;<a href=\"javascript:g('"
						+ thisItem.getFullpath().replace("'", "\\'") + "','"
						+ host + "','" + projectID + "'," + treeNum + ")\">"
						+ nodes[g].getName() + "</a>");

				if (nodes[g].getPendingCount() > 0) {
					retSB.append("<font color='gray'>("
							+ nodes[g].getPendingCount() + ")</font>");
				}
				if (nodes[g].getIdCMCount() > 0 || nodes[g].getIdSSCount() > 0
						|| nodes[g].getIdDepCount() > 0) {
					retSB.append("<font color='green'>("
							+ nodes[g].getIdCMCount() + " CM, "
							+ nodes[g].getIdSSCount() + " SS, "
							+ nodes[g].getIdDepCount() + " Deps)</font>");
				}
				if (nodes[g].isDisplay()) {
					retSB.append("(!)");
				}
				retSB.append("</a><br/>");
				if (thisItem.getType().equals("folder")
						|| thisItem.getType().equals("archive")
						|| thisItem.getType().equalsIgnoreCase(
								"EXPANDED_ARCHIVE")) {
					retSB.append("<div id=\"" + thisItem.getFullpath() + "_k"
							+ treeNum + "\"></div>");
				}
			}
		}

		return retSB.toString();
	}

	public String getProjectIdFromName(String projName, String sessionId) {
		String ret = null;

		ProtexServerProxyV6_2 myProtexServer = null;

		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionId);
		if (thisUser != null)
			myProtexServer = thisUser.getMyProtexServer();
		else
			return null;

		// Make a service
		ProjectApi projectApi = null;
		try {
			String user = getUserName(sessionId);
			String password = getPassword(sessionId);
			projectApi = myProtexServer.getProjectApi(sdkReadTimeout);
		} catch (RuntimeException e) {
			System.err
					.println("Connection to server failed: " + e.getMessage());
			return null;
		}

		try {
			Project tmp = projectApi.getProjectByName(projName);
			if (tmp != null)
				ret = tmp.getProjectId();
		} catch (SdkFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public Project getProject(String projectId, String sessionId) {
		// This list will be a combination of the info from the SDK
		// enriched with the queuing status, if there is any

		// System.err.println("getting project details for projectId '" +
		// projectId + "' with sessionId '" + sessionId + "'");

		ProtexServerProxyV6_2 myProtexServer = null;

		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionId);
		if (thisUser != null)
			myProtexServer = thisUser.getMyProtexServer();
		else
			return null;
		// if(hostSessionMap.containsKey(sessionId)) thishost =
		// hostSessionMap.get(sessionId);

		String thisHost = getHost(sessionId);

		// Make a service
		ProjectApi projectApi = null;
		try {
			String user = getUserName(sessionId);
			String password = getPassword(sessionId);
			projectApi = myProtexServer.getProjectApi(sdkReadTimeout);
		} catch (RuntimeException e) {
			System.err.println("Connection to server '" + thisHost
					+ "' failed: " + e.getMessage());
			return null;
		}

		Project proj = new Project();
		try {
			proj = projectApi.getProjectById(projectId);
		} catch (SdkFault e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return proj;

	}

	public ICUser getUserObjectBySession(String sessionID)
	{
		ICUser user = null;
		user = (ICUser) activeUsersBySession.get(sessionID);
		
		return user;
	}
	
	public String getPassword(String sessionID) {
		String ret = "";
		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionID);
		if (thisUser != null)
			ret = thisUser.getPassword();
		return ret;
	}

	public String getUserName(String sessionID) {
		String ret = "";
		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionID);
		if (thisUser != null)
			ret = thisUser.getLoginName();
		return ret;
	}

	public static String getHost(String sessionId) {
		String thisHost = null;
		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionId);
		if (thisUser != null)
			thisHost = thisUser.getServer();
		return thisHost;
	}

	public void logoutUserSession(String sessionId) {
		if (!activeUsersBySession.containsKey(sessionId))
			return;

		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionId);
		if (thisUser != null) {
			thisUser.setMyProtexServer(null);
		}
		activeUsersBySession.remove(sessionId);
		activeUsersByLastActivity.remove(thisUser.getLastActivity());
	}

	/**
	 * Generates a list of projects to be displayed on the get_proj_list page
	 * @param user
	 * @param pw
	 * @param sessionId
	 * @param getSeperatePendingInfo
	 * @return
	 */
	public List<ProjectInfo> listProjectsForDropdowns(String user, String pw,
			String sessionId, boolean getSeperatePendingInfo) 
	{

		List<ProjectInfo> ret = null;

		System.err.println("listProjectsForUser: getSeperatePendingInfo="
				+ getSeperatePendingInfo);

		ICUser vuser = activeUsersBySession.get(sessionId);

		if (isUserAuthenticated(user, pw, sessionId, vuser.getServer())) {
			ret = listProjects(sessionId);
		}

		return ret;
	}

	public List<ProjectInfo> listProjects(String sessionId) {
		// This list will be a combination of the info from the SDK
		// enriched with the queuing status, if there is any
		List<ProjectInfo> ret = null;
		ProtexServerProxyV6_2 myProtexServer = null;

		ICUser thisUser = (ICUser) activeUsersBySession.get(sessionId);

		if (thisUser == null) 
		{
			log.error("listProjects fails because thisUser is NULL for sessonId "+ sessionId);
			return ret;
		} else {

			myProtexServer = thisUser.getMyProtexServer();

		}

		// TODO:  Move to SDK layer
		ProjectApi projectApi = null;

		try {
			projectApi = myProtexServer.getProjectApi(sdkReadTimeout);
		} catch (RuntimeException e) {
			log.error("Connection to server failed: " + e.getMessage());
			return null;
		}

		try {
			ret = projectApi.getProjectsByUser(thisUser.getLoginName());
		} catch (SdkFault e) 
		{

			log.error("Could not get projects: " + e.getMessage());
		}

		return ret;
	}

	public boolean copyBOMOverrides(String serverUri, String username,
			String password, String sourceProjectId, String sourceCodeTreePath,
			String targetProjectId, String targetCodeTreePath,
			boolean isRecursive, boolean concatIfDestNotEmpty,
			boolean deleteIfSourceEmpty, String sessionId,
			boolean deferBOMRefresh) {
		boolean ret = true;
		ProtexServerProxyV6_2 myProtexServer = null;

		if (sourceProjectId.equals(targetProjectId)) {
			return true;
		}

		ICUser thisUser = activeUsersBySession.get(sessionId);

		if (thisUser == null) {
			return false;
		} else {
			myProtexServer = thisUser.getMyProtexServer();
		}

		BomApi bomApi = null;
		try {
			bomApi = myProtexServer.getBomApi(sdkReadTimeout);
		} catch (RuntimeException e) {
			ret = false;
			System.err.println("Connection to server '" + serverUri
					+ "' failed: " + e.getMessage());
			return false;
		}

		try {
			List<BomComponent> sourceComponents = bomApi.getBomComponentsForPath(
					sourceProjectId, sourceCodeTreePath);
			List<BomComponent> targetComponents = bomApi.getBomComponentsForPath(
					targetProjectId, targetCodeTreePath);
			if (sourceComponents.size() > 0) {
				HashMap<String, BomComponent> hashSrc = new HashMap<String, BomComponent>();
				for (int e = 0; e < sourceComponents.size(); e++) {
					BomComponent sourceComponent = sourceComponents.get(e);
					hashSrc.put(
							sourceComponent.getComponentId() + "#"
									+ sourceComponent.getVersionId(), sourceComponent);

				}
				for (int e = 0; e < targetComponents.size(); e++) {
					BomComponent targetComponent = targetComponents.get(e);

					BomComponent sourceComponent = hashSrc.get(targetComponent.getComponentId()
							+ "#" + targetComponent.getVersionId());
					if (sourceComponent != null) {

						if (targetComponent.getVersionId() == null) {
							targetComponent.setBomVersionName(sourceComponent
									.getBomVersionName());
						} else {
							targetComponent.setBomVersionName(null);
						}

						targetComponent.setLicenseInfo(sourceComponent.getLicenseInfo());
						
						bomApi.updateBomComponent(targetProjectId, targetComponent,
								BomRefreshMode.SKIP);
						
						bomApi.setComponentComment(targetProjectId, targetComponent.getComponentId(), targetComponent.getVersionId(), 
								bomApi.getComponentComment(sourceProjectId, sourceComponent.getComponentId(), sourceComponent.getVersionId()));
					}
				}
			}
		} catch (SdkFault e) {
			System.err.println("Error performing BOM Overwrite operations");
			e.printStackTrace();
			return false;
		}

		if (!deferBOMRefresh)
			refreshBOM(serverUri, targetProjectId, sessionId);

		return ret;
	}

	public boolean copyIDs(String serverUri, String username, String password,
			String sourceProjectId, String sourceCodeTreePath,
			String targetProjectId, String targetCodeTreePath,
			boolean isRecursive, String sessionId, boolean deferBOMRefresh) {

		/**
		 * The paths are coming in as encoded from the copyIDs.jsp pages and must be decoded.
		 */
		sourceCodeTreePath = SDKUtils.decodeString(sourceCodeTreePath);
		targetCodeTreePath = SDKUtils.decodeString(targetCodeTreePath);
		
		boolean ret = true;

		ProtexServerProxyV6_2 myProtexServer = null;

		ICUser thisUser = activeUsersBySession.get(sessionId);

		if (thisUser == null) {
			return false;
		} else {
			myProtexServer = thisUser.getMyProtexServer();
		}

		Boolean recursiveOption = new Boolean(isRecursive);

		log.info("Asked to copy Identifications "
				+ (recursiveOption ? "recursively " : "") + "from '"
				+ sourceProjectId + "'::'" + sourceCodeTreePath + "' to '"
				+ targetProjectId + "'::'" + targetCodeTreePath
				+ "' and deferBOMRefresh=" + deferBOMRefresh);

		ProjectApi projectApi = null;
		IdentificationApi identificationApi = null;
		BomApi bomApi = null;

		try {
			projectApi = myProtexServer.getProjectApi(sdkReadTimeout);
			bomApi = myProtexServer.getBomApi(sdkReadTimeout);
			identificationApi = myProtexServer
					.getIdentificationApi(sdkReadTimeout);

		} catch (RuntimeException e) {
			ret = false;
			System.err.println("Connection to server '" + serverUri
					+ "' failed: " + e.getMessage());
			return false;
		}

		// checking the source and target project to be valid
		Project p = null;
		try {
			p = projectApi.getProjectById(sourceProjectId);
			if (p == null) {
				System.out.println("No project found for Project ID = '"
						+ sourceProjectId + "'");
				return false;
				// System.exit(-2);
			}
		} catch (SdkFault e) {
			ret = false;
			System.err.println("getProjectById for sourceProject '"
					+ sourceProjectId + "' failed: " + e.getMessage());
			return ret;
			// System.exit(-1);
		}
		if (!sourceProjectId.equals(targetProjectId)) {
			try {
				p = projectApi.getProjectById(targetProjectId);
				if (p == null) {
					System.out.println("No project found for Project ID = '"
							+ targetProjectId + "'");
					return false;
				}
			} catch (SdkFault e) {
				ret = false;
				System.err.println("getProjectById for sourceProject '"
						+ targetProjectId + "' failed: " + e.getMessage());
				// System.exit(-1);
				e.printStackTrace();
			}
		}

		try {
			long startMillis = new Date().getTime();
			// System.err.println("-----------deferring BOM refresh");

			identificationApi.copyIdentifications(sourceProjectId,
					sourceCodeTreePath, targetProjectId, targetCodeTreePath,
					recursiveOption, deleteExistingIdentifications,
					BomRefreshMode.SKIP);

			System.err.println("Identifications copied from '"
					+ sourceProjectId + "'::'" + sourceCodeTreePath + "' to '"
					+ targetProjectId + "'::'" + targetCodeTreePath
					+ "' deferBOMRefresh=" + deferBOMRefresh);
			System.err.println("Orig Total Millis "
					+ ((new Date().getTime() - startMillis)));

			int treeDepth = 1;
			if (copyIDsInheritsFromHigherDirs) {
				System.err.println("\n" + "\t*****\t"
						+ "CURRENTLY COPYING ID'S FROM HIGHER DIRECTORIES"
						+ "\t*****" + "\n");
				String tmpSourceTreePath = sourceCodeTreePath;
				while (tmpSourceTreePath.length() > 1) {
					tmpSourceTreePath = tmpSourceTreePath.substring(0,
							tmpSourceTreePath.lastIndexOf("/"));
					if (!(sourceProjectId.equals(targetProjectId) && (targetCodeTreePath
							.equals(tmpSourceTreePath) || targetCodeTreePath
							.startsWith(tmpSourceTreePath + "/")))) {
						if (tmpSourceTreePath.length() == 0) {
							tmpSourceTreePath = "/";
						}

						startMillis = new Date().getTime();
						identificationApi.copyIdentifications(sourceProjectId,
								tmpSourceTreePath, targetProjectId,
								targetCodeTreePath, false, false,
								BomRefreshMode.SKIP);
						System.err.println("Loop Total Millis "
								+ ((new Date().getTime() - startMillis)));
						System.err.println("Identifications copied from '"
								+ sourceProjectId + "'::'" + tmpSourceTreePath
								+ "' to '" + targetProjectId + "'::'"
								+ targetCodeTreePath + "'");
					}
				}
			}

			copyBOMOverrides(serverUri, username, password, sourceProjectId,
					sourceCodeTreePath, targetProjectId, targetCodeTreePath,
					isRecursive, true, true, sessionId, deferBOMRefresh);

			if (!deferBOMRefresh) {
				bomApi.refreshBom(targetProjectId, false, true);
			}

		} catch (SdkFault e) {
			System.err.println("copyIdentifications failed: " + e.getMessage());
			e.printStackTrace();
			// System.exit(-1);
			ret = false;
		}
		return ret;
	}

	private void clearTargetIds(CodeTreeApi codeTreeApi,
			IdentificationApi identificationApi, String targetProjectId,
			String targetCodeTreePath) {
		PartialCodeTree totalCodeTree = SDKUtils.getCodeTree(codeTreeApi,
				targetProjectId, targetCodeTreePath, -1);

		for (CodeTreeNode node : totalCodeTree.getNodes()) {

			String nodePath = targetCodeTreePath
					+ (targetCodeTreePath.equals("/") ? "" : "/")
					+ node.getName();

			System.out.println("\nChecking Target Node: " + nodePath);

			List<Identification> ids = SDKUtils.getAppliedIdsForNode(
					identificationApi, targetProjectId,
					totalCodeTree.getParentPath(), node);

			for (Identification id : ids) {

				try {
					if (id.getType().equals(IdentificationType.CODE_MATCH)) {

						identificationApi.removeCodeMatchIdentification(
								targetProjectId, (CodeMatchIdentification) id,
								BomRefreshMode.SKIP);

					} else if (id.getType().equals(
							IdentificationType.DECLARATION)) {

						System.out
								.println("Removing Declared Identification...");
						identificationApi.removeDeclaredIdentification(
								targetProjectId, (DeclaredIdentification) id,
								BomRefreshMode.SKIP);

					} else if (id.getType().equals(
							IdentificationType.STRING_SEARCH)) {

						System.out
								.println("Removing String Search Identification...");
						identificationApi.removeStringSearchIdentification(
								targetProjectId,
								(StringSearchIdentification) id,
								BomRefreshMode.SKIP);

					}

					else if (id.getType().equals(IdentificationType.DEPENDENCY)) {

						System.out
								.println("Removing Dependency Identification...");
						identificationApi.removeDependencyIdentification(
								targetProjectId, (DependencyIdentification) id,
								BomRefreshMode.SKIP);

					}

				}

				catch (SdkFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
	
	public static void setVersion(String version) 
	{
		versionID = version;
		
	}
	
	public static String getVersion()
	{
		return versionID;
	}

	public static boolean isLoginAttempted() {
		return loginAttempted;
	}

	public static void setLoginAttempted(boolean loginAttempted) {
		serverSingleton.loginAttempted = loginAttempted;
	}
}
