package com.cooshare.os.kernel.db;

import com.cooshare.os.kernel.objects.*;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocalDataEngineManager {
	
	private LocalDataEngineHelper helper;
	private SQLiteDatabase db;
	
	
	
	public LocalDataEngineManager(Context context) {
		
		helper = new LocalDataEngineHelper(context);
		db = helper.getWritableDatabase();
		
	}
	
	
	
	public void insert_EP(List<EP> objects) {
        
		db.beginTransaction();
		
        try {
        	for (EP object : objects) {
        		db.execSQL("INSERT INTO EP VALUES(?, ?, ?, ?, ?, ?, ?)", new Object[]{object.EP_ID, object.EP_MAC_ID, object.EP_PRODUCTID, object.EP_TYPEID, object.EP_USERDEFINED_ALIAS, object.HCCU_ID, object.EP_IP});
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_EP_EVENTS(List<EP_EVENTS> objects) {
        db.beginTransaction();
        
        try {
        	for (EP_EVENTS object : objects) {
        		
        		db.execSQL("INSERT INTO EP_EVENTS VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
        				new Object[]{object.ACTIVERANGE_IFCONTAINBOUNDARY, object.ACTIVERANGE_MAX, object.ACTIVERANGE_MIN, object.EP_ID, object.EP_PROPERTY_ID, object.EVENT_ID, object.ISACTIVE, object.OPERATION_TYPE_ID, object.TARGET, object.COMMAND});
        		
        	
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_EP_TRANS(List<EP_TRANS> objects) {
        db.beginTransaction();
        try {
        	for (EP_TRANS object : objects) {
        		db.execSQL("INSERT INTO EP_TRANS VALUES(?, ?, ?)", 
        				new Object[]{object.MISC, object.TARGET_EP_ID, object.TRIGGER_TYPE_ID});
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_HCCU_REQ_LOGS(List<HCCU_REQ_LOGS> objects) {
        db.beginTransaction();
        try {
        	for (HCCU_REQ_LOGS object : objects) {
        		db.execSQL("INSERT INTO  HCCU_REQ_LOGS VALUES(?, ?, ?, ?, ?)", 
        				new Object[]{object.REQUEST_DATETIME, object.REQUEST_HCCU_ID, object.REQUEST_IP, object.REQUEST_RESULT_ID, object.REQUEST_TYPE_ID});
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_HCCU(List<HCCU> objects) {
        db.beginTransaction();
        try {
        	for (HCCU object : objects) {
        		db.execSQL("INSERT INTO HCCU VALUES(?, ?, ?, ?, ?)", 
        				new Object[]{object.HCCU_ACC_STATUS, object.HCCU_ID, object.HCCU_MAC_ID, object.HCCU_MAC_STATUS, object.HCCU_UID});
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_EXEC_ORDER(List<EXEC_ORDER> objects) {
        db.beginTransaction();
        try {
        	for (EXEC_ORDER object : objects) {
        		db.execSQL("INSERT INTO  EXEC_ORDER VALUES(?, ?, ?, ?)", 
        				new Object[]{object.PROP, object.VALUE, object.EP_ID, object.OWNER});
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_EP_PROPC(List<EP_PROPC> objects) {
        db.beginTransaction();
        try {
        	for (EP_PROPC object : objects) {
        		
        		
        		db.execSQL("INSERT INTO EP_PROPC VALUES(?, ?)", 
        				new Object[]{object.EP_ID, object.PROPERTYCOLLECTION});
        		
        		
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	public void insert_EP_PROP(List<EP_PROP> objects) {
        db.beginTransaction();
        try {
        	for (EP_PROP object : objects) {
        		db.execSQL("INSERT INTO  EP_PROP VALUES(?, ?)", 
        				new Object[]{object.EP_PROPERTY_ID, object.EP_PROPERTY_NAME});
        	}
        	db.setTransactionSuccessful();
        } finally {
        	db.endTransaction();
        }
	}
	
	
	
	public void update_EP(String FieldName, String Content, EP Object) {
		ContentValues cv = new ContentValues();
		cv.put(FieldName, Content);
		db.update("EP", cv, "EP_ID = ?", new String[]{String.valueOf(Object.EP_ID)});
	}
	
	public void update_EP_EVENTS(String FieldName, String Content, EP_EVENTS Object) {
		ContentValues cv = new ContentValues();
		cv.put(FieldName, Content);
		db.update("EP_EVENTS", cv, "EVENT_ID = ?", new String[]{String.valueOf(Object.EVENT_ID)});
	}
	
	public void update_HCCU(String FieldName, String Content, HCCU Object) {
		ContentValues cv = new ContentValues();
		cv.put(FieldName, Content);
		db.update("HCCU", cv, "HCCU_ID = ?", new String[]{String.valueOf(Object.HCCU_ID)});
	}
	
	public void update_EP_PROPC(String FieldName, String Content, EP_PROPC Object) {
		ContentValues cv = new ContentValues();
		cv.put(FieldName, Content);
		db.update("EP_PROPC", cv, "EP_ID = ?", new String[]{String.valueOf(Object.EP_ID)});
	}
	
	
	
	public void delete_EP(EP Object) {
		db.delete("EP", "EP_ID = ?", new String[]{String.valueOf(Object.EP_ID)});
	}
	
	public void delete_EP_EVENTS(EP_EVENTS Object) {
		db.delete("EP_EVENTS", "EVENT_ID = ?", new String[]{String.valueOf(Object.EVENT_ID)});
	}
	
	public void delete_EP_EVENTS() {
		db.delete("EP_EVENTS", null, null);
	}
	
	public void delete_EP_TRANS() {
		db.delete("EP_TRANS", null, null);
	}
	
	public void delete_HCCU_REQ_LOGS() {
		db.delete("HCCU_REQ_LOGS", null, null);
	}
	
	public void delete_EXEC_ORDER(EXEC_ORDER Object) {
		db.delete("EXEC_ORDER", "_id = ?", new String[]{String.valueOf(Object._id)});
	}
	
	public void delete_EP_PROPC(EP_PROPC Object) {
		db.delete("EP_PROPC", "EP_ID = ?", new String[]{String.valueOf(Object.EP_ID)});
	}
	
	public void delete_EP_PROP() {
		db.delete("EP_PROP", null, null);
	}
	
	public List<EP> query_EP(String query, String[] SelectionArgs) {
		ArrayList<EP> Objects = new ArrayList<EP>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	EP Object = new EP();
        	Object.EP_ID = c.getInt(c.getColumnIndex("EP_ID"));
        	Object.EP_PRODUCTID = c.getInt(c.getColumnIndex("EP_PRODUCTID"));
        	Object.EP_TYPEID = c.getInt(c.getColumnIndex("EP_TYPEID"));
        	Object.HCCU_ID = c.getInt(c.getColumnIndex("HCCU_ID"));
        	Object.EP_MAC_ID = c.getString(c.getColumnIndex("EP_MAC_ID"));
        	Object.EP_USERDEFINED_ALIAS = c.getString(c.getColumnIndex("EP_USERDEFINED_ALIAS"));
        	Object.EP_IP = c.getString(c.getColumnIndex("EP_IP"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<EP_EVENTS> query_EP_EVENTS(String query, String[] SelectionArgs) {
		ArrayList<EP_EVENTS> Objects = new ArrayList<EP_EVENTS>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	EP_EVENTS Object = new EP_EVENTS();
        	Object.ACTIVERANGE_IFCONTAINBOUNDARY = c.getInt(c.getColumnIndex("ACTIVERANGE_IFCONTAINBOUNDARY"));
        	Object.ACTIVERANGE_MAX = c.getString(c.getColumnIndex("ACTIVERANGE_MAX"));
        	Object.ACTIVERANGE_MIN = c.getString(c.getColumnIndex("ACTIVERANGE_MIN"));
        	Object.EP_ID = c.getInt(c.getColumnIndex("EP_ID"));
        	Object.EP_PROPERTY_ID = c.getInt(c.getColumnIndex("EP_PROPERTY_ID"));
        	Object.EVENT_ID = c.getInt(c.getColumnIndex("EVENT_ID"));
        	Object.ISACTIVE = c.getInt(c.getColumnIndex("ISACTIVE"));
        	Object.OPERATION_TYPE_ID = c.getInt(c.getColumnIndex("OPERATION_TYPE_ID"));
        	Object.TARGET = c.getFloat(c.getColumnIndex("TARGET"));
        	Object.COMMAND = c.getString(c.getColumnIndex("COMMAND"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<EP_TRANS> query_EP_TRANS(String query, String[] SelectionArgs) {
		ArrayList<EP_TRANS> Objects = new ArrayList<EP_TRANS>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	EP_TRANS Object = new EP_TRANS();
        	Object.MISC = c.getString(c.getColumnIndex("MISC"));
        	Object.TARGET_EP_ID = c.getInt(c.getColumnIndex("TARGET_EP_ID"));
        	Object.TRANSACTION_ID = c.getInt(c.getColumnIndex("TRANSACTION_ID"));
        	Object.TRIGGER_TYPE_ID = c.getInt(c.getColumnIndex("TRIGGER_TYPE_ID"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<HCCU_REQ_LOGS> query_HCCU_REQ_LOGS(String query, String[] SelectionArgs) {
		ArrayList<HCCU_REQ_LOGS> Objects = new ArrayList<HCCU_REQ_LOGS>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	HCCU_REQ_LOGS Object = new HCCU_REQ_LOGS();
        	Object.REQUEST_DATETIME = c.getString(c.getColumnIndex("REQUEST_DATETIME"));
        	Object.REQUEST_HCCU_ID = c.getInt(c.getColumnIndex("REQUEST_HCCU_ID"));
        	Object.REQUEST_IP = c.getString(c.getColumnIndex("REQUEST_IP"));
        	Object.REQUEST_RESULT_ID = c.getInt(c.getColumnIndex("REQUEST_RESULT_ID"));
        	Object.REQUEST_TYPE_ID = c.getInt(c.getColumnIndex("REQUEST_TYPE_ID"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<HCCU> query_HCCU(String query, String[] SelectionArgs) {
		ArrayList<HCCU> Objects = new ArrayList<HCCU>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	HCCU Object = new HCCU();
        	Object.HCCU_ACC_STATUS = c.getInt(c.getColumnIndex("HCCU_ACC_STATUS"));
        	Object.HCCU_ID = c.getInt(c.getColumnIndex("HCCU_ID"));
        	Object.HCCU_MAC_ID = c.getString(c.getColumnIndex("HCCU_MAC_ID"));
        	Object.HCCU_MAC_STATUS = c.getInt(c.getColumnIndex("HCCU_MAC_STATUS"));
        	Object.HCCU_UID = c.getString(c.getColumnIndex("HCCU_UID"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<EXEC_ORDER> query_EXEC_ORDER(String query, String[] SelectionArgs) {
		ArrayList<EXEC_ORDER> Objects = new ArrayList<EXEC_ORDER>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	EXEC_ORDER Object = new EXEC_ORDER();
        	Object._id = c.getInt(c.getColumnIndex("_id"));
        	Object.PROP = c.getString(c.getColumnIndex("PROP"));
        	Object.VALUE = c.getString(c.getColumnIndex("VALUE"));
        	Object.EP_ID = c.getInt(c.getColumnIndex("EP_ID"));
        	Object.OWNER = c.getInt(c.getColumnIndex("OWNER"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<EP_PROPC> query_EP_PROPC(String query, String[] SelectionArgs) {
		ArrayList<EP_PROPC> Objects = new ArrayList<EP_PROPC>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	EP_PROPC Object = new EP_PROPC();
        	Object.EP_ID = c.getInt(c.getColumnIndex("EP_ID"));
        	Object.PROPERTYCOLLECTION = c.getString(c.getColumnIndex("PROPERTYCOLLECTION"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	public List<EP_PROP> query_EP_PROP(String query, String[] SelectionArgs) {
		ArrayList<EP_PROP> Objects = new ArrayList<EP_PROP>();
		Cursor c = queryTheCursor(query, SelectionArgs);
        while (c.moveToNext()) {
        	EP_PROP Object = new EP_PROP();
        	Object.EP_PROPERTY_ID = c.getInt(c.getColumnIndex("EP_PROPERTY_ID"));
        	Object.EP_PROPERTY_NAME = c.getString(c.getColumnIndex("EP_PROPERTY_NAME"));
        	Objects.add(Object);
        }
        c.close();
        return Objects;
	}
	
	
	public Cursor queryTheCursor(String query, String[] SelectionArgs) {
        Cursor c = db.rawQuery(query, SelectionArgs);
        return c;
	}
	
	public void closeDB() {
		db.close();
	}
}

