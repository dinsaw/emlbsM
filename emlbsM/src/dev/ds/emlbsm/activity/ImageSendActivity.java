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
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import dev.ds.emlbsm.R;
import dev.ds.emlbsm.util.AppPreferences;
import dev.ds.emlbsm.util.auth.JSONAuthStrategy;
import dev.ds.emlbsm.util.base.Base64;

public class ImageSendActivity extends Activity {
	byte imagedata[];
	Button goMenuButton, sendButton;
	Location location;
	Spinner spinner;
	EditText descEditText;
	private Handler handler;
	private ProgressDialog progressDialog;
	public static final String IMAGESTRINGKEY = "IMAGESTRING";
	public static final String LATKEY = "LATSTRING";
	public static final String LANGKEY = "LANGSTRING";
	public static final String SELECTEDCTKEY = "SELECTEDCT";
	public static final String DESCKEY = "DESC";
	public static final String ISSUEREPLY = "Issued";
	public static final String FAILREPLY = "Failed";
	InputStream is;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_send);

		Bundle bundle = getIntent().getExtras();
		imagedata = bundle.getByteArray("imagedata");
		location = (Location) bundle.get("location");
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		Bitmap bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
				imagedata.length);
		imageView.setImageBitmap(bitmap);
		descEditText = (EditText) findViewById(R.id.editText1);
		prepareSpinner();
		addListenerOnButton();

	}

	private void prepareSpinner() {
		spinner = (Spinner) findViewById(R.id.spinner1);
		AsyncTask<Void, Void, List<String>> asyncTask = new RetreiveOverlayFeedTask()
				.execute();
		List<String> res = null;
		try {
			res = asyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (res != null) {
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, res);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(dataAdapter);
		}
	}

	private void addListenerOnButton() {
		goMenuButton = (Button) findViewById(R.id.button2);
		sendButton = (Button) findViewById(R.id.button1);
		goMenuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(view.getContext(), MenuActivity.class));
			}
		});
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Toast.makeText(
						getApplicationContext(),
						AppPreferences.getUname() + "|"
								+ descEditText.getText().toString(),
						Toast.LENGTH_LONG).show();
				byte[] ba = imagedata;
				String baString = Base64.encodeBytes(ba);
				HashMap<String, String> hashMap = new HashMap<String, String>(2);
				hashMap.put(AppPreferences.UNAMEKEY, AppPreferences.getUname());
				hashMap.put(AppPreferences.PASSKEY, AppPreferences.getPass());
				hashMap.put(ImageSendActivity.IMAGESTRINGKEY, baString);
				hashMap.put(ImageSendActivity.LATKEY, location.getLatitude()
						+ "");
				hashMap.put(ImageSendActivity.LANGKEY, location.getLongitude()
						+ "");
				hashMap.put(ImageSendActivity.SELECTEDCTKEY, spinner
						.getSelectedItem().toString());
				hashMap.put(ImageSendActivity.DESCKEY, descEditText.getText()
						.toString());
				/*@SuppressWarnings("unchecked")
				AsyncTask<HashMap<String, String>, Void, String> result = new RetreiveFeedTask()
						.execute(hashMap);
				String res = null;
				try {
					res = result.get();
				} catch (InterruptedException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_LONG).show();
				} catch (ExecutionException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_LONG).show();
				}*/
				 progressDialog = new ProgressDialog(view.getContext());
			        progressDialog.setTitle("Please Wait!!");
			        progressDialog.setMessage("Wait!!");
			        progressDialog.setCancelable(false);
			        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			        final View viewfinal = view;
			        handler = new Handler()
			        {

			            @Override
			            public void handleMessage(Message msg)
			            {
			                progressDialog.dismiss();
			                Intent mainIntent = new Intent(viewfinal.getContext(), MenuActivity.class);
			                startActivity(mainIntent);
			                super.handleMessage(msg);
			            }

			        };
			        progressDialog.show();
				IssueRequestTask task = new IssueRequestTask(hashMap);
				/*Thread thread = new Thread(task);
				synchronized (thread) {
					thread.start();
				}*/
				new Thread(task)
		        {
		            public void run()
		            {
		                super.run();
		                handler.sendEmptyMessage(0);
		            }

		        }.start();
				String res = task.getReply();
				System.out.println("res:"+res);
				if (res.equals(ImageSendActivity.ISSUEREPLY)) {
					Toast.makeText(getApplicationContext(),
							"Complaint Issued.", Toast.LENGTH_LONG).show();
				} else if (res.equals(ImageSendActivity.FAILREPLY)) {
					Toast.makeText(getApplicationContext(),
							"Complaint not Issuied.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_image_send, menu);
		return true;
	}

}

class RetreiveFeedTask extends AsyncTask<HashMap<String, String>, Void, String> {

