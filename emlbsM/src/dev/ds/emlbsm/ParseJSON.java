package dev.ds.emlbsm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ParseJSON extends Activity {
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parse_json);
    new RetreiveFeedTask().execute("hh");
    
  }
} 
class RetreiveFeedTask extends AsyncTask<String, Void, String> { 

    private Exception exception;
	@Override
	protected String doInBackground(String... s) {
		StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://14.97.192.35:8080/EMLBS/loginjson?username=testuser&password=testuse");
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        Log.e(ParseJSON.class.toString(), "Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	    	this.exception=e;
	      e.printStackTrace();
	    } catch (IOException e) {
	    	this.exception=e;
	      e.printStackTrace();
	    }
	    String readTwitterFeed= builder.toString();
	    try {
	        JSONArray jsonArray = new JSONArray(readTwitterFeed);
	 
	    	Log.i(ParseJSON.class.getName(),
	            "Number of entries " + jsonArray.length());
	        for (int i = 0; i < jsonArray.length(); i++) {
	          JSONObject jsonObject = jsonArray.getJSONObject(i);
	          Log.i(ParseJSON.class.getName(), jsonObject.getString("response"));
	          System.out.println("t:"+jsonObject.getString("response"));
	        }
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    System.out.println("gooooooooooooot itttttttt");
	    return "allok";
	}

 }

