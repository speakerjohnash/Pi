package com.bluebird.pi;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;

public class PiDBAdapter {

		// Database Column Definitions
		public static final String KEY_ROWID = "_id";
		public static final String KEY_GOALWAKE = "goalwake";
		public static final String KEY_ACTUALWAKE = "actualwake";
		public static final String KEY_GOALSLEEP = "goalsleep";
		public static final String KEY_ACTUALSLEEP = "actualsleep";
		public static final String KEY_STARTGOALTIME = "startgoaltime";
		public static final String KEY_ENDGOALTIME = "endgoaltime";
		
		// Database Meta Info
		private static final String DATABASE_NAME = "pi.db";
		private static final int DATABASE_VERSION = 1;
		private static final String TABLE_DAYS = "days";
		private static final String TABLE_GOALS = "goals";
		
		// Query For Most Recent Row
		private static final String CURRENT_DAY = "SELECT _id FROM days WHERE actualwake ISNULL OR actualsleep " +
				"ISNULL ORDER BY _id DESC LIMIT 1";
				
		// Create Database SQL strings
		private static final String CREATE_TABLE_DAYS = 
				"create table days (_id integer primary key autoincrement, "
				+ "goalwake integer, actualwake integer, "
				+ "goalsleep integer, actualsleep integer);";
		
		private static final String CREATE_TABLE_GOALS = 
				"create table goals (_id integer primary key autoincrement, "
				+ "startgoaltime integer, endgoaltime integer);";
		
		// Create Objects
		private final Context context;
		private DatabaseHelper DBHelper;
		private SQLiteDatabase db;
		
		// DBAdapter Constructor
		public PiDBAdapter(Context ctx){
		    this.context = ctx;
		    DBHelper = new DatabaseHelper(context);
		}

		private static class DatabaseHelper extends SQLiteOpenHelper{
			
			// Call Super Class Constructor
			DatabaseHelper(Context context){
			        super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
			
		    @Override
		    public void onCreate(SQLiteDatabase db){
		        
		    	// Create Database (each table created needs own execSQL call)
		    	db.execSQL(CREATE_TABLE_DAYS);
		    	db.execSQL(CREATE_TABLE_GOALS);
		    	
		        // Create New Row
				db.insert(TABLE_DAYS, KEY_ACTUALWAKE, null);
		    }

		    @Override
		    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		           Log.w("Pi", "Upgrading database from version " + oldVersion
		                 + " to "
		                 + newVersion + ", which will destroy all old data");
		           db.execSQL("DROP TABLE IF EXISTS days");
		           onCreate(db);
		    }
			
		}
		
		// Open Database
		public PiDBAdapter open() throws SQLException
		{
		    db = DBHelper.getWritableDatabase();
		    return this;
		}

		// Close Database
		public void close()
		{
		    DBHelper.close();
		}
		
		// Insert Wake Time
		public void insertWake(long time)
		{
			Log.d("LONG TIME(ME LOVE YOU)", Long.toString(time));
			
			// Create New Row
			db.insert(TABLE_DAYS, KEY_ACTUALWAKE, null);
			
			// Return Cursor Object at Most Recent Day
			Cursor cursor = db.rawQuery(CURRENT_DAY, null);
			
			// Move Cursor to First row
			cursor.moveToFirst();
			
			// Grab Row Id - Number is columnIndex
			int id = cursor.getInt(0);
			
			// Build Query
			String whereClause = "_id=" + Integer.toString(id);
			ContentValues values = new ContentValues();
			values.put("actualwake", time);
			
			// Add to Database
			db.update("days", values, whereClause, null);
			
		}

		// Insert Sleep Time
		public void insertSleep(long time)
		{
			// Return Cursor Object at Most Recent Day
			Cursor cursor = db.rawQuery(CURRENT_DAY, null);
			
			// Move Cursor to First row
			cursor.moveToFirst();
						
			// Grab Row Id - Number is columnIndex
			int id = cursor.getInt(0);
						
			// Build Query
			String whereClause = "_id=" + Integer.toString(id);
			ContentValues values = new ContentValues();
			values.put("actualsleep", time);
						
			// Add to Database
			db.update("days", values, whereClause, null);
			
		}
		
		// Get All Days from Database
		public Cursor getAllDays()
		{
		    Cursor cursor = db.rawQuery("SELECT * FROM days", null);
		    
		    // Move Cursor to First row
		 	cursor.moveToFirst();
		 			
		    return cursor;
		}
		
		// Get All Days from Database
		public Cursor getAllWake()
		{
		    Cursor cursor = db.rawQuery("SELECT _id, actualwake FROM days", null);
		    
		    // Move Cursor to First row
		 	cursor.moveToFirst();
		 			
		    return cursor;
		}
		
