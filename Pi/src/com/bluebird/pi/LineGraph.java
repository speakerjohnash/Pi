package com.bluebird.pi;

import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

public class LineGraph {
	
	private Cursor wakeCursor;
	private Cursor sleepCursor;
	private Calendar cal;
	private XYMultipleSeriesDataset dataset;
	
	public Intent getIntent(Context context){
		
		// Create Calendar Instance and Clear
		cal = Calendar.getInstance();
		cal.clear();
		
		// Open Database and get Cursors
		PiDBAdapter db = new PiDBAdapter(context);
		db.open();
		wakeCursor = db.getAllWake();
		sleepCursor = db.getAllSleep();
		
		// Create Series Objects
		XYSeries wakeSeries = new XYSeries("realWake");
		XYSeries sleepSeries = new XYSeries("realSleep");
		
		// Load Series with Database Data
		while (wakeCursor.isAfterLast() == false) {
			
			// Make Sure Field Has Content
			if (wakeCursor.isNull(1) != true){
				cal.setTimeInMillis(wakeCursor.getLong(1));
				wakeSeries.add(wakeCursor.getDouble(0), cal.get(Calendar.HOUR_OF_DAY));
			}
			else {
				wakeSeries.add(wakeCursor.getDouble(0), (double) 0);
			}
		
			wakeCursor.moveToNext();
		}
		
		while (sleepCursor.isAfterLast() == false) {
			
			// Make Sure Field Has Content
			if (sleepCursor.isNull(1) != true){
				cal.setTimeInMillis(sleepCursor.getLong(1));
				sleepSeries.add(sleepCursor.getDouble(0), cal.get(Calendar.HOUR_OF_DAY));
			}
			else {
				sleepSeries.add(sleepCursor.getDouble(0), (double) 0);
			}
		
			sleepCursor.moveToNext();
		}
		
		// Add Series to Dataset
		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(wakeSeries);
		dataset.addSeries(sleepSeries);
		
		// Create Line Renderers 
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		
		XYSeriesRenderer sleepRenderer = new XYSeriesRenderer();
		XYSeriesRenderer wakeRenderer = new XYSeriesRenderer();
		
		// Set Styles
		sleepRenderer.setColor(Color.GREEN);
		
		mRenderer.addSeriesRenderer(sleepRenderer);
		mRenderer.addSeriesRenderer(wakeRenderer);
		
		// Create and Return Intent/ Close Database
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, "Wake/Sleep Graph");
		db.close();
		return intent;
	}
	
}
