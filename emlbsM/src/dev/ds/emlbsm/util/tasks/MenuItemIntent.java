package dev.ds.emlbsm.util.tasks;

import android.content.Intent;

public class MenuItemIntent {
	private Intent intent;
	private String displayName;
	
	
	public MenuItemIntent(Intent intent, String displayName) {
		this.intent = intent;
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Override
	public boolean equals(Object o) {
		if (this.displayName.equals(((MenuItemIntent)o).displayName)&&this.intent.equals(((MenuItemIntent)o).intent)) {
			return true;
		}
		return false;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
}