		// Get All Days from Database
		public Cursor getAllSleep()
		{
		    Cursor cursor = db.rawQuery("SELECT _id, actualsleep FROM days", null);
		    
		    // Move Cursor to First row
		 	cursor.moveToFirst();
		 			
		    return cursor;
		}

		// Create Dummy Data
		public void addDummyData(int rows, int wakeGradient, int sleepGradient)
		{
			double randomWakeSign;
			double randomSleepSign;
			
			String recentSleepQuery = "SELECT actualsleep FROM days " +
				"ORDER BY actualsleep DESC LIMIT 1";
			
			String recentWakeQuery = "SELECT actualwake FROM days " +
					"ORDER BY actualwake DESC LIMIT 1";
			
			Cursor recentWakeCursor = db.rawQuery(recentWakeQuery, null);
			recentWakeCursor.moveToFirst();
			long recentWake = recentWakeCursor.getLong(0);
			
			Cursor recentSleepCursor = db.rawQuery(recentSleepQuery, null);
			recentSleepCursor.moveToFirst();
			long recentSleep = recentSleepCursor.getLong(0);
			
			long thisRowSleep;
			long thisRowWake;
			long lastRowSleep = recentSleep;
			long lastRowWake = recentWake;
			
			
			for(int ii = 0; ii < rows; ii++)
			{
				String querbear = "INSERT INTO days (actualwake,actualsleep) VALUES ";
				randomWakeSign = Math.random();
				randomSleepSign = Math.random();
				
				long dayOffset = 84600 * 1000;
				
				if(randomSleepSign > 0.5)
					thisRowSleep = (long) (lastRowSleep + ((Math.random() * sleepGradient) * 60000));
				else
					thisRowSleep = (long) (lastRowSleep - ((Math.random() * sleepGradient) * 60000));
				
				if(randomWakeSign > 0.5)
					thisRowWake = (long) (lastRowWake + ((Math.random() * wakeGradient) * 60000));
				else
					thisRowWake = (long) (lastRowWake - ((Math.random() * wakeGradient) * 60000));
				
				//write to the string
				querbear += "(" + Long.toString(thisRowWake) + "," + Long.toString(thisRowSleep) + ")";
				db.execSQL(querbear);

				Log.d("QUEER", querbear);
				
				lastRowSleep = thisRowSleep + dayOffset;
				lastRowWake = thisRowWake + dayOffset;
			}
			
		}
		
		// Insert One Row
		public void insertOneRow(long wake, long sleep){
			String query = "INSERT INTO days (actualwake,actualsleep) VALUES " + "(" + Long.toString(wake) + "," + Long.toString(sleep) + ");";
			db.execSQL(query);
		}
		
		// Get Chart Data
		public GraphReturn getChartData(){
			// Get Most Recent Goal Start and End Time
			String timeQuery = "SELECT _id, startgoaltime, endgoaltime FROM goals ORDER BY _id";
			Cursor cursor = db.rawQuery(timeQuery, null);
			cursor.moveToLast();
			
			// TODO Make sure no error if no goal set
			
			long startGoalTime = cursor.getLong(1);
			long endGoalTime = cursor.getLong(2);
			
			// Get Rows Relevant to that Goal
			String query = "SELECT _id, actualwake, actualsleep FROM days WHERE actualsleep >= " + Long.toString(startGoalTime) + " AND actualsleep <= " + Long.toString(endGoalTime);
			
			Cursor cursor2 = db.rawQuery(query, null);
			
			// Create Return Object
			GraphReturn graphReturn = new GraphReturn(cursor2, startGoalTime, endGoalTime);
			
			return graphReturn;
		}
		
		// Add Goal Row
		public void insertGoal(long beginGoalTime, long endGoalTime){
			String query = "INSERT INTO goals (startgoaltime,endgoaltime) VALUES " + "(" + Long.toString(beginGoalTime) + "," + Long.toString(endGoalTime) + ")";
			db.execSQL(query);
		}

		// Multiple Graph Return Values
		
		public class GraphReturn {
			
			private Cursor cursor;
			private long startGoalTime;
			private long endGoalTime;
			
			public GraphReturn (Cursor cursor, long startGoalTime, long endGoalTime) {
				this.cursor = cursor;
				this.startGoalTime = startGoalTime;
				this.endGoalTime = endGoalTime;
			}
			
			public Cursor getCursor() {
				return cursor;
			}
			
			public long getStart() {
				return startGoalTime;
			}
			
			public long getEnd() {
				return endGoalTime;
			}
			
		}

	
}
