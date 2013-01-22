package com.crowdspeak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageThreadActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String messageIdString = intent.getStringExtra("message_id"); 
		setContentView(R.layout.activity_message);
		
		String [] columns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT"};
		final String selection = "MESSAGEID =? "
				;
		Cursor cursor = DatabaseInstanceHolder.db.
//rawQuery(new String("SELECT MESSAGEID as _id, PERSONALVOTEVALUE, NUMBEROFVOTES, MESSAGETEXT, NUMBEROFCOMMENTS FROM MESSAGETABLE WHERE _id = \""+messageIdString+"\""),null);
				query("MESSAGETABLE", columns,selection, new String [] {messageIdString}, null, null, null);
		cursor.moveToFirst();
		Log.v("Hi",Integer.toString(cursor.getCount()));
	
		final String messageId = messageIdString;
				//cursor.getString(cursor.getColumnIndex("_id "));
		int pv = cursor.getColumnIndex("PERSONALVOTEVALUE");	
		//pv=1;
		Log.v("", Integer.toString(pv));
		int myVote = cursor.getInt(1);
		
		//selection = "MESSAGEID = " + messageId;
		View view = getWindow().getDecorView().findViewById(android.R.id.content);
		ImageButton upvote = (ImageButton)view.findViewById(R.id.activity_message_upvote);
		upvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Cursor c = DatabaseInstanceHolder.db.rawQuery("SELECT PERSONALVOTEVALUE AS _id FROM MESSAGE", new String [] {selection});
				   c.moveToFirst();
				   c.getInt(c.getColumnIndex("_id"));  
			   		}
				   });
		ImageButton downvote = (ImageButton)view.findViewById(R.id.activity_message_downvote);
		downvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   	
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
			downvote.setSelected(true);
			break;
			case 1:
			upvote.setSelected(true);	
			break;
			default: break;
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
		
		ListView lv = (ListView) this.findViewById(android.R.id.content).findViewById(R.id.message_list);
	     // //////
        String [] replyColumns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","NUMBEROFCOMMENTS"};
        Cursor query = DatabaseInstanceHolder.db.query("MESSAGETABLE", replyColumns, null, null, null, null, "NUMBEROFVOTES");
        MessageThreadAdapter mAdapter = new MessageThreadAdapter(this,query);
        //lv.setAdapter(mAdapter);
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
}
