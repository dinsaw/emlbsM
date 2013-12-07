/**
 * 
 */
package dev.ds.emlbsm.util.auth;

/**
 * @author dinesh
 *
 */
public interface AuthStrategy {
	public boolean authenticate(String uname,String pass);
}
