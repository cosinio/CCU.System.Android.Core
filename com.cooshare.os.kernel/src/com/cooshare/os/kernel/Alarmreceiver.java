package com.cooshare.os.kernel; 
import java.util.ArrayList;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 


public class Alarmreceiver extends BroadcastReceiver {     
	ActivityManager myManager;  
	ArrayList<RunningAppProcessInfo> runningProcess;     
	@Override    
	public void onReceive(Context context, Intent intent) {   
		if (intent.getAction().equals("arui.alarm.action")) {    

              Intent is = new Intent(context, core.class);	      		
              context.startService(is);      			        	
              }        	                 
		}            
	}  
