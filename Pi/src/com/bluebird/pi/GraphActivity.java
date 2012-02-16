package com.bluebird.pi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GraphActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Get LineGraph Intent
		LineGraph line = new LineGraph();
		Intent lineIntent = line.getIntent(this);
		startActivity(lineIntent);
		
	}
	
	

}
