/**
 * 
 */
package dev.ds.emlbsm.util.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.view.View;

/**
 * @author dinesh
 *
 */
public class MenuForActivity {
	private List<MenuItemActivity> activities = new ArrayList<MenuItemActivity>();
	public List<String> getAllDisplayNames() {
		List<String> names=new ArrayList<String>();
		for (Iterator<MenuItemActivity> iterator = this.activities.iterator(); iterator.hasNext();) {
			MenuItemActivity a = (MenuItemActivity) iterator.next();
			names.add(a.getDisplayName());
		}
		return names; 
	}
	public void addMenuItemActivity(MenuItemActivity menuItemActivity) {
		activities.add(menuItemActivity);
	}
	public Intent getIntentByItemPos(int position,View view) {
		return new Intent(view.getContext(), activities.get(position).getActivityClass());
	}
}
