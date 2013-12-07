package dev.ds.emlbsm.util.loc;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {
	LocationUtil locationUtil;
	public MyLocationListener(LocationUtil locationUtil) {
		this.locationUtil = locationUtil;
	}
	@Override
	public void onLocationChanged(Location location) {
		// A new location update is received.  Do something useful with it.  In this case,
        // we're sending the update to a handler which then updates the UI with the new
        // location.
		Log.i(MyLocationListener.class.getName(), location.getLatitude()+","+location.getLongitude());
    	locationUtil.setMlocation(location);
    	System.out.println("updated");
	}

	@Override
	public void onProviderDisabled(String provider) {
		locationUtil.onProviderDisabled(provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		 locationUtil.onProviderEnabled(provider);
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
