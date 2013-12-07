/**
 * 
 */
package dev.ds.emlbsm.util.auth;

/**
 * @author dinesh
 *
 */
public class AuthContext {
	private AuthStrategy authStrategy;

	public void setAuthStrategy(AuthStrategy authStrategy) {
		this.authStrategy = authStrategy;
	}
	public boolean authenticateUser(String uname, String pass) {
		return authStrategy.authenticate(uname, pass);
	}
}
