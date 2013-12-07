/**
 * 
 */
package dev.ds.emlbsm.util.auth;

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

import dev.ds.emlbsm.util.AppPreferences;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author dinesh
 * 
 */
public class JSONAuthStrategy implements AuthStrategy {
	public static final String authsuccess = "AUTHSUCCESS";
	public static final String authfailure = "AUTHFAILURE";
	public static final String UNAMEKEY = "UNAME";
	public static final String PASSKEY = "PASS";

	@Override
	public boolean authenticate(String uname, String pass) {
		System.out.println("aaaaaaaaaaaaaaa");
		HashMap<String, String> hashMap = new HashMap<String, String>(2);
		hashMap.put(UNAMEKEY, uname);
		hashMap.put(PASSKEY, pass);
		AsyncTask<HashMap<String, String>, Void, String> result = new RetreiveFeedTask()
				.execute(hashMap);
		String res = null;
		try {
			res = result.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (res != null) {
			if (res.equals(authsuccess)) {
				return true;
			}
		}
		return false;
	}

}

class RetreiveFeedTask extends AsyncTask<HashMap<String, String>, Void, String> {

	private Exception exception;
	private String authresponse;

	@Override
	protected String doInBackground(HashMap<String, String>... hashMaps) {
		HashMap<String, String> hashMap = hashMaps[0];
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String uname = hashMap.get(JSONAuthStrategy.UNAMEKEY);
		String pass = hashMap.get(JSONAuthStrategy.PASSKEY);
		HttpGet httpGet = new HttpGet("http://" + AppPreferences.serverip+":"
				+ AppPreferences.serverport + AppPreferences.loginURL
				+ "?username=" + uname + "&password=" + pass);
		Log.i(JSONAuthStrategy.class.getName(), "http://" + AppPreferences.serverip+":"
				+ AppPreferences.serverport + AppPreferences.loginURL
				+ "?username=" + uname + "&password=" + pass);
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
			this.exception = e;
			e.printStackTrace();
		} catch (IOException e) {
			this.exception = e;
			e.printStackTrace();
		}
		String authfeed = builder.toString();
		try {
			JSONObject object = new JSONObject(authfeed);
			Log.i(JSONAuthStrategy.class.getName(), "Response : " + authfeed);
			authresponse = object.getString("response");
		} catch (Exception e) {
			exception = e;
			e.printStackTrace();
		}
		return authresponse;
	}
}
