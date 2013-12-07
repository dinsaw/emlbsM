package dev.ds.emlbsm.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;
import dev.ds.emlbsm.util.Useful;
import dev.ds.emlbsm.util.auth.JSONAuthStrategy;

public class NewsActivity extends Activity {
	String newsTitle,news;
	TextView newsTitleView,newsView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		Bundle bundle = getIntent().getExtras();
		newsTitle = bundle.getString(NewsMenuActivity.NEWSTITLEKEY);
		newsTitleView=(TextView) findViewById(R.id.newsTitle);
		newsTitleView.setText(newsTitle);
		prepareNews();
	}

	private void prepareNews() {
		newsView = (TextView) findViewById(R.id.news);
		HashMap<String, String> hashMap = new HashMap<String, String>(1);
		hashMap.put(NewsMenuActivity.NEWSTITLEKEY, newsTitle);
		AsyncTask<HashMap<String, String>, Void, String> task = new RetreiveNewsTask(this);
		task.execute(hashMap);
		String res = null;
		try {
			res = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null) {
			news=res;
			newsView.setText(news);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_news, menu);
		return true;
	}
	class RetreiveNewsTask extends AsyncTask<HashMap<String, String>, Void, String> {
		Activity activity;
		private ProgressDialog dialog;
		
		private Exception exception;
		private String news;

		public RetreiveNewsTask(Activity activity) {
			this.activity = activity;
			dialog = new ProgressDialog(activity);
		}
		@Override
		protected void onPreExecute() {
			dialog.setTitle("Please wait");
			dialog.setMessage("Loading News");
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			super.onPostExecute(result);
		}
		@Override
		protected String doInBackground(HashMap<String, String>... hashMaps) {
			HashMap<String, String> hashMap = hashMaps[0];
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			String newsTitle = hashMap.get(NewsMenuActivity.NEWSTITLEKEY);
			HttpGet httpGet = new HttpGet("http://" + AppPreferences.serverip+":"
					+ AppPreferences.serverport + AppPreferences.getNewsByTitleURL
					+ "?newsTitle="+Useful.createStringForURL(newsTitle));
			Log.i(NewsActivity.class.getName(), "http://" + AppPreferences.serverip+":"
					+ AppPreferences.serverport + AppPreferences.getNewsByTitleURL
					+ "?newsTitle="+Useful.createStringForURL(newsTitle));
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
					Log.e(NewsActivity.class.toString(),
							"Failed to download file");
				}
			} catch (ClientProtocolException e) {
				this.exception = e;
				e.printStackTrace();
			} catch (IOException e) {
				this.exception = e;
				e.printStackTrace();
			}
			String feed = builder.toString();
			try {
				JSONObject object = new JSONObject(feed);
				Log.i(NewsActivity.class.getName(), "news : " + feed);
				news = object.getString("news");
			} catch (Exception e) {
				exception = e;
				e.printStackTrace();
			}
			return news;
		}
	}
}
