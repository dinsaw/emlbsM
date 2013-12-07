/**
 * 
 */
package dev.ds.emlbsm.util;

import android.graphics.Color;

/**
 * @author dinesh
 *
 */
public class Useful {
	public static String createStringForURL(String orgString) {
		return orgString.replaceAll(" ", "%20");
	}
	
	/**
	 * @param hex #FFAAFF
	 * @return
	 */
	public static int convertToHueFromHex(String hex) {
		int r,g,b;
		r = Integer.parseInt(hex.substring(1, 3), 16);
		g = Integer.parseInt(hex.substring(3, 5), 16);
		b = Integer.parseInt(hex.substring(5, 7), 16);
		System.out.println("RGB"+r+g+b);
		float[] HSV = new float[3];
		Color.RGBToHSV(r, g, b, HSV);
		int hue = (int) HSV[0];
		System.out.println("Hue"+HSV[0]);
		return hue;
	}
}
