package com.crowdspeak;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDatabaseHelper extends SQLiteOpenHelper {

	static final String dbName = ;
	static final String messageTable = ;
	static final String colId = ;
	
	public MessageDatabaseHelper(Context context)
	{
		super(context,dbName,null,1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE" + messageTable + " ( " + colId + "INTEGER PRIMARY KEY , " + ...);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + messageTable);
		onCreate(db);
	}

}
