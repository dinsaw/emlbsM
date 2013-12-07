package dev.ds.emlbsm.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;
import dev.ds.emlbsm.util.Useful;
import dev.ds.emlbsm.util.jsonmodels.DisplayableMarker;

public class MapActivity extends Activity{
	private GoogleMap map;
	private ProgressDialog progressDialog;
	private Handler handler;
	private Button legendButton;
	private List<DisplayableMarker> markers = new ArrayList<DisplayableMarker>();

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		addListeners();
		loadMarkers();
		// Show the Up button in the action bar.
	}

	private void addListeners() {
		legendButton = (Button) findViewById(R.id.legendbutton);
		legendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				/*AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Message")
				       .setTitle("Title");
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();*/
				Intent intent = new Intent(getApplicationContext(), LegendActivity.class);
				ArrayList<String> oTypes = new ArrayList<String>();
				ArrayList<String> colors = new ArrayList<String>();
				String[] strings;
				for (DisplayableMarker marker : markers) {
					if (!oTypes.contains(marker.getTitle())) {
						oTypes.add(marker.getTitle());
						colors.add(marker.getHexColor());
					}
				}
				intent.putStringArrayListExtra("oTypes", oTypes);
				intent.putStringArrayListExtra("colors", colors);
				startActivity(intent);
			}
		});
	}

	private void loadMarkers() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please Wait!!");
		progressDialog.setMessage("Wait!!");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
				System.out.println("handle ,messsagee");
				double centerLat = 0,centerLng = 0;
				for (DisplayableMarker marker : markers) {
					centerLat = marker.getLat();
					centerLng = marker.getLng();
					map.addMarker(new MarkerOptions()
							.position(new LatLng(marker.getLat(), marker.getLng()))
							.title(marker.getTitle())
							.icon(BitmapDescriptorFactory.defaultMarker(Useful
									.convertToHueFromHex(marker.getHexColor()))).visible(true));
				}
				Log.i(MapActivity.class.getName(), "center"+centerLat+","+centerLng);
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat, centerLng), 10));
				//map.animateCamera(CameraUpdateFactory.zoomBy(5, new Point((int)centerLat, (int)centerLng)));
				super.handleMessage(msg);
			}

		};
		progressDialog.show();
		GatherMarkersTask task = new GatherMarkersTask(this);
		/*
		 * Thread thread = new Thread(task); synchronized (thread) {
		 * thread.start(); }
		 */
		new Thread(task) {
			public void run() {
				super.run();
				handler.sendEmptyMessage(0);
			}

		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public List<DisplayableMarker> getMarkers() {
		return markers;
	}

	public void setMarkers(List<DisplayableMarker> markers) {
		this.markers = markers;
	}

}

class GatherMarkersTask extends Thread {
	private MapActivity mapActivity;
	private List<DisplayableMarker> markers = new ArrayList<DisplayableMarker>();
	public GatherMarkersTask(MapActivity mapActivity) {
		this.mapActivity = mapActivity;
	}

	@Override
	public void run() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		/*
		 * String lat = hashMap.get(NewsMenuActivity.LATKEY); String lng =
		 * hashMap.get(NewsMenuActivity.LNGKEY);
		 */
		HttpGet httpGet = new HttpGet("http://" + AppPreferences.serverip + ":"
				+ AppPreferences.serverport + AppPreferences.getMarkers);
		Log.i(MapActivity.class.getName(), "http://"
				+ AppPreferences.serverip + ":" + AppPreferences.serverport
				+ AppPreferences.getMarkers);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(MapActivity.class.toString(),
						"Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String feed = builder.toString();
		try {
			JSONObject jsonResponse = new JSONObject(new String(feed));
			JSONArray jsonArray = jsonResponse.getJSONArray("markers");
			Log.i(MapActivity.class.getName(), "Number of entries "
					+ jsonArray.length()+feed);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject markerJSON = new JSONObject(jsonArray.getString(i));
				double lat = Double.parseDouble(markerJSON.getString("lat"));
				double lng = Double.parseDouble(markerJSON.getString("lng"));
				String hexColor = markerJSON.getString("hexColor");
				String title = markerJSON.getString("title");
				DisplayableMarker marker = new DisplayableMarker(title, lat,
						lng, hexColor);
				markers.add(marker);
				Log.i(MapActivity.class.getName(), jsonArray.getString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mapActivity.setMarkers(markers);
	}

	public List<DisplayableMarker> getMarkers() {
		return markers;
	}

	public void setMarkers(List<DisplayableMarker> markers) {
		this.markers = markers;
	}

}
interface CustomTaskCallBack {
	public void onComplete(List<DisplayableMarker> markers);
}