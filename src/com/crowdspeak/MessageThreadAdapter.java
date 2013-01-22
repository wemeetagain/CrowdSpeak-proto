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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessageThreadAdapter extends CursorAdapter {

	public MessageThreadAdapter(Context context, Cursor c) {
		super(context, c);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		int myVote = cursor.getInt(
				cursor.getColumnIndex("PERSONALVOTEVALUE"));
		final int messageId = cursor.getColumnIndex("MESSAGEID");
		final String selection = "MESSAGEID = " + messageId;
		ImageButton upvote = (ImageButton)view.findViewById(R.id.comment_layout_upvote);
		upvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   Cursor c = DatabaseInstanceHolder.db.rawQuery("SELECT PERSONALVOTEVALUE AS _id FROM MESSAGE", new String [] {selection});
				   c.moveToFirst();
				   c.getInt(c.getColumnIndex("_id"));  
			   		}
				   });
		ImageButton downvote = (ImageButton)view.findViewById(R.id.comment_layout_downvote);
		downvote.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   	
				   }
				   });
		TextView numvotes = (TextView)view.findViewById(R.id.comment_layout_numvotes);
		TextView message_text = (TextView)view.findViewById(R.id.comment_layout_message_text);
		message_text.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				    // TODO Auto-generated method stub
				   	
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
		

		message_text.setText(cursor.getString(cursor.getColumnIndex("MESSAGETEXT")));

		
		//make cursor
		Cursor query = DatabaseInstanceHolder.db.rawQuery("SELECT COMMENTID2 AS COMMENTID FROM COMMENTHASCOMMENTTABLE WHERE COMMENTHASCOMMENTTABLE.COMMENTID1 = COMMENTTABLE.COMMENTID JOIN ORDER BY NUMBEROFCOMMENTS", null);
		//Cursor c = DatabaseInstanceHolder.db.rawQuery("SELECT COMMENTID, COMMENTTEXT, PERSONALVOTEVALUE FROM COMMENTTABLE WHERE COMMENTHASCOMMENTTABLE.COMMENTID1 = COMMENTTABLE.COMMENTID JOIN ORDER BY NUMBEROFCOMMENTS", null);
		
		if(query != null)
		{
			LinearLayout ll = (LinearLayout) view.findViewById(R.id.comment_area);
			
			//create listview, 
			ListView lv = new ListView(context);
			
			//select the comments wjoin to 
			//create adapter 
			MessageThreadAdapter adapter = new MessageThreadAdapter(context,query);
			
			//add adapter to listview
			lv.setAdapter(adapter);
			//add to comment_area, 
			ll.addView(lv);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.comment_layout, parent, false);
		bindView(v, context, cursor);
		return v;
	}

}