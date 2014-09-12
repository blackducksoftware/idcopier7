/**
 * 
 */
package com.blackducksoftware.proserv.idcopier.config;

/**
 * @author Ari Kamen
 *
 */
public class IDCConstants {
	
	public static String ID_COPIER_CONFIG_FILE = "config.properties";
	public static String ID_COPIER_SERVER_LIST_LOCATION = "server.list.location";
	public static String SERVER_LIST_FILE_NAME = "server_list.txt";
	
	public static String PROTEX_USER = "protex.user";
	public static String PROTEX_PASSWORD = "protex.password";
	public static String PROTEX_SERVER = "protex.server";
	public static String PROTEX_SDK_ALTERNATE = "protex.sdk.alternate";

	public static String COPY_ID_INHERITS = "protex.copy.id.inherits";
	public static String COPY_ID_DELETE_EXISTING ="protex.copy.id.delete.existing";
	
	// These do not appear to be used
	public static String SDK_CONNECT_TIMEOUT = "protex.sdk.connect.timeout";
	public static String SDK_READ_TIMEOUT = "protex.sdk.read.timeout";
	
	// Internal switches
	public static Integer CODE_TREE_DEPTH = new Integer(1);   // how many levels deep to go into the code tree (this app always goes one level deep)

	
	// 
	public static String SDK_ALTERNATE_BACKDOOR_SWITCH = "ProServ#";
}
