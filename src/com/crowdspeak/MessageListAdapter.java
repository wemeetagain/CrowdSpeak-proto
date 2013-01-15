package com.crowdspeak;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MessageListAdapter extends CursorAdapter {

	public MessageListAdapter(Context context, Cursor c) {
		super(context, c);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		int myVote = cursor.getInt(
				cursor.getColumnIndex("PERSONALVOTEVALUE"));
		final int messageId = cursor.getColumnIndex("MESSAGEID");
		String selection = "MESSAGEID = " + messageId;
		ImageButton upvote = (ImageButton)view.findViewById(R.id.upvote);
		upvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Cursor c = DatabaseInstanceHolder.db.rawQuery("SELECT PERSONALVOTEVALUE AS _id FROM MESSAGE", new String [] {selection});
				   c.moveToFirst();
				   c.getInt(c.getColumnIndex("_id"));  
			   		}
				   });
		ImageButton downvote = (ImageButton)view.findViewById(R.id.downvote);
		downvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   	
				   }
				   });
		TextView numvotes = (TextView)view.findViewById(R.id.numvotes);
		TextView message_text = (TextView)view.findViewById(R.id.message_text);
		message_text.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Intent myIntent = new Intent(MainActivity.this, MessageThreadActivity.class);
				   myIntent.putExtra("message_id", Integer.toString(messageId));
				   MessageThreadActivity.this.startActivity(myIntent);
			   		}
				   });
		TextView numcomments = (TextView)view.findViewById(R.id.numcomments);
		
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

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.message_layout, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}
