/**
 * 
 */
package dev.ds.emlbsm.util.jsonmodels;

/**
 * @author dinesh
 *
 */
public class DisplayableMarker {
	private String title;
	private double lat;
	private double lng;
	private String hexColor;
	
	public DisplayableMarker(String title, double lat, double lng,
			String hexColor) {
		this.title = title;
		this.lat = lat;
		this.lng = lng;
		this.hexColor = hexColor;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getHexColor() {
		return hexColor;
	}
	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
