package com.crowdspeak;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MessageListAdapter extends CursorAdapter {

	public MessageListAdapter(Context context, Cursor c) {
		super(context, c);
	}
	
	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
		final String messageId = cursor.getString(
				cursor.getColumnIndex("_id"));
		final String selection = "MESSAGEID = " + messageId;
		final ToggleButton upvote = (ToggleButton)view.findViewById(R.id.message_layout_upvote);
		final ToggleButton downvote = (ToggleButton)view.findViewById(R.id.message_layout_downvote);
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
				   String where = "MESSAGEID=?"; // The where clause to identify which columns to update.
				   String[] value = { messageId }; // The value for the where clause.

				   // Update the database (all columns in TABLE_NAME where my_column has a value of 2 will be changed to 5)
				   DatabaseInstanceHolder.db.update("MESSAGETABLE", cv, where, value);
				   }
				   });
		TextView numvotes = (TextView)view.findViewById(R.id.message_layout_numvotes);
		TextView message_text = (TextView)view.findViewById(R.id.message_layout_text);
		message_text.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Intent myIntent = new Intent(context, MessageThreadActivity.class);
				   myIntent.putExtra("message_id", messageId);
				   context.startActivity(myIntent);
			   		}
				   });
		TextView numcomments = (TextView)view.findViewById(R.id.message_layout_numcomments);
		int myVote = cursor.getInt(
				cursor.getColumnIndex("PERSONALVOTEVALUE"));
		switch (myVote)
		{
			case -1:
			downvote.setChecked(true);
			break;
			case 1:
			upvote.setChecked(true);	
			break;
			default: break;
		}
		
		numvotes.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("NUMBEROFVOTES"))));
		message_text.setText(cursor.getString(cursor.getColumnIndex("MESSAGETEXT")));
		numcomments.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex("NUMBEROFCOMMENTS"))));
		
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
