/**
 * 
 */
package dev.ds.emlbsm.util.auth;

/**
 * @author dinesh
 * 
 */
public class SampleAuthStrategy implements AuthStrategy {

	@Override
	public boolean authenticate(String uname, String pass) {
		if (uname.equals("s") && pass.equals("t")) {
			return true;
		}
		return false;
	}

}
