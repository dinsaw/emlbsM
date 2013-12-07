package dev.ds.emlbsm.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;

public class AlphaSettingsActivity extends Activity {
	EditText serverEditText,portEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alpha_settings);
		serverEditText=(EditText) findViewById(R.id.editTextserver);
		portEditText=(EditText) findViewById(R.id.editTextserverport);
		serverEditText.setText(AppPreferences.serverip);
		portEditText.setText(AppPreferences.serverport);
	}
	@Override
	public void onBackPressed() {
		AppPreferences.serverip=serverEditText.getText().toString();
		AppPreferences.serverport=portEditText.getText().toString();
		 SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
		 Editor editor=settings.edit();
		 editor.putString(AppPreferences.SERVERIP, serverEditText.getText().toString());
		 editor.putString(AppPreferences.SERVERPORT, portEditText.getText().toString());
		 editor.commit();
		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alpha_settings, menu);
		return true;
	}

}
