package com.bluebird.pi;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Dialog;
import android.preference.Preference;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast; 
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import com.bluebird.pi.SunriseSunset;

public class PreferencesActivity extends CustomMethods {
	
	// CUSTOM VARIABLES
	
	// Set Time Dialog IDs
	static final int CURRENT_WAKE_ID = 1;
	static final int CURRENT_SLEEP_ID = 2;
	static final int GOAL_WAKE_ID = 3;
	static final int GOAL_SLEEP_ID = 4;
	static final int GOAL_DATE_ID = 5;
	
	// Instantiate Time Variables
	private int currentWakeHour;
	private int currentWakeMinute;
	private int currentSleepHour;
	private int currentSleepMinute;
	private int goalWakeHour;
	private int goalWakeMinute;
	private int goalSleepHour;
	private int goalSleepMinute;
	private int goalMonth;
	private int goalDay;
	private int goalYear;
	
	// Current Date
	private int mYear;
	private int mMonth;
	private int mDay;
	
	// GET DATABASE
    PiDBAdapter db = new PiDBAdapter(this);
	
	// ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        // TEST SUNRISE   
        Calendar testS = Calendar.getInstance();
        SunriseSunset sunOb = new SunriseSunset(testS, SunriseSunset.SunEnum.SUNRISE, getApplicationContext());
        Calendar testR = sunOb.getCashMoney();
        Log.d("PI", testR.toString());
        
        
        // Set Preferences Layout
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.preferencelayout);
        
     // get the current date
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH); 
        
    // Get Preferences
        Preference currentWakePref = (Preference) findPreference("currentWakePref");
        Preference currentSleepPref = (Preference) findPreference("currentSleepPref");
        Preference goalWakePref = (Preference) findPreference("goalWakePref");
        Preference goalSleepPref = (Preference) findPreference("goalSleepPref");
        
    // ADD LISTENERS   
      
        currentWakePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	 
            public boolean onPreferenceClick(Preference preference) {
            		showDialog(CURRENT_WAKE_ID);
                    return true;
            }
            
        });
        
        currentSleepPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
       	 
            public boolean onPreferenceClick(Preference preference) {
            		showDialog(CURRENT_SLEEP_ID);
                    return true;
            }
            
        }); 
        
        goalWakePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
          	 
            public boolean onPreferenceClick(Preference preference) {
            		showDialog(GOAL_WAKE_ID);
                    return true;
            }
            
        }); 
        
        goalSleepPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
          	 
            public boolean onPreferenceClick(Preference preference) {
            		showDialog(GOAL_SLEEP_ID);
                    return true;
            }
            
        });
        
        // Set Start Button
        
        final Button startButton = (Button) findViewById(R.id.startgoal);
        startButton.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
            	SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
            	
            	// Check if all preferences set
            	if(settings.contains("currentWakeHour") &&
            	   settings.contains("currentWakeMinute") &&
            	   settings.contains("currentSleepHour") &&
            	   settings.contains("currentSleepMinute") &&
            	   settings.contains("goalWakeHour") &&
            	   settings.contains("goalWakeMinute") &&
            	   settings.contains("goalSleepHour") &&
            	   settings.contains("goalSleepMinute")){
            		
            	   // Start Date Picker Dialog
            	   showDialog(GOAL_DATE_ID);
            		
            	} else {
            	   // If not all preferences are set create toast requesting user does so	
            	   Toast.makeText(PreferencesActivity.this, "Please set all preferences before continuing", Toast.LENGTH_LONG).show();
            	}
            		
            }
        });       
        
      //Grab Current Preferences and set to Time Picker Dialog
	    SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
	    currentWakeHour = settings.getInt("currentWakeHour", 12);
	    currentWakeMinute = settings.getInt("currentWakeMinute", 0);
	    currentSleepHour = settings.getInt("currentSleepHour", 12);
	    currentSleepMinute = settings.getInt("currentSleepMinute", 0);
	    goalWakeHour = settings.getInt("goalWakeHour", 12);
	    goalWakeMinute = settings.getInt("goalWakeMinute", 0);
	    goalSleepHour = settings.getInt("goalSleepHour", 12);
	    goalSleepMinute = settings.getInt("goalSleepMinute", 0);
	    goalMonth = settings.getInt("goalMonth", mMonth);
	    goalDay = settings.getInt("goalDay", mDay);
	    goalYear = settings.getInt("goalYear", mYear);
        
    // END ONCREATE
        
    }
    
    // MENU
    
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.menu_database:
        	// Create Intent and Launch Activity
        	Intent databaseIntent = new Intent(this, TempDBActivity.class);
        	startActivity(databaseIntent);
        	
            return true;
        case R.id.menu_graph:
        	// Create Intent and Launch Activity
        	Intent graphIntent = new Intent(this, GraphActivity.class);
        	startActivity(graphIntent);
        	
            return true;
            
        case R.id.menu_canvas:
        	// Create Intent and Launch Activity
        	Intent canvasIntent = new Intent(this, TempCanvasActivity.class);
        	startActivity(canvasIntent);
        	
            return true;      
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
    
    
    // ON CREATE DIALOGS
    
    @Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CURRENT_WAKE_ID:
			// set time picker as current time
			return new TimePickerDialog(this, 
                                        currentWakeTPL, currentWakeHour, currentWakeMinute,false);
		case CURRENT_SLEEP_ID:
			// set time picker as current time
			return new TimePickerDialog(this, 
                                        currentSleepTPL, currentSleepHour, currentSleepMinute,false);
		case GOAL_WAKE_ID:
			// set time picker as current time
			return new TimePickerDialog(this, 
                                        goalWakeTPL, goalWakeHour, goalWakeMinute,false);
		case GOAL_SLEEP_ID:
			// set time picker as current time
			return new TimePickerDialog(this, 
                                        goalSleepTPL, goalSleepHour, goalSleepMinute,false);
		case GOAL_DATE_ID:
			// set date picker as current preference
			return new DatePickerDialog(this,
                    goalDateDPL,
                    goalYear, goalMonth, goalDay);
		}
		return null;
	}
    
    // TIME PICKER LISTENERS
    
    private TimePickerDialog.OnTimeSetListener currentWakeTPL = 
        new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
						int selectedMinute) {
				
			// Set Time Variable to Selected Times	
			currentWakeHour = selectedHour;
			currentWakeMinute = selectedMinute;
			
			 //Load Shared Preferences and Editor
			SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			
			editor.putInt("currentWakeHour", currentWakeHour);
			editor.putInt("currentWakeMinute", currentWakeMinute);
			editor.commit();
 
		}
	};
	
	private TimePickerDialog.OnTimeSetListener currentSleepTPL = 
	        new TimePickerDialog.OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int selectedHour,
							int selectedMinute) {
				
				// Set Time Variables to Selected Time	
				currentSleepHour = selectedHour;
				currentSleepMinute = selectedMinute;
				
	          	 //Load Shared Preferences and Editor
				SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				
				editor.putInt("currentSleepHour", currentSleepHour);
				editor.putInt("currentSleepMinute", currentSleepMinute);
				
				editor.commit();

			}
		};

		private TimePickerDialog.OnTimeSetListener goalWakeTPL = 
		        new TimePickerDialog.OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int selectedHour,
								int selectedMinute) {
					
					// Set Time Variables to Selected Time
					goalWakeHour = selectedHour;
					goalWakeMinute = selectedMinute;
					
					 //Load Shared Preferences and Editor
					SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
		
					editor.putInt("goalWakeHour", goalWakeHour);
					editor.putInt("goalWakeMinute", goalWakeMinute); 
					editor.commit();
								 
				}
			};
			
		private TimePickerDialog.OnTimeSetListener goalSleepTPL = 
			        new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int selectedHour,
									int selectedMinute) {
						
						// Set Time Variables to Selected Time
						goalSleepHour = selectedHour;
						goalSleepMinute = selectedMinute;
						
						 //Load Shared Preferences and Editor
						SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						
						editor.putInt("goalSleepHour", goalSleepHour);
						editor.putInt("goalSleepMinute", goalSleepMinute);
						editor.commit();

					}
				};
				
		private DatePickerDialog.OnDateSetListener goalDateDPL =
			          new DatePickerDialog.OnDateSetListener() {
			               public void onDateSet(DatePicker view, int year, 
			                               int monthOfYear, int dayOfMonth) {
			                 
			            	 // Set Date Variables to Selected Date
							 goalDay = dayOfMonth;
							 goalMonth = monthOfYear;
							 goalYear = year;
							 
							 // Save Goal Info (End Goal at Midnight, not at final sleep)
							 Calendar cal = Calendar.getInstance();
							 Calendar cal2 = Calendar.getInstance();
							 cal2.clear();
							 cal2.set(Calendar.DAY_OF_MONTH, goalDay);
							 cal2.set(Calendar.MONTH, goalMonth);
							 cal2.set(Calendar.YEAR, goalYear);
							 
							 db.open();
							 db.insertGoal(cal.getTimeInMillis(), cal2.getTimeInMillis());
							 db.close();
									   
			            	 //Load Shared Preferences and Editor
							 SharedPreferences settings = getSharedPreferences("MyPreferences", MODE_PRIVATE);
							 SharedPreferences.Editor editor = settings.edit();
			            	  
							 // Save Preferences
							 editor.putInt("goalDay", goalDay);
							 editor.putInt("goalMonth", goalMonth);
							 editor.putInt("goalYear", goalYear);
							 editor.commit();
							 
							 // Start Goal
							 setGradient(dayOfMonth,monthOfYear,year);
							 setWakeAlarm(getNextAlarm("wake", false));
							 setSleepAlarm(getNextAlarm("sleep", false));
			            	   
			         }
			     };		
// END PREFERENCES ACTIVITY    
        
}