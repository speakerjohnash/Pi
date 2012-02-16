package com.bluebird.pi;

import android.os.Bundle;

public class ResetAlarmsActivity extends CustomMethods {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setSleepAlarm(getNextAlarm("sleep", true));
        setWakeAlarm(getNextAlarm("wake", true));
        finish();
	}
	
}
