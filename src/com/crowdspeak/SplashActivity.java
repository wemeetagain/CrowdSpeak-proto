  package com.crowdspeak;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DatabaseInstanceHolder.createDBInstance(getApplicationContext());
        
        //enter text values into database
        //DatabaseInstanceHolder.db.execSQL("INSERT INTO MESSAGETABLE VALUES (1,\"This is a message\",0,0,\"2013-1-19 10:20:20\",20,\"nothing\",0,\"nothing\")");
        
        /*
		db.execSQL("CREATE TABLE " + messageTable + " ( " + messageId + " INTEGER PRIMARY KEY , " + messageBody + " TEXT , " 
		+ numComments + " INTEGER , " + broadcastBit + " BOOLEAN , " + creationTime + " TIMESTAMP , " + numVotes + " INTEGER , "
		+ broadcasterIP + " TEXT , " + personalVoteValue + " INTEGER , " + messageHash + " TEXT )");
         */
        
        //login
        Intent intent1 = new Intent(this,LoginActivity.class);
        startActivity(intent1);
        
        //Intent intent2 = new Intent(this, MainActivity.class);
        //startActivity(intent2);
        finish();
    }

    
}
