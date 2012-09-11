package com.crowdspeak;

import java.util.Arrays;

import org.w3c.dom.Document;

public class MessageHandler implements Runnable {

	public DatagramPacket packet;
	public SQLiteDatabase db;
	
	public MessageHandler(DatagramPacket p)
	{
		packet = p;
	}
	
	public void run() {
		Message m = decode(packet);
		updateMessageDB(db,m);
	}
	
	public Message decode()
	{
		Message message;
		Document doc = byteArrayToDom(packet.getData());
		Element rootElement = doc.getDocumentElement();
		// need error checking
		
		
		return message;
	}
	
	public Document byteArrayToDom(byte[] ba)
	{
		int length = convertByteArrayToInt(Arrays.copyOfRange(ba, 0, 4));
		byte[] messageByteArray = Arrays.copyOfRange(ba, 4, 4 + length);
		String xmlString = (String) convertByteArrayToObject(messageByteArray);
		Document doc = null;
		try
		{
			doc = stringToDom(xmlString);
		}
		catch(SAXException se)
		{}
		return doc;
	}
}
