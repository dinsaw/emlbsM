package dev.ds.emlbsm.util.tasks;

import android.content.Intent;
import android.view.View;
import dev.ds.emlbsm.activity.AboutActivity;
import dev.ds.emlbsm.activity.HelpActivity;
import dev.ds.emlbsm.activity.MapActivity;

public class StartActivityUnit {
	private static final String[] items = { "Map", "Nearest Police Station",
			"Crime Spots", "Report Issue", "Settings", "Help", "About" };
	public static Intent getIntentByItemPos(int position,View view) {
		Intent myIntent = null;
		switch (position) {
		case 0:
			myIntent= new Intent( view.getContext(), MapActivity.class);
			break;
		case 5:
			myIntent= new Intent( view.getContext(), HelpActivity.class);
			break;
		case 6:
			myIntent= new Intent( view.getContext(), AboutActivity.class);
			break;
		default:
			break;
		}
		return myIntent; 
	}
	public static String[] getItems() {
		return items;
	}
	
}
