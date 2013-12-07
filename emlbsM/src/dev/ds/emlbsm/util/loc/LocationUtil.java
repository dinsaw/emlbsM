/**
 * 
 */
package dev.ds.emlbsm.util.loc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;

/**
 * @author dinesh
 * 
 */
public class LocationUtil {
	LocationManager locationManager;
	LocationProvider locationProvider;
	private Activity activity;
	private Location mlocation;
	private Handler locHandler;
	MyLocationListener locationListener;
	public LocationUtil(Activity activity2) {
		this.activity = activity2;
		createLocationManager();
		createLocationProvider();
		createLocationListener();
	}

	public void createLocationManager() {
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				  .isProviderEnabled(LocationManager.GPS_PROVIDER);

				// Check if enabled and if not send user to the GSP settings
				// Better solution would be to display a dialog and suggesting to 
				// go to the settings
				if (!enabled) {
				  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				  activity.startActivity(intent);
				} 
	}

	private void createLocationProvider() {
		System.out.println("clp");
		locationProvider = locationManager
				.getProvider(LocationManager.GPS_PROVIDER);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(false);
		String providerName = locationManager.getBestProvider(criteria, true);

		// If no suitable provider is found, null is returned.
		if (providerName != null) {
		   locationProvider=(LocationProvider) activity.getSystemService(providerName);
			mlocation=locationManager.getLastKnownLocation(providerName);
		} else {
			     locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
			 	mlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
	}
	public boolean isGPSEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	public void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    activity.startActivity(settingsIntent);
	}
	public void createLocationListener() {
		locationListener = new MyLocationListener(this);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,   //**************************************
        10000,          // 10-second interval.
        0,             // 10 meters.
        locationListener);
		System.out.println("requets");
	}

	public Location getMlocation() {
		return mlocation;
	}

	public void setMlocation(Location mlocation) {
		System.out.println("set location : "+mlocation);
		this.mlocation = mlocation;
	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(activity, "Disabled provider " + provider,
		        Toast.LENGTH_SHORT).show();
		
	}

	public void onProviderEnabled(String provider) {
		Toast.makeText(activity, "Enabled new provider " + provider,
		        Toast.LENGTH_SHORT).show();
	}
	public void removeUpdates() {
		locationManager.removeUpdates(locationListener);
	}
}
