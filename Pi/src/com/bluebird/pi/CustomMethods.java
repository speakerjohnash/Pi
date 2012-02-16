package com.bluebird.pi;
import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import java.lang.Math;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;


public class CustomMethods extends PreferenceActivity {

	// CUSTOM METHODS
	
		// Get next calendar object for next available alarm
		protected Calendar getNextAlarm(String wakeOrSleep, Boolean onReboot){
			
			// Create Variables
					SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
					int gradient = 0;
					int mHour = 12;
					int mMin = 0;
			
			// If Wake or Sleep, Initialize Variables
					if(wakeOrSleep.equals("wake")){
						mHour = settings.getInt("currentWakeHour",12);
						mMin = settings.getInt("currentWakeMinute",0);
						gradient = settings.getInt("wakeGradient",0);
					}
					else if(wakeOrSleep.equals("sleep")) {
						mHour = settings.getInt("currentSleepHour",12);
						mMin = settings.getInt("currentSleepMinute",0);
						gradient = settings.getInt("sleepGradient",0);
					}		
					
					Log.d("stringValue",wakeOrSleep);
					
			// CODE TO RETURN CALENDAR
			
			Calendar cal  = Calendar.getInstance();
			Calendar nextAlarm  = Calendar.getInstance();
			nextAlarm.set(Calendar.HOUR_OF_DAY,mHour);
			nextAlarm.set(Calendar.MINUTE,mMin);
			nextAlarm.set(Calendar.SECOND,00);
			
			// Check if Alarm is in past, and if so grab alarm from tomorrow
			
			if(nextAlarm.after(cal)){
			// Check for gradient change and Subtract Differences
				if (onReboot == false){
					nextAlarm.add(Calendar.MINUTE, gradient);
				}
			return nextAlarm;
			}else{
			cal.add(Calendar.DATE,1);
			cal.set(Calendar.HOUR_OF_DAY,mHour);
			cal.set(Calendar.MINUTE,mMin);
			cal.set(Calendar.SECOND,00);
			
			// Check for Gradient change and Subtract Difference
			if (onReboot == false){
				cal.add(Calendar.MINUTE, gradient);
			}
			
			return cal;
			}	
		
		// END GETNEXTALARM	
			
		}
		
		// Set Wake Time
		
		protected void setWakeAlarm(Calendar cal){
			Log.d("calinsetwake", cal.toString());
			//Load Shared Preferences and Editor
		    SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
		    SharedPreferences.Editor editor = settings.edit();
		    
			Intent intent = new Intent(getApplicationContext(), WakeAlarm.class);
			PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(), 111, intent, 0);
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
			
			// Alert Next Alarm
			Toast.makeText(this, "Next wake alarm set for " + DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.getTime()) ,
					Toast.LENGTH_LONG).show();
			
			// Reset Preferences
			editor.putInt("currentWakeHour", cal.get(Calendar.HOUR_OF_DAY)); 
			editor.putInt("currentWakeMinute", cal.get(Calendar.MINUTE)); 
			editor.commit();		
		
		}
		
		// Set Sleep Time
		
		protected void setSleepAlarm(Calendar cal){
			Log.d("calinsetsleep", cal.toString());
			
			//Load Shared Preferences and Editor
		    SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
		    SharedPreferences.Editor editor = settings.edit();

			Intent intent = new Intent(getApplicationContext(), SleepAlarm.class);
			PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(), 222, intent, 0);
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
			
			// Alert Next Alarm
					Toast.makeText(this, "Next sleep alarm set for " + DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.getTime()) ,
							Toast.LENGTH_LONG).show();
			
			// Reset Preferences
					editor.putInt("currentSleepHour", cal.get(Calendar.HOUR_OF_DAY)); 
					editor.putInt("currentSleepMinute", cal.get(Calendar.MINUTE)); 
					editor.commit();
		}
		
		// Set Gradient 
		
		protected void setGradient(int goalDay, int goalMonth, int goalYear){
			
			// Initialize Gradient Variable
			int wakeGradient = 0;
			int sleepGradient = 0;
			int millisInDay = 1000 * 60 * 60 * 24;
			
	// Get Calendar Objects for current and future sleep schedule
			
		    Calendar today  = Calendar.getInstance();
		    Calendar goalDate = Calendar.getInstance();
			Calendar beginWake  = Calendar.getInstance();
			Calendar beginSleep  = Calendar.getInstance();
			Calendar endWake = Calendar.getInstance();
			Calendar endSleep  = Calendar.getInstance();
			
			// Initialize Current Settings so it can be called from other classes;
			SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
		    int currentWakeHour = settings.getInt("currentWakeHour", 12);
		    int currentWakeMinute = settings.getInt("currentWakeMinute", 0);
		    int currentSleepHour = settings.getInt("currentSleepHour", 12);
		    int currentSleepMinute = settings.getInt("currentSleepMinute", 0);
		    int goalWakeHour = settings.getInt("goalWakeHour", 12);
		    int goalWakeMinute = settings.getInt("goalWakeMinute", 0);
		    int goalSleepHour = settings.getInt("goalSleepHour", 12);
		    int goalSleepMinute = settings.getInt("goalSleepMinute", 0);
			
			// Set Calendar Values
			beginWake.set(Calendar.HOUR_OF_DAY,currentWakeHour);
			beginWake.set(Calendar.MINUTE,currentWakeMinute);
			beginWake.set(Calendar.SECOND,00);
			
			beginSleep.set(Calendar.HOUR_OF_DAY,currentSleepHour);
			beginSleep.set(Calendar.MINUTE,currentSleepMinute);
			beginSleep.set(Calendar.SECOND,00);
			
			endWake.set(Calendar.HOUR_OF_DAY,goalWakeHour);
			endWake.set(Calendar.MINUTE,goalWakeMinute);
			endWake.set(Calendar.SECOND,00);
			
			endSleep.set(Calendar.HOUR_OF_DAY,goalSleepHour);
			endSleep.set(Calendar.MINUTE,goalSleepMinute);
			endSleep.set(Calendar.SECOND,00);
			
			goalDate.set(Calendar.DAY_OF_MONTH, goalDay);
			goalDate.set(Calendar.MONTH, goalMonth);
			goalDate.set(Calendar.YEAR, goalYear);
			
			// Calculate Gradients
			// Wake
			long wakeDiff = endWake.getTimeInMillis() - beginWake.getTimeInMillis(); 
			wakeDiff = Math.abs(wakeDiff);
			if (wakeDiff>11){
				wakeDiff = wakeDiff * -1;
			}
			long dayDiff = (goalDate.getTimeInMillis() - today.getTimeInMillis()) / millisInDay;
			long longWakeGradient = wakeDiff/ dayDiff; 
			longWakeGradient = (long) ((longWakeGradient / (1000*60)) % 60);
			wakeGradient = (int) longWakeGradient;
			
			//Sleep 
			long sleepDiff = endSleep.getTimeInMillis() - beginSleep.getTimeInMillis();
			sleepDiff = Math.abs(wakeDiff);
			if (sleepDiff>11){
				sleepDiff = sleepDiff * -1;
			}
			long longSleepGradient = sleepDiff/ dayDiff; 
			longSleepGradient = (long) ((longSleepGradient / (1000*60)) % 60);
			sleepGradient = (int) longSleepGradient;
			
			Log.d("wakeGradient", Integer.toString(wakeGradient));
			Log.d("sleepGradient", Integer.toString(sleepGradient));
			
			
			// Save Gradient Preference
			editor.putInt("wakeGradient", wakeGradient);
	        editor.putInt("sleepGradient", sleepGradient);
	        editor.commit();
			
		}
	
}
