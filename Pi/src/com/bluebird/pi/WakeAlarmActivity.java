package com.bluebird.pi;

import java.io.IOException;
import java.util.Calendar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;

public class WakeAlarmActivity extends CustomMethods {
	
	// Create Database Object
	PiDBAdapter db = new PiDBAdapter(this);

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    // Get URI
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
            }
        }    
	
	// Set Off Repeating Vibration
			final Vibrator vibrator = (Vibrator) WakeAlarmActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
			 
			long[] pattern = { 0, 200, 500, 350, 400, 1000, 200, 300, 500 };
			 
			vibrator.vibrate(pattern, 0);
			
	// Set Off Music		
			final MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mediaPlayer.setLooping(true);
			mediaPlayer.setVolume(100, 100);
			try {
				mediaPlayer.setDataSource(getApplicationContext(), alert);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();	
	
	// Start Dialog

    AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

    // set the message to display
    alertbox.setMessage("Wake the fuck up");

    // add a neutral button to the alert box and assign a click listener
    alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

        // click listener on the alert box
        public void onClick(DialogInterface dialog, int which) {
        	// Get Current Time
        	Calendar cal = Calendar.getInstance();
        	long time = (long) cal.getTimeInMillis();
        	
        	// Save Wake Time to Database
        	db.open();
        	db.insertWake(time);
        	db.close();
        	
            // the button was clicked
        	mediaPlayer.release();
            vibrator.cancel();
            setWakeAlarm(getNextAlarm("wake", false));
            finish();
        }
    });

    // Show Alert Box
    alertbox.show();
	
    
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        
        // Make Sure Vibrator Cancels
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.cancel();
   
	}
	
}
