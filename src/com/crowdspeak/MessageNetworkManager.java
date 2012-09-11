package com.crowdspeak;

import java.net.DatagramSocket;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.Message;

public class MessageNetworkManager extends Service {

	private final IntentFilter intentFilter = new IntentFilter();
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		
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

		
	    
	    
		new Thread(new Runnable()
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
		new Thread(new Runnable()
		{
			public void run()
			{
				broadcast();
			}
		}).start();
		return null;
	}
	public void broadcast()
	{
		Message[] m = getBroadcastedMessages();
		for(Message mess:m)
		{
			sendMulticastMessage(mess);
			Thread.sleep(INTERMESSAGEDELAY);
		}
		Thread.sleep(BROADCASTDELAY);
		//^ can be in method caller
	}
	
	public Message[] getBroadcastedMessage()
	{
		Message[] m;
		// get Cursor for all messages w/ broadcast flag
		Cursor c = ;
		m = new Message[c.getCount()];
		for(int i = 0; i < m.length && !c.isAfterLast(); i++)
		{
			m[i] = new Message(c.getInt(0),c.getString(1));
		}
		return m;
	}
	
	public void receiveAndHandle(DatagramSocket socket)
	{
		while(true)
		{
			DatagramPacket p = emptyDatagramPacket();
			socket.receive(p);
			new Thread(new MessageHandler(p)).start();
			Thread.sleep(RECEIVEDELAY);
		}
	}

}
