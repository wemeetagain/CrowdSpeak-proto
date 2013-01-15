package com.crowdspeak;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MessageThreadActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String messageId = intent.getStringExtra("message_id"); 
		setContentView(R.layout.activity_message);
		
		String [] columns = {"MESSAGEID","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","NUMBEROFCOMMENTS"};
		String selection = "MESSAGEID = " + messageId;
		Cursor cursor = DatabaseInstanceHolder.db.
				query("MESSAGETABLE", columns, "MESSAGE", selection, null, null, null);
		
		int myVote = cursor.getInt(
				cursor.getColumnIndex("PERSONALVOTEVALUE"));
		final int messageId = cursor.getColumnIndex("MESSAGEID");
		String selection = "MESSAGEID = " + messageId;
		ImageButton upvote = (ImageButton)view.findViewById(R.id.message_upvote);
		upvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Cursor c = DatabaseInstanceHolder.db.rawQuery("SELECT PERSONALVOTEVALUE AS _id FROM MESSAGE", new String [] {selection});
				   c.moveToFirst();
				   c.getInt(c.getColumnIndex("_id"));  
			   		}
				   });
		ImageButton downvote = (ImageButton)view.findViewById(R.id.message_downvote);
		downvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   	
				   }
				   });
		TextView numvotes = (TextView)view.findViewById(R.id.message_numvotes);
		TextView message_text = (TextView)view.findViewById(R.id.message_text);
		message_text.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Intent myIntent = new Intent(MainActivity.this, MessageThreadActivity.class);
				   myIntent.putExtra("message_id", Integer.toString(messageId));
				   MessageThreadActivity.this.startActivity(myIntent);
			   		}
				   });
		TextView numcomments = (TextView)view.findViewById(R.id.message_numcomments);
		
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
		
		numvotes.setText(cursor.getInt(cursor.getColumnIndex("NUMBEROFVOTES")));
		message_text.setText(cursor.getString(cursor.getColumnIndex("MESSAGETEXT")));
		numcomments.setText(cursor.getInt(cursor.getColumnIndex("NUMBEROFCOMMENTS")));
	}
}
