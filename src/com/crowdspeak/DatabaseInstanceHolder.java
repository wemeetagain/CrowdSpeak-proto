package com.crowdspeak;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.widget.Toast;

public class DatabaseInstanceHolder{

    public static MessageDatabaseHelper DBHelper;  
    public static SQLiteDatabase db; // This is global variable to access across the application

    public static void createDBInstance(Context pContext){
        if(DBHelper == null) {
            DBHelper = new MessageDatabaseHelper(pContext); // This will be the DB Handler Class
            db = DBHelper.getWritableDatabase(); // Initialize the DB Note: openAndCreateDataBase is a utility method created by you to do everything an return the DB object
        }
      }
}
class Constants
{
	public static String getHash(String password)
	{
	 
	    MessageDigest md=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    md.update(password.getBytes());
	
	    byte byteData[] = md.digest();
	    
	    StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	return hexString.toString();
	}
	 public static String getIPAddress(boolean useIPv4) {
	        try {
	            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
	            for (NetworkInterface intf : interfaces) {
	                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
	                for (InetAddress addr : addrs) {
	                    if (!addr.isLoopbackAddress()) {
	                        String sAddr = addr.getHostAddress().toUpperCase();
	                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
	                        if (useIPv4) {
	                            if (isIPv4) 
	                                return sAddr;
	                        } else {
	                            if (!isIPv4) {
	                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
	                                return delim<0 ? sAddr : sAddr.substring(0, delim);
	                            }
	                        }
	                    }
	                }
	            }
	        } catch (Exception ex) { } // for now eat exceptions
	        return "";
	    }
}