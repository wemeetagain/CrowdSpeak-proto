package com.crowdspeak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.IBinder;
import android.util.Log;

public class MessageNetworkManager extends Service {

	private static final long INTERMESSAGEDELAY = 0;
	private static final long BROADCASTDELAY = 5000;
	private static final long RECEIVEDELAY = 0;
	private String sessionCookie;
	
	private final IntentFilter intentFilter = new IntentFilter();
	Channel mChannel;
	BroadcastReceiver mReceiver;
	DatagramSocket mcsocket,unisocket;
	
	GPSTracker gps;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		
		/*
		//WiFi Direct initialization
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    mChannel = mManager.initialize(this, getMainLooper(), null);
	    mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
		
		//  Indicates a change in the Wi-Fi Peer-to-Peer status.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

	    // Indicates a change in the list of available peers.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

	    // Indicates the state of Wi-Fi P2P connectivity has changed.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

	    // Indicates this device's details have changed.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		*/
		
		sessionCookie = intent.getStringExtra("session"); 
		
	    gps = new GPSTracker(this);
	    
		/*new Thread(new Runnable()
		{
			public void run()
			{
				receiveAndHandle(mcsocket);
			}
		}).start();
		new Thread(new Runnable()
		{
			public void run()
			{
				receiveAndHandle(unisocket);
			}
		}).start();
		*/
		new Thread(new Runnable()
		{
			public void run()
			{
				while(true)
				{
					broadcast();
					try {
						Thread.sleep(BROADCASTDELAY);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		return 0;
	}
	public void broadcast()
	{
		Log.v("broadcast","begin broadcast()");
		Message[] m = getBroadcastingMessagesForServer();
		HttpClient httpclient = new DefaultHttpClient();
   	    HttpPost httppost = new HttpPost("http://9.wut.se:5000/me");
   	    httppost.addHeader("Cookie",sessionCookie);
   	    double lat = 0;
   	    double lon = 0;
   	    //get GPS location
   	    if(gps.canGetLocation()){
   	    	lat=gps.getLatitude(); // returns latitude
   	    	lon=gps.getLongitude(); // returns longitude
   	    }
   	    Log.v("broadcast","lat "+lat+" lon "+lon);
   		try {
   			Log.v("broadcast",arrayToString(m,","));
   	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
   	    nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
   	 	nameValuePairs.add(new BasicNameValuePair("lon", Double.toString(lon)));
   	 	nameValuePairs.add(new BasicNameValuePair("nummessages", Integer.toString(m.length)));
   	 	int acc = 0;
   	    for(Message mess:m)
		{
   	    	nameValuePairs.add(new BasicNameValuePair("p"+acc, Integer.toString(mess.p)));
			nameValuePairs.add(new BasicNameValuePair("id"+acc, mess.id));
			nameValuePairs.add(new BasicNameValuePair("pvv"+acc, Integer.toString(mess.PVV)));
			 
		    if(mess.p==0)
			{
		    	nameValuePairs.add(new BasicNameValuePair("text"+acc, mess.text));
		    	nameValuePairs.add(new BasicNameValuePair("cret"+acc, mess.creationTime));
			}
		    acc ++;
		}
		
	   
	   	   
	       
	       httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	       HttpResponse response=httpclient.execute(httppost);
	       String r="";
	       HttpEntity responseEntity = response.getEntity();
	       if(responseEntity!=null) {
	           r = EntityUtils.toString(responseEntity);
	       }
	       Log.v("broadcast",r);
	       parseAndUpdate(r);

	    } catch (ClientProtocolException e) {
	         // TODO Auto-generated catch block
	    } catch (IOException e) {
	         // TODO Auto-generated catch block
	    }
		
	}
	public String arrayToString(Message[] array, String delimiter) {
	    StringBuilder arTostr = new StringBuilder();
	    if (array.length > 0) {
	        arTostr.append(array[0]);
	        for (int i=1; i<array.length; i++) {
	            arTostr.append(delimiter);
	            arTostr.append(array[i]);
	        }
	    }
	    return arTostr.toString();
	}
	private void parseAndUpdate(String s)
	{
		boolean updated = false;
		int nummessages;
		int index = 0;
		if(isValid(s))
		{
			Log.v("parse","Valid string");
			nummessages = 0;
			Log.v("parse",nummessages+" message(s) in string");
			index=s.indexOf("[");
			int v;
			do
			{
				
				String id= next("_id",s, (s.indexOf("_id",index)+1));
				updated = false;
				if(!id.equals("result"))
					{
					Cursor c = DatabaseInstanceHolder.db.query
							("MESSAGETABLE",new String[] {"MESSAGEID as _id","NUMBEROFCOMMENTS","BROADCASTBIT","CREATIONTIME","BROADCASTERIP","PERSONALVOTEVALUE","MESSAGEHASH"},
							"MESSAGEID=?",new String[] {id},null,null,null,null);
					c.moveToFirst();
					v=s.indexOf("vote_count",index);
					String numVotes=s.substring(s.indexOf(" ",v)+1,s.indexOf(",",v));
					String pvv="";
					String text=next("text",s,index);
					v=s.indexOf("cretd",index);
					String cretd=s.substring(s.indexOf("(",v)+	1,s.indexOf(")",v));
					index = s.indexOf("}, {",index);
					if(index!=-1)
						index++;
					Log.v("parse",id+" "+numVotes+" "+pvv+" "+text+" "+cretd +" " + index);
					try
					{
					if(!(c.getCount()==0))
					{
						Log.v("update", "old message");
						ContentValues cv=new ContentValues();
						   cv.put("NUMBEROFVOTES", numVotes);
						   cv.put("SENTTOSERVER", "2");
						   //cv.put("", "");
						   String idString = "\""+id+"\"";
						   int i = DatabaseInstanceHolder.db.update("MESSAGETABLE", cv, "MESSAGEID=?", 
						    new String []{idString});
						   updated=true;
					}}
					catch(Exception ex)
					{
						Log.v("update", "Error: "+ex.getMessage());
					}
					finally
					{
						if(!updated)
						{
							Log.v("update", "NEW message");
							DatabaseInstanceHolder.db.execSQL
								("INSERT INTO MESSAGETABLE " +
								"(MESSAGEID,MESSAGETEXT,NUMBEROFCOMMENTS,BROADCASTBIT,CREATIONTIME," +
								"NUMBEROFVOTES,BROADCASTERIP,PERSONALVOTEVALUE,MESSAGEHASH,SENTTOSERVER) " +
								"  VALUES ( " + 
								"\""+id +"\", "+ 
								"\""+text +"\", "+
								0 +", "+
								0 +", "+
								"\""+cretd +"\", "+
								numVotes +", "+
								"\""+"XXX.XXX.XXX" +"\", "+
								0 +", "+
								"\""+id +"\", "+
								2+" )");
						}
					}
				}
				else
					index=-1;
			}
			while(index!=-1);
					
		}
	}
	
	private boolean isValid(String s)
	{
		if(s.indexOf("\'ok\'")==2)
			return true;
		return false;
	}
	
	private int numMessages(String s)
	{
		int index = 0;
		int ct = 1;
		do
		{
			Log.v("numMessages","index:"+index);
			index = s.indexOf("}, {",index);
			if(index!=-1)
				ct++;
		}
		while(index!=-1);
		return ct;
	}
	//they ve managed to push their square peg into your round hole
	private String next(String key, String s, int index)
	{
		String value="";
		//from i to j
		int i = s.indexOf(key,index);
		int j;
		i=s.indexOf(":",i);
		i=s.indexOf("\'",i)+1;
		j=s.indexOf("\'",i);
		j=s.charAt(j-1)=='\\'?s.indexOf("\'",j):j;
		value=s.substring(i,j);
		return value;
	}
	
	private void sendMulticastMessage(Message mess) {
		// TODO Auto-generated method stub
		
	}
	
	public Message[] getBroadcastingMessagesForServer()
	{
		ArrayList<Message> m;
		m = new ArrayList<Message>();
		// get Cursor for all messages w/ broadcast flag
		String [] columns = {"MESSAGEID as _id","PERSONALVOTEVALUE","NUMBEROFVOTES","MESSAGETEXT","CREATIONTIME","BROADCASTERIP","SENTTOSERVER"};
		final String selection = "BROADCASTBIT=? ";
		
		try
		{
		Cursor c = DatabaseInstanceHolder.db.
				query("MESSAGETABLE", columns, null, null, null, null, null);
		c.moveToFirst();
		
		for(int i = 0; i < c.getCount() && !c.isAfterLast(); i++)
		{
			Message mi = new Message();
			String broadcasterIp =
					c.getString(
					c.getColumnIndex("BROADCASTERIP"));
			String messageId =
					c.getString(
					c.getColumnIndex("_id"));
			int sentToServer=
					c.getInt(
					c.getColumnIndex("SENTTOSERVER"));
			int personalVoteValue=
					c.getInt(
					c.getColumnIndex("PERSONALVOTEVALUE"));
			Log.v("getMessages", "BIP"+broadcasterIp);
			Log.v("getMessages", "ID"+messageId);
			mi.id=messageId;
			mi.PVV=personalVoteValue;
			int p = 1;
			Log.v("getMessages",broadcasterIp + " " + Integer.toString(sentToServer));
			if(broadcasterIp.equals(Constants.getIPAddress(true)) && sentToServer == 0)
				p = 0;
			//purpose 0 = we are broadcasting message for first time
			//              store message information, creation time, ip, votevalue
			// purpose 1 = we are now participants, store vote value
			mi.p=p;
			if(p==0)
			{
				String messageText = c.getString(
						c.getColumnIndex("MESSAGETEXT"));
				String creationTime = c.getString(
						c.getColumnIndex("CREATIONTIME"));
				mi.text=messageText;
				mi.creationTime=creationTime;
			}
			else if(p==1)
			{
				
			}
			m.add(mi);
			c.moveToNext();
		}
		}
		catch(Exception e)
		{
			Log.v("getMessages","Error!");
			Log.v("getMessages",e.getMessage());
			m = new ArrayList<Message>();
		}
		Log.v("getMessages",m.toString());
		Message [] a= new Message[0];
		return  m.toArray(a);
	}
	
	public void receiveAndHandle(DatagramSocket socket)
	{
		while(true)
		{
			DatagramPacket p = emptyDatagramPacket();
			try {
				socket.receive(p);
				new Thread(new MessageHandler(p)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(RECEIVEDELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private DatagramPacket emptyDatagramPacket() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
