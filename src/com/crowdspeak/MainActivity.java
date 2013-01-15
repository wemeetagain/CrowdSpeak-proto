   package com.crowdspeak;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity{

	
	MessageListAdapter mAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        //create and add listview with posts to the current view
        ListView lv = (ListView) this.findViewById(android.R.id.content).findViewById(R.id.message_list);
     // //////
        String [] columns = {"MESSAGEID","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","NUMBEROFCOMMENTS"};
        Cursor query = DatabaseInstanceHolder.db.query("MESSAGETABLE", columns, null, null, null, null, "NUMBEROFVOTES");
        mAdapter = new MessageListAdapter(this,query);
        lv.setListAdapter(mAdapter);
    }


    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
*/
    
}
