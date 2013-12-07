package dev.ds.emlbsm.util.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.view.View;

public class MenuForIntent {
	private List<MenuItemIntent> activities=new ArrayList<MenuItemIntent>();
	
	public List<String> getAllDisplayNames() {
		List<String> names=new ArrayList<String>();
		for (Iterator<MenuItemIntent> iterator = this.activities.iterator(); iterator.hasNext();) {
			MenuItemIntent a = (MenuItemIntent) iterator.next();
			names.add(a.getDisplayName());
		}
		return names; 
	}
	public void addMenuItemIntent(MenuItemIntent menuItemIntent) {
		activities.add(menuItemIntent);
	}
	public Intent getIntentByItemPos(int position,View view) {
		return activities.get(position).getIntent(); 
	}
}
