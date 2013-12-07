/**
 * 
 */
package dev.ds.emlbsm.util.tasks;

import android.app.Activity;

/**
 * @author dinesh
 *
 */
public class MenuItemActivity {
	private Class<? extends Activity> activityClass;
	private String displayName;
	
	public MenuItemActivity(Class<? extends Activity> activityClass,
			String displayName) {
		this.activityClass = activityClass;
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Class<? extends Activity> getActivityClass() {
		return activityClass;
	}
	public void setActivityClass(Class<? extends Activity> activityClass) {
		this.activityClass = activityClass;
	}
}
