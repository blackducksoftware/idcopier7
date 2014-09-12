<%@ page import="java.util.*,com.blackducksoftware.protex.sdk.idcopier.idcSingleton,com.blackducksoftware.protex.sdk.idcopier.serverSingleton, com.blackducksoftware.sdk.protex.project.ProjectInfo" language="java" %><%

	serverSingleton ss = serverSingleton.getServerSingleton();
	idcSingleton idc = idcSingleton.getIdcSingleton();
	
	String host = request.getParameter("h");
	String sTreeNum = request.getParameter("t");
	String u = ss.getUserName(session.getId());
	String p = ss.getPassword(session.getId());
	
	int treeNum = 1;
	if(sTreeNum != null) 
	{
	  try 
	  {
	  		treeNum = Integer.parseInt(sTreeNum);
	  }
	  catch (Exception e) {}
	}

	List<ProjectInfo> projects = null;
	HashMap<String, List<ProjectInfo>> pListCache = idc.getProjectListCache();
	if(pListCache.containsKey(session.getId())) projects = pListCache.get(session.getId());
	else 
	{
		  projects=ss.listProjectsForDropdowns(u, p, session.getId(), false);
		  idc.addToProjListCache(session.getId(), projects);
	}
	if(projects!=null) 
	{
		  out.print("<option value=''>please select a project</option>");
		  for(int g=0;g<projects.size();g++) 
		  {
			    ProjectInfo proj = projects.get(g);
			    out.print("<option value='" + proj.getProjectId() + "'>" + proj.getName() + "</option>");
		  }  
	}
//System.err.println("ret is " + ret);
%>
