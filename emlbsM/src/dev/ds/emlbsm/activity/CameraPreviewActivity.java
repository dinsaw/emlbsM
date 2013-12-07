package dev.ds.emlbsm.activity;

import dev.ds.emlbsm.R;
import dev.ds.emlbsm.R.layout;
import dev.ds.emlbsm.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CameraPreviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_preview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_camera_preview, menu);
		return true;
	}

}
