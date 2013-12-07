package dev.ds.emlbsm.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;
import dev.ds.emlbsm.util.auth.JSONAuthStrategy;
import dev.ds.emlbsm.util.loc.LocationUtil;
import dev.ds.emlbsm.util.tasks.MenuForIntent;
import dev.ds.emlbsm.util.tasks.MenuItemIntent;

public class NewsMenuActivity extends ListActivity {
	public static final String LATKEY="lat";
	public static final String LNGKEY="lng";
	public static final String NEWSTITLEKEY="newsTitle";
	MenuForIntent menuForIntent;
	List<String> newstitleList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_menu);
		createMenu();
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menuForIntent.getAllDisplayNames()));
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		startActivity(menuForIntent.getIntentByItemPos(position, v));
	}
	private void createMenu() {
		AsyncTask<HashMap<String, String>, String, List<String>> task = new GatherNewsTitlesTask(this);
		LocationUtil locationUtil = new LocationUtil(this);
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put(LATKEY, locationUtil.getMlocation().getLatitude()+"");
		hashMap.put(LNGKEY, locationUtil.getMlocation().getLongitude()+"");
		Toast.makeText(getApplicationContext(), hashMap.get(LATKEY)+","+hashMap.get(LNGKEY), Toast.LENGTH_SHORT).show();
		task.execute(hashMap);
		List<String> res = null;
		try {
			res = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (res!=null) {
			newstitleList=res;
		}
		menuForIntent = new MenuForIntent();
		if (newstitleList.size()==0) {
			Toast.makeText(getApplicationContext(), "No news for this Location", Toast.LENGTH_LONG).show();
		} else {
			
			for (String newstitle : newstitleList) {
				Intent intent = new Intent(getApplicationContext(),
						NewsActivity.class);
				intent.putExtra(NEWSTITLEKEY, newstitle);
				menuForIntent.addMenuItemIntent(new MenuItemIntent(intent, newstitle));
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_news_menu, menu);
		return true;
	}

}
class GatherNewsTitlesTask extends AsyncTask<HashMap<String, String>, String, List<String>> {
	Activity activity;
	private ProgressDialog dialog;
	public GatherNewsTitlesTask(Activity activity) {
		this.activity = activity;
		dialog = new ProgressDialog(activity);
	}
	@Override
	protected void onPreExecute() {
		dialog.setTitle("Please wait");
		dialog.setTitle("Loading News Menu ...");
		dialog.show();
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(List<String> result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onPostExecute(result);
	}
	@Override
	protected List<String> doInBackground(HashMap<String, String>... hashMaps) {
		List<String> newsTitles = new ArrayList<String>();
		HashMap<String, String> hashMap = hashMaps[0];
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String lat = hashMap.get(NewsMenuActivity.LATKEY);
		String lng = hashMap.get(NewsMenuActivity.LNGKEY);
		HttpGet httpGet = new HttpGet("http://" + AppPreferences.serverip+":"
				+ AppPreferences.serverport + AppPreferences.getNewsTitlesURL
				+ "?x=" + lat + "&y=" + lng);
		Log.i(NewsMenuActivity.class.getName(),"http://" + AppPreferences.serverip+":"
				+ AppPreferences.serverport + AppPreferences.getNewsTitlesURL
				+ "?x=" + lat + "&y=" + lng);
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
				Log.e(NewsMenuActivity.class.toString(),
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
			JSONArray jsonArray = jsonResponse.getJSONArray("newsTitleList");
			Log.i(NewsMenuActivity.class.getName(), "Number of entries "
					+ jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				newsTitles.add(jsonArray.getString(i));
				Log.i(NewsMenuActivity.class.getName(), jsonArray.getString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsTitles;
	}
	
}
