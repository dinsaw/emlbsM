package dev.ds.emlbsm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.ds.emlbsm.activity.ImageSendActivity;
import dev.ds.emlbsm.activity.IssueActivity;
import dev.ds.emlbsm.util.loc.LocationUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PhotoHandler implements PictureCallback {

  private final Context context;
  private Activity activity;
  public PhotoHandler(Context context,Activity activity) {
    this.context = context;
    this.activity = activity;
  }

  @Override
  public void onPictureTaken(byte[] data, Camera camera) {
	 LocationUtil locationUtil = new LocationUtil(activity);
	 Location location = locationUtil.getMlocation();
	 Toast.makeText(context, "Location["+location.getLatitude()+","+location.getLongitude()+"]",Toast.LENGTH_LONG).show();
    /*File pictureFileDir = getDir();

    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

      Log.d(IssueActivity.DEBUG_TAG, "Can't create directory to save image.");
      Toast.makeText(context, "Can't create directory to save image.",
          Toast.LENGTH_LONG).show();
      return;

    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
    String date = dateFormat.format(new Date());
    String photoFile = "Picture_" + date + ".jpg";

    String filename = pictureFileDir.getPath() + File.separator + photoFile;

    File pictureFile = new File(filename);

    try {
      FileOutputStream fos = new FileOutputStream(pictureFile);
      fos.write(data);
      fos.close();
      Toast.makeText(context, "Location["+location.getLatitude()+","+location.getLongitude()+"]"+
      "New Image saved:" + photoFile,
          Toast.LENGTH_LONG).show();
    } catch (Exception error) {
      Log.d(IssueActivity.DEBUG_TAG, "File" + filename + "not saved: "
          + error.getMessage());
      Toast.makeText(context, "Image could not be saved.",
          Toast.LENGTH_LONG).show();
    }
*/  
	 Intent imagesendIntent = new Intent(context, ImageSendActivity.class);
	 imagesendIntent.putExtra("imagedata", data);
	 imagesendIntent.putExtra("location", location);
	 activity.startActivity(imagesendIntent);
	 activity.finish();
	 }

  private File getDir() {
    File sdDir = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    return new File(sdDir, "CameraAPIDemo");
  }
} 