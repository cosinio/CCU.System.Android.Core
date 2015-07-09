package com.cooshare.os.kernel;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.SystemClock;
import android.content.*;
import android.os.Handler;




public class Shell extends Activity {

	private Handler handler = new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.shell);
	    

	      	Intent mintent = new Intent(this, Alarmreceiver.class);    
        	mintent.setAction("arui.alarm.action");    
  
        	PendingIntent sender = PendingIntent.getBroadcast(this, 0,    
                  mintent, 0);    
        
	        long firstime = SystemClock.elapsedRealtime();    
	        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);    
	        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,60 * 1000, sender);   
      
	
	    
	    handler.removeCallbacks(updateTimeTask);
	    handler.postDelayed(updateTimeTask, 1000);
	    
	}
	
	private void updateTime() {
		
		
		  			    
	}
	
	private Runnable updateTimeTask = new Runnable() {
		public void run() {
		updateTime();
		handler.postDelayed(this, 5000);
		}
	};
	
	

}