	@Override
	protected String doInBackground(HashMap<String, String>... maps) {
		String authresponse = null;
		StringBuilder builder = new StringBuilder();
		HashMap<String, String> hashMap = maps[0];
		String uname = hashMap.get(AppPreferences.UNAMEKEY);
		String pass = hashMap.get(AppPreferences.PASSKEY);
		String ba1 = hashMap.get(ImageSendActivity.IMAGESTRINGKEY);
		String lat = hashMap.get(ImageSendActivity.LATKEY);
		String lang = hashMap.get(ImageSendActivity.LANGKEY);
		String complaintType = hashMap.get(ImageSendActivity.SELECTEDCTKEY);
		String desc = hashMap.get(ImageSendActivity.DESCKEY);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Log.i("username", "toto" + uname + pass);
		nameValuePairs.add(new BasicNameValuePair("image", ba1));
		nameValuePairs.add(new BasicNameValuePair("username", uname));
		nameValuePairs.add(new BasicNameValuePair("password", pass));
		nameValuePairs.add(new BasicNameValuePair("latitude", lat));
		nameValuePairs.add(new BasicNameValuePair("longitude", lang));
		nameValuePairs.add(new BasicNameValuePair("useroverlaytype",
				complaintType));
		nameValuePairs.add(new BasicNameValuePair("description", desc));
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String url = "http://" + AppPreferences.serverip + ":"
					+ AppPreferences.serverport
					+ AppPreferences.issueRequestURL;
			HttpPost httppost = new HttpPost(url);
			Log.i(ImageSendActivity.class.getName(), url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
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
				Log.e(ImageSendActivity.class.toString(),
						"Failed to download file");
			}
		} catch (Exception e) {
			Log.e(ImageSendActivity.class.getName(),
					"Error in http connection " + e.toString());
		}
		String authfeed = builder.toString();
		try {
			JSONObject object = new JSONObject(authfeed);
			Log.i(ImageSendActivity.class.getName(), "Response : " + authfeed);
			authresponse = object.getString("reply");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authresponse;
	}
}

class IssueRequestTask implements Runnable {
	HashMap<String, String> map;
	private String reply = "";
	public IssueRequestTask(HashMap<String, String> map) {
		this.map=map;
	}
	@Override
	public void run() {
		String authresponse = null;
		StringBuilder builder = new StringBuilder();
		HashMap<String, String> hashMap = map;
		String uname = hashMap.get(AppPreferences.UNAMEKEY);
		String pass = hashMap.get(AppPreferences.PASSKEY);
		String ba1 = hashMap.get(ImageSendActivity.IMAGESTRINGKEY);
		String lat = hashMap.get(ImageSendActivity.LATKEY);
		String lang = hashMap.get(ImageSendActivity.LANGKEY);
		String complaintType = hashMap.get(ImageSendActivity.SELECTEDCTKEY);
		String desc = hashMap.get(ImageSendActivity.DESCKEY);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Log.i("username", "toto" + uname + pass);
		nameValuePairs.add(new BasicNameValuePair("image", ba1));
		nameValuePairs.add(new BasicNameValuePair("username", uname));
		nameValuePairs.add(new BasicNameValuePair("password", pass));
		nameValuePairs.add(new BasicNameValuePair("latitude", lat));
		nameValuePairs.add(new BasicNameValuePair("longitude", lang));
		nameValuePairs.add(new BasicNameValuePair("useroverlaytype",
				complaintType));
		nameValuePairs.add(new BasicNameValuePair("description", desc));
		try {
			HttpClient httpclient = new DefaultHttpClient();
			String url = "http://" + AppPreferences.serverip + ":"
					+ AppPreferences.serverport
					+ AppPreferences.issueRequestURL;
			HttpPost httppost = new HttpPost(url);
			Log.i(ImageSendActivity.class.getName(), url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
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
				Log.e(ImageSendActivity.class.toString(),
						"Failed to download file");
			}
		} catch (Exception e) {
			Log.e(ImageSendActivity.class.getName(),
					"Error in http connection " + e.toString());
		}
		String authfeed = builder.toString();
		try {
			JSONObject object = new JSONObject(authfeed);
			Log.i(ImageSendActivity.class.getName(), "Response : " + authfeed);
			authresponse = object.getString("reply");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setReply(authresponse);
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
}

class RetreiveOverlayFeedTask extends AsyncTask<Void, Void, List<String>> {

	@Override
	protected List<String> doInBackground(Void... voids) {
		List<String> overlayTypes = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://" + AppPreferences.serverip + ":"
				+ AppPreferences.serverport + AppPreferences.getOverlayTypeURL);
		Log.i(ImageSendActivity.class.getName(), "http://"
				+ AppPreferences.serverip + ":" + AppPreferences.serverport
				+ AppPreferences.getOverlayTypeURL);
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
				Log.e(JSONAuthStrategy.class.toString(),
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
			JSONArray jsonArray = jsonResponse.getJSONArray("userOverlayTypes");
			Log.i(ImageSendActivity.class.getName(), "Number of entries "
					+ jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				overlayTypes.add(jsonArray.getString(i));
				Log.i(ImageSendActivity.class.getName(), jsonArray.getString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return overlayTypes;
	}
}