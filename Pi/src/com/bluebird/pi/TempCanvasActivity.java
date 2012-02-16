package com.bluebird.pi;

import java.util.Calendar;

import com.bluebird.pi.PiDBAdapter.GraphReturn;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Color;


public class TempCanvasActivity extends Activity {
	
	private double xRatio;
	private double yRatio;
	private int screenWidth;
	protected int screenPadding;
	private int screenHeight;
	private Point[] wakeData;
	private Point[] sleepData;
	private Point[] sunRise;
	private Point[] sunSet;
	private Calendar today;
	
	// Create Database Object
	private PiDBAdapter db = new PiDBAdapter(this);
	private GraphReturn graphReturn;
	private Cursor cursor;
	private long startGoalTime;
	private long endGoalTime;
	
	// View
	SunChartView sunchartview;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get Reconfiguration Object
		final Object graphData = getLastNonConfigurationInstance();
		
		// If First Create Grab Data from Database
		if (graphData == null){
			
			// Get Most Recent Goal Data for View
			db.open();
			graphReturn = db.getChartData();
			cursor = graphReturn.getCursor();
			startGoalTime = graphReturn.getStart();
			endGoalTime = graphReturn.getEnd();
			today = Calendar.getInstance();
			
			// Close DB
			db.close();
			
		}
		
		// TODO Get Sunrise / Sunset Data
		
		// Create View
		sunchartview = new SunChartView(this);
		setContentView(sunchartview);
			
	}
	
	// Save Object on Configuration Change (Screen Orient)
	@Override
	public Object onRetainNonConfigurationInstance() {
		
		// TODO Return Graph Data (or Bitmap?)
		return null;
	}
	
	// SUN CHART VIEW (move to own file?)
	private class SunChartView extends View{

		public SunChartView(Context context) {
			super(context);
		}
		
		protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
		{
			
			// Get Screen Width and Height
		    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		    screenWidth = MeasureSpec.getSize(widthMeasureSpec);
		    screenHeight = MeasureSpec.getSize(heightMeasureSpec);
		    screenPadding = (int) (0.08 * screenWidth);
		    Log.d("padding", Integer.toString(screenPadding));
		    // Log.d("width",Integer.toString(screenWidth));
		    // Log.d("height",Integer.toString(screenHeight));
		   
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			
			// bg color
			canvas.drawColor(Color.rgb(49, 49, 49));
			
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(4);
			paint.setColor(Color.rgb(221, 178, 74));
			Path axes = new Path();
			
			axes.lineTo(0, (screenHeight - screenPadding * 2));
			axes.lineTo((screenWidth - screenPadding * 2), (screenHeight - screenPadding * 2));
			axes.offset(screenPadding, screenPadding);
			canvas.drawPath(axes, paint);
			// path object called axes
			// make sure you have padding
		}
	
	}

}
