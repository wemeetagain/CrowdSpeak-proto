package com.crowdspeak;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;


public class MessageCreator extends AsyncTask<String, Void, String> {

	private String message;
	private Context c;
	
	public MessageCreator(Context cn, String m)
	{
		message = m;
		c = cn;
	}
	@Override
    protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (month/day/year)
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Get the date today using Calendar object.
		Date today = (Date) Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String date = "\""+df.format(today)+"\"";
		
		String myIP = "\""+Constants.getIPAddress(true)+"\"";
		String hash = Constants.getHash(message+date+myIP);
		hash=hash.substring(0,15);
		hash = "\""+ hash +"\"";
		String query = 
"INSERT INTO MESSAGETABLE VALUES ("+ hash +",\""+ message + "\",0,1,"+date+",0,"+myIP+",1," + hash + ",0)";
		/*
		  messageId + " TEXT PRIMARY KEY , " 
		+ messageBody + " TEXT , " 
		+ numComments + " INTEGER , " 
		+ broadcastBit + " INTEGER , " 
		+ creationTime + " TIMESTAMP , " 
		+ numVotes + " INTEGER , "
		+ broadcasterIP + " TEXT , " 
		+ personalVoteValue + " INTEGER , " 
		+ messageHash + " TEXT
		*/
		Log.v("createMessage",query);
		DatabaseInstanceHolder.db.execSQL(query);
		return "true";
	}
	@Override
    protected void onPostExecute(String result) {    
		if(result.equals("true"))
		{
			((MainActivity) c).refresh();
			/*ListView lv = (ListView) ((Activity) c).findViewById(R.id.message_list);
			MessageListAdapter mAdapter = (MessageListAdapter) lv.getAdapter();
			
			
			String [] columns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","NUMBEROFCOMMENTS"};
		    Cursor query = DatabaseInstanceHolder.db.query("MESSAGETABLE", columns, null, null, null, null, "NUMBEROFVOTES");
		    MessageListAdapter mAdapter = new MessageListAdapter(c,query);
		    lv.setAdapter(mAdapter);
			*/
		}
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
	
}
