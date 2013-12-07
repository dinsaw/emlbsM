package dev.ds.emlbsm.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;
import dev.ds.emlbsm.util.auth.AuthContext;
import dev.ds.emlbsm.util.auth.JSONAuthStrategy;

public class MainActivity extends Activity {
	Button button,alphaButton;
	EditText unameEditText,passEditText;
	CheckBox checkBox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
		setLastUserDetails();
		
	}
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
	}
	private void setLastUserDetails() {
		SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
		if (!settings.getString(AppPreferences.UNAMEKEY, "").equals("")) {
			char uname[]=settings.getString(AppPreferences.UNAMEKEY, "").toCharArray();
			char pass[]=settings.getString(AppPreferences.PASSKEY, "").toCharArray();
			unameEditText.setText(uname, 0, uname.length);
			passEditText.setText(pass, 0, pass.length);
			checkBox.setChecked(true);
		}
		if (!settings.getString(AppPreferences.SERVERIP, "").equals("")) {
			char ip[]=settings.getString(AppPreferences.SERVERIP, "").toCharArray();
			char port[]=settings.getString(AppPreferences.SERVERPORT, "").toCharArray();
			AppPreferences.serverip = new String(ip);
			AppPreferences.serverport = new String(port);
			checkBox.setChecked(true);
		}
	}

	private void addListenerOnButton() {
		button = (Button) findViewById(R.id.button1);
		alphaButton = (Button) findViewById(R.id.buttonalpha);
		
		unameEditText = (EditText) findViewById(R.id.editText1);
		passEditText=(EditText) findViewById(R.id.editText2);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				AuthContext authContext=new AuthContext();
				authContext.setAuthStrategy(new JSONAuthStrategy());
				String uname = unameEditText.getText().toString();
				String pass = passEditText.getText().toString();
				if (authContext.authenticateUser(uname, pass)) {
					 if (checkBox.isChecked()) {
						 SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
						 Editor editor=settings.edit();
						 editor.putString(AppPreferences.UNAMEKEY, uname);
						 editor.putString(AppPreferences.PASSKEY, pass);
						 editor.commit();
					 } else {
						 SharedPreferences settings = getSharedPreferences(AppPreferences.PREFS_NAME, 0);
						 Editor editor=settings.edit();
						 editor.remove(AppPreferences.UNAMEKEY);
						 editor.remove(AppPreferences.PASSKEY);
						 editor.commit();
					}
					AppPreferences.setUname(uname);
					AppPreferences.setPass(pass);
					Intent myIntent = new Intent( view.getContext(), MenuActivity.class);
					Toast.makeText( getApplicationContext(), "Log In Successful.", Toast.LENGTH_SHORT ).show();
			        startActivity(myIntent);
				} else {
					Toast.makeText( getApplicationContext(), "Log In Failed.", Toast.LENGTH_SHORT ).show();
				}
			}
		});
		alphaButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), AlphaSettingsActivity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
}
