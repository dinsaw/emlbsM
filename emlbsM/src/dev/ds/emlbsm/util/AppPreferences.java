/**
 * 
 */
package dev.ds.emlbsm.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author dinesh
 *
 */
public class AppPreferences {
	public static String serverip = "10.0.2.2";
	public static String serverport = "8080";
	public static String loginURL = "/EMLBS/loginjson";
	public static String issueRequestURL = "/EMLBS/issuerequest";
	public static String getOverlayTypeURL = "/EMLBS/getalloverlaytypes";
	public static String getNewsTitlesURL = "/EMLBS/getloconewstitles";
	public static String getNewsByTitleURL = "/EMLBS/getloconewsbytitle";;
	public static String getMarkers = "/EMLBS/getMarkers";;

	public static final String PREFS_NAME = "emlbsPrefsFile";
	public static final String UNAMEKEY = "user";
	public static final String PASSKEY = "pass";
	
	private static String uname = "";
	private static String pass = "";
	public static final String SERVERIP = "serverip";
	public static final String SERVERPORT = "serverport";
	public static String getUname() {
		return uname;
	}
	public static void setUname(String uname) {
		AppPreferences.uname = uname;
	}
	public static String getPass() {
		return pass;
	}
	public static void setPass(String pass) {
		AppPreferences.pass = pass;
	}
}
