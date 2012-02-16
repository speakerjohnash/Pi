package com.bluebird.pi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WakeAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		
		Intent i = new Intent(context, WakeAlarmActivity.class);  
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    context.startActivity(i);
	    
	}

}
