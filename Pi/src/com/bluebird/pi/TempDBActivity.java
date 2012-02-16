package com.bluebird.pi;

import android.app.ListActivity;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.widget.ListView;


public class TempDBActivity extends ListActivity {

	// Create Database Object
	private PiDBAdapter db = new PiDBAdapter(this);
	private Cursor cursor;
	private CursorAdapter dataSource;
	private static final String fields[] = { "actualwake", "actualsleep", "_id" };

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Open Database and Return Cursor
		db.open();
		//db.insertOneRow(1328832000000L, 1328878800000L);
		//db.addDummyData(20,8,-5);
		cursor = db.getAllDays();
		
		// Create Cursor Adapter for use with List View
		dataSource = new SimpleCursorAdapter(this, R.layout.row, cursor, fields,
				new int[] { R.id.actualwake, R.id.actualsleep });
		
		// Populate List View With Content
		ListView view = getListView();
		view.setHeaderDividersEnabled(true);
		view.addHeaderView(getLayoutInflater().inflate(R.layout.row, null));
		setListAdapter(dataSource);
		
		// Close Database
		db.close();
		
	}
	
}
