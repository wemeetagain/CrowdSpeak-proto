package com.crowdspeak;

import java.net.DatagramPacket;
import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.database.sqlite.SQLiteDatabase;

public class MessageHandler implements Runnable {

	public DatagramPacket packet;
	public SQLiteDatabase db;
	
	public MessageHandler(DatagramPacket p)
	{
		packet = p;
	}
	
	public void run() {
		Message m = decode();
		updateMessageDB(db,m);
	}
	
	private void updateMessageDB(SQLiteDatabase db2, Message m) {
		// TODO Auto-generated method stub
		
	}

	public Message decode()
	{
		Message message=null;
		Document doc = byteArrayToDom(packet.getData());
		Element rootElement = doc.getDocumentElement();
		// need error checking
		
		
		return message;
	}
	
	public Document byteArrayToDom(byte[] ba)
	{
		Document doc = null;
		
		/*
		int length = convertByteArrayToInt(Arrays.copyOfRange(ba, 0, 4));
		byte[] messageByteArray = Arrays.copyOfRange(ba, 4, 4 + length);
		String xmlString = (String) convertByteArrayToObject(messageByteArray);
		
		try
		{
			doc = stringToDom(xmlString);
		}
		catch(SAXException se)
		{}
		
		*/
		
		return doc;
	}
}
class Message{
	public Message(int i, String s){}
}