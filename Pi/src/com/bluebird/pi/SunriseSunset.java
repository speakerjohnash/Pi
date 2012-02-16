/*
 * LOOK AT THIS TO SEE WHY WE DO THINGS
 * http://williams.best.vwh.net/sunrise_sunset_algorithm.htm
 */

package com.bluebird.pi;

import java.util.Calendar;
import java.lang.Math;
import java.util.TimeZone;

import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.content.Context;
import android.util.Log;

public class SunriseSunset {
	
	private Calendar cal;
	private SunEnum value;
	private double zenith = 90.83;
	private int dayOfYear;
	private Context ctx;
	private double latitude;
	private double longitude;
	private String provider;
	private boolean britishEmpire = false;
	private Calendar cashMoney;
	
	private double three60ify(double number)
	{
		if (number > 360){
			number -= 360;
		}
		else if (number < 0){
			number += 360;
		}
		return number;
	}
	
	public enum SunEnum {
		SUNRISE, SUNSET
	}

	// Constructor
	public SunriseSunset(Calendar cal, SunEnum value, Context ctx) {
		
		this.cal = cal;
		this.value = value;
		this.ctx = ctx;
		
		// Get Location Object
		LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
		// Get Latitude / Longitude
		//latitude = location.getLatitude();
		//longitude = location.getLongitude();
		
		latitude = 38.4405556;
		longitude = -122.7133333;
		
		// GET HOUR ANGLE
		double hourAngle = longitude / 15;
		
		// Set base time of rise/set
		dayOfYear = cal.DAY_OF_YEAR;
		int defaultTime;
		
		switch (value) {
			case SUNRISE:
				defaultTime = 6;
				break;
			case SUNSET:
				defaultTime = 18;
				break;
			default:
				defaultTime = 6; // stupid
		}
		double baseT = (double) dayOfYear + ( ( defaultTime - hourAngle ) / 24 );
		
		Log.d("Base Time", Double.toString(baseT));
		
		// Now we will calculate the mean anomaly of the sun /geek
		double mean = ( 0.9856 * baseT ) - 3.289; // what the heck do these numbers come from
		
		Log.d("Mean Anom", Double.toString(mean));
		
		// Calculate true longitude of the sun
		double trueLon = mean + (1.916 * Math.sin(Math.toRadians(mean))) + (0.02 * Math.sin(Math.toRadians(2 * mean))) + 282.634; // honestly WTF
		
		// Sometimes L is outside the range of 0 - 360 degrese.
		trueLon = three60ify(trueLon);
		
		Log.d("True Long", Double.toString(trueLon));
		
		// Now we calculate Right Ascension
		double rightAsc = 0.91764 * Math.tan(Math.toRadians(trueLon));
		rightAsc = Math.toDegrees(Math.atan(rightAsc)); 
		
		Log.d("Right Ascension", Double.toString(rightAsc));
		
		// Move Right Ascension to correct quadrant
		double lQuad = (Math.floor(trueLon / 90) * 90);
		double raQuad = (Math.floor(rightAsc / 90) * 90);
		rightAsc = rightAsc + (lQuad - raQuad); // rawquads
		
		// Convert right ascension to hours
		rightAsc = rightAsc / 15;
		
		Log.d("Right Ascension in Hours", Double.toString(rightAsc));
		
		// Find sun's declination
		double sinDec = 0.39782 * Math.sin(Math.toRadians(trueLon));
		double cosDec = Math.asin(sinDec);
		cosDec = Math.cos(cosDec);
		
		Log.d("SinDec", Double.toString(sinDec));
		Log.d("CosDec", Double.toString(cosDec));
		
		// Calculate the sun's hour angle
		double cosh = Math.cos(Math.toRadians(zenith));
		cosh = cosh - (sinDec * (Math.sin(Math.toRadians(latitude))));
		cosh = cosh / (cosDec * Math.cos(Math.toRadians(latitude)));
				
		Log.d("Local Hour Angle", Double.toString(cosh));
		
		// If the value of cosh is outside of -1 .. 1, the sun will not rise or set
		// You are in the arctic circle.
		if(cosh > 1 || cosh < -1) {
			britishEmpire = true;
		}
		
		double H;
		
		switch(value) {
			case SUNRISE:
				H = 360 - Math.toDegrees(Math.acos(cosh));
				break;
			case SUNSET:
				H = Math.toDegrees(Math.acos(cosh));
				break;
			default:
				H = 360 - Math.toDegrees(Math.acos(cosh));
				// default is dumb.
		}
		
		Log.d("H", Double.toString(H));
		
		// now we convert hours to hours
		H = H / 15;
		
		Log.d("H in Hours", Double.toString(H));
		
		// Im just gonna go for it
		double time = H + rightAsc - (0.06571 * baseT) - 6.622;
		
		Log.d("Rise in Hours", Double.toString(time));
		
		// Universal Time
		double UT = time - hourAngle;
		
		if (UT < 0) {
			UT += 24;
		}
		else if (UT > 24) {
			UT -= 24;
		}
		
		Log.d("Time in UTC", Double.toString(UT));
		
		// Convert To Local Time
		TimeZone tZone = cal.getTimeZone();
		double offset = (double) tZone.getRawOffset()/ 3600000;		
		UT += offset;
		Log.d("offset",Double.toString(offset));
		Log.d("realtime",Double.toString(UT));
		
		// Set CashMoney
		cashMoney = Calendar.getInstance();
		cashMoney.clear();
		double UTMins = (double) (UT % 1) * 60; 
		double UTHours = (double) UT / 1;
		cashMoney.set(Calendar.HOUR_OF_DAY, (int) UTHours);
		cashMoney.set(Calendar.MINUTE, (int) UTMins);
		
	}
	
	public Calendar getCashMoney() {
		return cashMoney;
	}
	
	
}
