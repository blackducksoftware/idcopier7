package com.blackducksoftware.protex.sdk.idcopier;

public class IDCTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sessId = "xoxoxo";
		String src_projId = "c_aaahthcopyidtestfeb232010";
		String dest_projId = "c_bbb_hthcopyidtest3feb232010";
		String host = "http://coldduck.blackducksoftware.com";
        String srcCodeTreePath = "/lib/jboss.jar";
        String targetCodeTreePath = "/lib/jboss.jar";
		String username = "hhearst@blackducksoftware.com";
        String password = "blackduck";
        
		int treeNum=1;
		
        serverSingleton ss = serverSingleton.getServerSingleton();
        ss.isUserAuthenticated(username, password, sessId, host);

        Boolean ret= ss.copyIDs(host, username, password, src_projId, srcCodeTreePath, dest_projId, targetCodeTreePath, true, sessId, false);
        System.err.println(ret);
        System.exit(-1);
	}

}
