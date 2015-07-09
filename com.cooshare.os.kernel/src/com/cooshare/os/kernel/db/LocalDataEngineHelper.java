package com.cooshare.os.kernel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDataEngineHelper extends SQLiteOpenHelper{

	 private static final String DATABASE_NAME = "COS_HCCU.db";  
	 private static final int DATABASE_VERSION = 1;  
	      
	
	 public LocalDataEngineHelper(Context context) {  
	        
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	    }  
	 
	    @Override  
	    public void onCreate(SQLiteDatabase db) {  
	        db.execSQL("CREATE TABLE IF NOT EXISTS EP" +  
	                "(EP_ID INTEGER, EP_MAC_ID VARCHAR, EP_PRODUCTID INTEGER, EP_TYPEID INTEGER, EP_USERDEFINED_ALIAS VARCHAR, HCCU_ID INTEGER, EP_IP VARCHAR)");
	        
	        db.execSQL("CREATE TABLE IF NOT EXISTS EP_EVENTS" +
	        		"(ACTIVERANGE_IFCONTAINBOUNDARY INTEGER, ACTIVERANGE_MAX VARCHAR, ACTIVERANGE_MIN VARCHAR, EP_ID INTEGER, EP_PROPERTY_ID INTEGER, EVENT_ID INTEGER, ISACTIVE INTEGER, OPERATION_TYPE_ID INTEGER, TARGET FLOAT, COMMAND VARCHAR)");
	    
	        db.execSQL("CREATE TABLE IF NOT EXISTS EP_TRANS" +
	        		"(TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,MISC VARCHAR,TARGET_EP_ID INTEGER,TRIGGER_TYPE_ID INTEGER)");
	        
	        db.execSQL("CREATE TABLE IF NOT EXISTS HCCU_REQ_LOGS" +
	        		"(REQUEST_DATETIME VARCHAR,REQUEST_HCCU_ID INTEGER,REQUEST_IP VARCHAR,REQUEST_RESULT_ID INTEGER, REQUEST_TYPE_ID INTEGER)");
	        
	        db.execSQL("CREATE TABLE IF NOT EXISTS HCCU" +
	        		"(HCCU_ACC_STATUS INTEGER, HCCU_ID INTEGER, HCCU_MAC_ID VARCHAR, HCCU_MAC_STATUS INTEGER, HCCU_UID VARCHAR)");
	        
	        db.execSQL("CREATE TABLE IF NOT EXISTS EXEC_ORDER" +
	        		"(_id INTEGER PRIMARY KEY AUTOINCREMENT, PROP VARCHAR, VALUE VARCHAR, EP_ID INTEGER, OWNER INTEGER)");
	    
	        db.execSQL("CREATE TABLE IF NOT EXISTS EP_PROPC" +
	        		"(EP_ID INTEGER, PROPERTYCOLLECTION VARCHAR)");
	        
	        db.execSQL("CREATE TABLE IF NOT EXISTS EP_PROP" +
	        		"(EP_PROPERTY_ID INTEGER, EP_PROPERTY_NAME VARCHAR)");
	        
	    }  
	  
	    @Override  
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
	       
	    }  
	 
}
