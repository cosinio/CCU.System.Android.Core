package com.cooshare.os.kernel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class AutoSrv extends BroadcastReceiver
{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			
				 if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) { 
			
			 
					Intent mintent = new Intent(context, Alarmreceiver.class);    
			        mintent.setAction("arui.alarm.action");    
			    
			        PendingIntent sender = PendingIntent.getBroadcast(context, 0, mintent, 0);    
			          
		            long firstime = SystemClock.elapsedRealtime();    
			        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);    
			        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 60 * 1000, sender);   
			           
			        Log.w("AutoSrv", "BOOT_COMPLETED / ALARM MANAGER START.");
			
			 }
					
		}
}




