package com.crowdspeak;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MessageThreadActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String messageIdString = intent.getStringExtra("message_id"); 
		setContentView(R.layout.activity_message);
		
		String [] columns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","SENTTOSERVER"};
		final String selection = "MESSAGEID=? "
				;
		Cursor cursor = DatabaseInstanceHolder.db.
//rawQuery(new String("SELECT MESSAGEID as _id, PERSONALVOTEVALUE, NUMBEROFVOTES, MESSAGETEXT, NUMBEROFCOMMENTS FROM MESSAGETABLE WHERE _id = \""+messageIdString+"\""),null);
				query("MESSAGETABLE", columns, selection, new String [] {messageIdString}, null, null, null);
		cursor.moveToFirst();
		Log.v("Hi",Integer.toString(cursor.getCount()));
	
		final String messageId = messageIdString;
				//cursor.getString(cursor.getColumnIndex("_id "));
		int pv = cursor.getColumnIndex("PERSONALVOTEVALUE");	
		//pv=1;
		Log.v("", Integer.toString(pv));
		int myVote = cursor.getInt(1);
		final int sentToServer = cursor.getInt(4);
		
		//selection = "MESSAGEID = " + messageId;
		View view = getWindow().getDecorView().findViewById(android.R.id.content);
		final ToggleButton upvote = (ToggleButton)view.findViewById(R.id.activity_message_upvote);
		final ToggleButton downvote = (ToggleButton)view.findViewById(R.id.activity_message_downvote);
		upvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean on)
		    {
				    // TODO Auto-generated method stub
				   // Create content values that contains the name of the column you want to update and the value you want to assign to it 
				   
				   ContentValues cv = new ContentValues();
				   if(on)
				   {
					   downvote.setChecked(false);
					   cv.put("PERSONALVOTEVALUE", "1");
					   
				   }
				   else
				   {
					   cv.put("PERSONALVOTEVALUE", "0");
				   }
				   if(sentToServer!=0)
					   cv.put("SENTTOSERVER", 1);
				   String where = "MESSAGEID=?"; // The where clause to identify which columns to update.
				   String[] value = { messageId }; // The value for the where clause.
				   
				   // Update the database (all columns in TABLE_NAME where my_column has a value of 2 will be changed to 5)
				   DatabaseInstanceHolder.db.update("MESSAGETABLE", cv, where, value);
		    }
		});
		downvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean on)
		    {
				    // TODO Auto-generated method stub
				   // Create content values that contains the name of the column you want to update and the value you want to assign to it 
				   ContentValues cv = new ContentValues();
				   if(on)
				   {
					   upvote.setChecked(false);
					   cv.put("PERSONALVOTEVALUE", "-1");
				   }
				   else
				   {
					   cv.put("PERSONALVOTEVALUE", "0");
				   }
				   if(sentToServer!=0)
					   cv.put("SENTTOSERVER", 1);
				   String where = "MESSAGEID=?"; // The where clause to identify which columns to update.
				   String[] value = { messageId }; // The value for the where clause.

				   // Update the database (all columns in TABLE_NAME where my_column has a value of 2 will be changed to 5)
				   DatabaseInstanceHolder.db.update("MESSAGETABLE", cv, where, value);
				   }
				   });
		TextView numvotes = (TextView)view.findViewById(R.id.activity_message_numvotes);
		TextView message_text = (TextView)view.findViewById(R.id.activity_message_text);
		message_text.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   //reply?
				   
			   	}
				});
		switch (myVote)
		{
			case -1:
			downvote.setChecked(true);
			break;
			case 1:
			upvote.setChecked(true);	
			break;	
			default: 
			downvote.setChecked(false);	
			upvote.setChecked(false);break;
		}
	/*
		TextView numcomments = (TextView)view.findViewById(R.id.message_numcomments);
		
	*/	
		numvotes.setText(cursor.getString(
				2));
				//cursor.getColumnIndex("NUMBEROFVOTES")));
		message_text.setText(cursor.getString(
				3));
				//cursor.getString(cursor.getColumnIndex("MESSAGETEXT")));
	//	numcomments.setText(cursor.getInt(cursor.getColumnIndex("NUMBEROFCOMMENTS")));
		
		 // //////
        String [] replyColumns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","NUMBEROFCOMMENTS"};
        Cursor query = DatabaseInstanceHolder.db.query("MESSAGETABLE", replyColumns, null, null, null, null, "NUMBEROFVOTES");
        query.moveToFirst();
        if(query.getCount()==0)
        {
        	ListView lv = (ListView) this.findViewById(android.R.id.content).findViewById(R.id.message_list);
        	MessageThreadAdapter mAdapter = new MessageThreadAdapter(this,query);
        	lv.setAdapter(mAdapter);
        }
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
        /*if (item.getItemId() == R.id.courses) {
            startActivity(new Intent(this, CoursesActivity.class));
        }*/
        return super.onOptionsItemSelected(item);
    }
}
