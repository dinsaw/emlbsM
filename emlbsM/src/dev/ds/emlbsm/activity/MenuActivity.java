package dev.ds.emlbsm.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;
import dev.ds.emlbsm.util.tasks.MenuForActivity;
import dev.ds.emlbsm.util.tasks.MenuItemActivity;

public class MenuActivity extends ListActivity {
	
	MenuForActivity menuForActivity;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_menu);
		createMenu();
		checkPrefFile();
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menuForActivity.getAllDisplayNames()));
	}
	@Override
	public void onBackPressed() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
	}
	private void createMenu() {
		menuForActivity = new MenuForActivity();
		menuForActivity.addMenuItemActivity(new MenuItemActivity(MapActivity.class, "CityMap"));
		menuForActivity.addMenuItemActivity(new MenuItemActivity(IssueActivity.class, "Issue Complaint"));
		menuForActivity.addMenuItemActivity(new MenuItemActivity(NewsMenuActivity.class, "Loco News"));
		menuForActivity.addMenuItemActivity(new MenuItemActivity(HelpActivity.class, "Help"));
		menuForActivity.addMenuItemActivity(new MenuItemActivity(AboutActivity.class, "About"));
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		//startActivity(StartActivityUnit.getIntentByItemPos(position, v));
		startActivity(menuForActivity.getIntentByItemPos(position, v));
	}
	public void checkPrefFile() {
		 SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
		 AppPreferences.setUname(settings.getString(AppPreferences.UNAMEKEY, ""));
		 AppPreferences.setPass(settings.getString(AppPreferences.PASSKEY, ""));
	}
	@Override
	protected void onPause() {
		checkPrefFile();
		super.onPause();
	}
	@Override
	protected void onRestart() {
		checkPrefFile();
		super.onRestart();
	}
}
