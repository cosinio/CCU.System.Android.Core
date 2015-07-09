package com.cooshare.os.kernel;

import com.cooshare.os.kernel.db.*;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import java.util.*;
import android.database.ContentObserver;
import com.friendlyarm.AndroidSDK.HardwareControler;

public class core extends Service {

	private Timer timer = new Timer();
	private Timer timer2 = new Timer();
	private Timer timer3 = new Timer();
	
	Context c;
	
    public static final String TAG = "CoreService";
    private ContentObserver mObserver = null;

    private String apn="";
   
    private boolean ifstartsrv = false;
    private boolean ifstartsrv2 = false;
    private boolean ifstartsrv3 = false;
    
	private long d1;
	private long d2;
	private long d3;
	private long d4;
	private long d5;
	private long d6;

	public static RecorderTask recorderTask = null;
	public static RecorderTask2 recorderTask2 = null;
	public static RecorderTask3 recorderTask3 = null;
	

	@Override
	public void onCreate() {
		
		
		c = this.getApplicationContext();
		
		
		/*
		 * Initial Global static values.
		 */
		connmgr.set_APN(initAPN(this));
		connmgr.AutoSelfCheck = true;
		if(connmgr.dbmgr!=null) connmgr.dbmgr.closeDB();
	    connmgr.dbmgr = new LocalDataEngineManager(c);	      
	    
	     
	    /*
	     *  Initial the Serial Port.
	     */
	    
	 	try{
	 		
	 		  connmgr.SerialPortFileDescriptionFlag_1 = HardwareControler.openSerialPort(connmgr.SerialPortDevName_1, 115200, 8, 1);	 			   
			  if(connmgr.SerialPortFileDescriptionFlag_1 == -1)  Log.w("Inital", "SerialPort " +connmgr.SerialPortDevName_1+" open failed.");
	 		   
	 	}catch(Exception ev){
	 		
	 		  Log.w("Inital", "Exception occured while opening SerialPort "+connmgr.SerialPortDevName_1);
	 	}
	 	
		try{
	 		
	 		  connmgr.SerialPortFileDescriptionFlag_2 = HardwareControler.openSerialPort(connmgr.SerialPortDevName_2, 115200, 8, 1);	 			   
			  if(connmgr.SerialPortFileDescriptionFlag_2 == -1)  Log.w("Inital", "SerialPort " +connmgr.SerialPortDevName_2+" open failed.");
	 		   
	 	}catch(Exception ev){
	 		
	 		  Log.w("Inital", "Exception occured while opening SerialPort "+connmgr.SerialPortDevName_2);
	 	}
	    
    		
	}
   
	public String initAPN(Context context) {   
	        
	        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
	        NetworkInfo info = manager.getActiveNetworkInfo();   
	        
	        connmgr.CM = manager;
	        connmgr.NIF = info;
	        
	        
	        if (info != null) {   
	            apn = info.getExtraInfo();   
	            
	            if (apn == null) {   
	                apn = "";   
	            }   
	        } else {   
	            apn = "Empty_Core";   
	        }
	        return apn;
	        
	    }  
	 
	public int onStartCommand(Intent intent, int flags, int startId) { 
		 
		
		
		 if(connmgr.LastUpdateTime==null){
			 ifstartsrv = true;
		 }else{	
			 
			 d1 = Calendar.getInstance().getTime().getTime(); 
			 d2 = connmgr.LastUpdateTime.getTime();
			 
			 if(d1-d2>=connmgr.recorder1_T*1000){				 
				 ifstartsrv = true;
			 }else{				 
				 ifstartsrv = false;
			 }			 
		 }
		 
		 if(connmgr.LastUpdateTime2==null){
			 ifstartsrv2 = true;
		 }else{	
			 
			 d3 = Calendar.getInstance().getTime().getTime(); 
			 d4 = connmgr.LastUpdateTime2.getTime();
			 
			 if(d3-d4>=connmgr.recorder2_T*1000){				 
				 ifstartsrv2 = true;
			 }else{				 
				 ifstartsrv2 = false;
			 }			 
		 }
		 
		 if(connmgr.LastUpdateTime3==null){
			 ifstartsrv3 = true;
		 }else{	
			 
			 d5 = Calendar.getInstance().getTime().getTime(); 
			 d6 = connmgr.LastUpdateTime3.getTime();
			 
			 if(d5-d6>=connmgr.recorder3_T*1000){				 
				 ifstartsrv3 = true;
			 }else{				 
				 ifstartsrv3 = false;
			 }			 
		 }
		
		 
		 
		 
		 if(ifstartsrv){
			 	
					super.onStart(intent, startId);
					if(recorderTask == null){  recorderTask = new RecorderTask(c);  timer.scheduleAtFixedRate(recorderTask, 1000, 1000);  }
		 }
		 
		 if(ifstartsrv2){
			 	
					super.onStart(intent, startId);
					if(recorderTask2 == null){ recorderTask2 = new RecorderTask2(c); timer2.scheduleAtFixedRate(recorderTask2, 1000, 1000); }
		 }
		 
		 if(ifstartsrv3){
			 	
					super.onStart(intent, startId);
					if(recorderTask3 == null){ recorderTask3 = new RecorderTask3(c); timer3.scheduleAtFixedRate(recorderTask3, 1000, 1000); }
		 }
		 
		 return START_REDELIVER_INTENT;  
	 } 
	
	@Override
	public void onDestroy()
	{
		this.getContentResolver().unregisterContentObserver(mObserver);
		super.onDestroy();
		
		if(timer!=null) timer.cancel();		
		if(timer2!=null) timer2.cancel();
		if(timer3!=null) timer3.cancel();
		
		if(connmgr.dbmgr!=null) connmgr.dbmgr.closeDB();
		
	}
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
