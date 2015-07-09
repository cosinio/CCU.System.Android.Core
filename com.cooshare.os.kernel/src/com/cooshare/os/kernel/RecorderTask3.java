package com.cooshare.os.kernel;

import java.util.*;

import com.cooshare.os.kernel.objects.EP;
import com.cooshare.os.kernel.objects.EP_TRANS;
import com.cooshare.os.kernel.objects.EXEC_ORDER;
import com.friendlyarm.AndroidSDK.HardwareControler;

import android.content.Context;
import android.util.Log;

public class RecorderTask3 extends TimerTask {

	Context context;
	int T = connmgr.recorder3_T;
	EP_TRANS et_Object = null;
	List<EP_TRANS> et_Objects = null;
	ArrayList<EP> Objects = null;
	
	public RecorderTask3(Context c){
		
		context =c;
		
	}
	
	public void run(){	
		
		try {
			connmgr.LastUpdateTime3 = Calendar.getInstance().getTime(); 
		
				
	            if(connmgr.secondsFromLastTrailPoint3%T==0){   	 
	            	
	         	   connmgr.secondsFromLastTrailPoint3=0;
	         	   
	         	 
	         	   /*
	         	    * Read Data From Local DB - ExecOrder
	         	    */
	         	   

	     		   try
	     		   {
	     			   
	     			  String command = "";
	     			  
	     			  Objects = (ArrayList<EP>) connmgr.dbmgr.query_EP("SELECT * FROM EP WHERE 1 = ?", new String[]{"1"});
	     			  
	     			  et_Objects = new ArrayList<EP_TRANS>();
	     			  
	     			  
	     			  for(int x=0; x<Objects.size();x++){
	     				  
	     				  
	     				  ArrayList<EXEC_ORDER> exe_objects = (ArrayList<EXEC_ORDER>) connmgr.dbmgr.query_EXEC_ORDER("SELECT * FROM EXEC_ORDER WHERE EP_ID = ?", new String[]{String.valueOf(((EP)Objects.get(x)).EP_ID)});
	     				  
	     				  if(exe_objects.size()!=0){
		     				  
		     				   if(connmgr.SerialPortFileDescriptionFlag_2!=-1){
		     					   
		     					   for(int y=0;y<exe_objects.size();y++){
		     						   
		     						   command = ((EXEC_ORDER)exe_objects.get(y)).PROP+","+((EXEC_ORDER)exe_objects.get(y)).VALUE+"|";
		     						   
		     						   et_Object = new EP_TRANS(0, ((EXEC_ORDER)exe_objects.get(y)).EP_ID, ((EXEC_ORDER)exe_objects.get(y)).OWNER, "");
		     						   et_Objects.add(et_Object);
		     					   }
		     					   
		     					   command = "/C|"+((EP)Objects.get(x)).EP_IP+"|"+String.valueOf(exe_objects.size())+"|"+command+"*";
		     					   
		     					   
		     					   
		     					   if(HardwareControler.write(connmgr.SerialPortFileDescriptionFlag_2,command.getBytes())==-1){
		     						   
		     						  Log.w("RecorderTask3", "Exception occured while WRITING SerialPort.");
		     					   }
		     					   
		     					  
		     				   }else{
		     					   
		     					   
		     					  try{   
		 				     		 
			     					  connmgr.SerialPortFileDescriptionFlag_2 = HardwareControler.openSerialPort(connmgr.SerialPortDevName_2, 115200, 8, 1);
						     				  
						     	   }catch(Exception ev){
						     		
						     		   Log.w("RecorderTask3", "Exception occured while opening SerialPort.");
						     	   }
		     					   
		     				   }
		     				  
		     				  
		     			  }
	     				  
	     			  }
	     			  
	     			  connmgr.dbmgr.insert_EP_TRANS(et_Objects);
	     			
	     		   }
	     		   catch(Exception ev){ }   	 
	         	   
	         	   
	         	   
	            }
	            	               
	       
	            
	            
	 
			
		} catch (Exception e) {
			core.recorderTask3 = null;			
		}finally{
			
			connmgr.secondsFromLastTrailPoint3++;
		}		
		
	}
	

}
