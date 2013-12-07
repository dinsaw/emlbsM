package dev.ds.emlbsm.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.ui.CameraPreview;
import dev.ds.emlbsm.util.PhotoHandler;

public class IssueActivity extends Activity implements Camera.ErrorCallback{
	public final static String DEBUG_TAG = "MakePhotoActivity";
	  private Camera camera;
	  private CameraPreview mPreview;
	  private FrameLayout preview;
	  private int cameraId = 0;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_issue);

	    iniCamera();
	    addPreview();
	  }

	private void addPreview() {
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		mPreview = new CameraPreview(getApplicationContext(), camera);
        preview.removeAllViews();
        preview.addView(mPreview);
        System.out.println("+++++++++view added+++++++++");
	}

	private void iniCamera() {
		// do we have a camera?
	    if (!getPackageManager()
	        .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	      Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
	          .show();
	    } else {
	      cameraId = findBackFacingCamera();
	      if (cameraId < 0) {
	        Toast.makeText(this, "No Back facing camera found.",
	            Toast.LENGTH_LONG).show();
	      } else {
	        camera = Camera.open(cameraId);
	        camera.setErrorCallback(this);
	        System.out.println(this.getResources().getConfiguration().orientation);
	        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
	            camera.setDisplayOrientation(90);
	        } else {
	            camera.setDisplayOrientation(0);
	        }


	      }
	    }
	}

	  public void onClick(View view) {
	    camera.takePicture(null, null,
	        new PhotoHandler(getApplicationContext(),this));
	  }

	  private int findFrontFacingCamera() {
	    int cameraId = -1;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	        Log.d(DEBUG_TAG, "Camera found");
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }
	  private int findBackFacingCamera() {
		    int cameraId = -1;
		    // Search for the front facing camera
		    int numberOfCameras = Camera.getNumberOfCameras();
		    for (int i = 0; i < numberOfCameras; i++) {
		      CameraInfo info = new CameraInfo();
		      Camera.getCameraInfo(i, info);
		      if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
		        Log.d(DEBUG_TAG, "Camera found");
		        cameraId = i;
		        break;
		      }
		    }
		    return cameraId;
		  }
	  @Override
	  protected void onPause() {
		  super.onPause();
		  if (camera != null) {
	    	System.out.println("onDDPause");
		    //camera.lock();
	    	camera.release();
	    	camera=null;
	    }
		  //finish();
		  System.out.println("onDDfinished");
	  }
	 /* @Override
	protected void onResume() {
		  System.out.println("onDDResume");
		  //if (camera==null) {
		//}
		  try {
			camera.reconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  super.onResume();
	}*/
	  /*@Override
	protected void onResume() {
		  System.out.println("onresumeeee");
		  super.onResume();
		  try {
			camera.reconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public void onError(int error, Camera arg1) {
		if (error==Camera.CAMERA_ERROR_SERVER_DIED) {
			Log.i("OncameraError", "ala"+error);
			finish();
			startActivity(getIntent());
		}
	}
	 
}

