package com.crowdspeak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseInstanceHolder{

    public static MessageDatabaseHelper DBHelper;  
    public static SQLiteDatabase db; // This is global variable to access across the applicaiton

    public static void createDBInstance(Context pContext){
        if(DBHelper == null) {
            DBHelper = new MessageDatabaseHelper(pContext); // This will be your DB Handler Class
            db = DBHelper.getWritableDatabase(); // Initialze the DB Note: openAndCreateDataBase is a utility method created by you to do everything an return the DB object
        }
      }
}
