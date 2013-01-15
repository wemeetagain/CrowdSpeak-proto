package com.crowdspeak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDatabaseHelper extends SQLiteOpenHelper {

	static final String dbName = "CROWDSPEAK";
	
	static final String messageTable = "MESSAGETABLE";
	static final String messageId = "MESSAGEID";	
	static final String messageBody = "MESSAGETEXT";
	static final String numComments = "NUMBEROFCOMMENTS";
	static final String broadcastBit = "BROADCASTBIT";
	static final String creationTime = "CREATIONTIME";
	static final String numVotes = "NUMBEROFVOTES";
	static final String broadcasterIP = "BROADCASTERIP";
	static final String personalVoteValue = "PERSONALVOTEVALUE";
	static final String messageHash = "MESSAGEHASH";
	//static final String listOfNeighbors = ;
	//static final String listOfComments = ;
	
	static final String commentTable = "COMMENTTABLE";
	static final String commentId = "COMMENTID";	
	static final String commentBody = "COMMENTTEXT";
	/*static final String creationTime = "CREATIONTIME";
	static final String numVotes = "NUMBEROFVOTES";
	static final String broadcasterIP = "BROADCASTERIP";
	static final String personalVoteValue = "PERSONALVOTEVALUE";
	//static final String listOfComments = ;
	*/
	
	static final String neighborTable = "NEIGHBORTABLE";
	static final String neighborIP = "NEIGHBORIP";	
	static final String lastHeardFrom = "LASTHEARDFROM";
	
	static final String messageHasNeighborTable = "MESSAGEHASNEIGHBORTABLE";
	static final String messageHasCommentTable = "MESSAGEHASCOMMENTTABLE";
	static final String commentHasCommentTable = "COMMENTHASCOMMENTTABLE";
	static final String commentId1= "COMMENTID1";
	static final String commentId2= "COMMENTID2";
	
	
	public MessageDatabaseHelper(Context context)
	{
		super(context,dbName,null,1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create message table
		db.execSQL("CREATE TABLE" + messageTable + " ( " + messageId + " INTEGER PRIMARY KEY , " + messageBody + " TEXT , " 
				+ numComments + " INTEGER , " + broadcastBit + " BOOLEAN , " + creationTime + " TIMESTAMP , " + numVotes + " INTEGER , "
				+ broadcasterIP + " TEXT , " + personalVoteValue + " INTEGER , " + messageHash + " TEXT )");
		//create comment table
		db.execSQL("CREATE TABLE" + commentTable + " ( " + commentId + " INTEGER PRIMARY KEY , " + commentBody + " TEXT , "
				+ creationTime + " TIMESTAMP , " + numVotes + " INTEGER , "	+ broadcasterIP + " TEXT , " + personalVoteValue + " INTEGER )");
		//create neighbors table
		db.execSQL("CREATE TABLE" + neighborTable + " ( " + neighborIP + " TEXT PRIMARY KEY , " + lastHeardFrom + " TIMESTAMP )");
		//create messagehasneighbor table
		db.execSQL("CREATE TABLE" + messageHasNeighborTable + " ( " + messageId + " INTEGER PRIMARY KEY, "  + neighborIP
				+ " TEXT , FOREIGN KEY( " + messageId + " ) REFERENCES " + messageTable + " ( " + messageId + " ) , FOREIGN KEY("
				+ neighborIP + " ) REFERENCES " + neighborTable + " ( "+ neighborIP + " ) )");
		//create messagehascomment table
		db.execSQL("CREATE TABLE" + messageHasCommentTable + " ( " + messageId + " INTEGER PRIMARY KEY, "  + commentId
				+ " INTEGER , FOREIGN KEY( " + messageId + " ) REFERENCES " + messageTable + " ( " + messageId + " ) , FOREIGN KEY("
				+ commentId + " ) REFERENCES " + commentTable + " ( "+ commentId + " ) )");	
		//create commenthascomment table
		db.execSQL("CREATE TABLE" + commentHasCommentTable + " ( " + commentId1 + " INTEGER PRIMARY KEY, "  + commentId2
				+ " INTEGER , FOREIGN KEY( " + commentId1 + " ) REFERENCES " + commentTable + " ( " + commentId + " ) )");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + messageTable);
		db.execSQL("DROP TABLE IF EXISTS " + commentTable);
		db.execSQL("DROP TABLE IF EXISTS " + neighborTable);
		onCreate(db);
	}

}
