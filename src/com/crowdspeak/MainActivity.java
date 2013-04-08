   package com.crowdspeak;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

	
	MessageListAdapter mAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        
        //create and add listview with posts to the current view
        final ListView lv = (ListView) this.findViewById(android.R.id.content).findViewById(R.id.message_list);
        
        //give life to message creation button
        final EditText message_text = (EditText) this.findViewById(R.id.create_text);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button b = (Button) this.findViewById(R.id.create_button);
        final Context c = this;
        b.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   String message = message_text.getText().toString();
				   if(message.length() != 0)
				   {
					   new MessageCreator(c,message).execute("");
					   //new Thread(mc).start();
					   message_text.setText("");
					   toast("Message Sent");
				   }
				   }
				   });
        
        
     // //////
        String [] columns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","CREATIONTIME","SENTTOSERVER"};
        Cursor query = DatabaseInstanceHolder.db.query("MESSAGETABLE", columns, null, null, null, null, "NUMBEROFVOTES");
        mAdapter = new MessageListAdapter(this,query);
        lv.setAdapter(mAdapter);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	mAdapter.notifyDataSetChanged();
    }
    
    public void refresh()
    {
    	String [] columns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","CREATIONTIME","SENTTOSERVER"};
	    Cursor query = DatabaseInstanceHolder.db.query("MESSAGETABLE", columns, null, null, null, null, "NUMBEROFVOTES");
	    mAdapter.changeCursor(query);
    	//mAdapter.notifyDataSetChanged();
    }
    
    public void toast(String s)
    {
			Context context = getApplicationContext();
			CharSequence text = s;
			int duration = Toast.LENGTH_SHORT;
			
			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.TOP,0,0);
			toast.show();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }
}
