package com.cooshare.os.kernel;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.cooshare.os.kernel.helper.CommandParser;
import com.friendlyarm.AndroidSDK.HardwareControler;

import android.content.Context;
import android.util.Log;

public class RecorderTask2 extends TimerTask {

	Context context;
	String apn="";
	int T = connmgr.recorder2_T;
	ArrayList<String> commandbox = null;
    
	
	public RecorderTask2(Context c){
		
		context =c;
	    
	}
	
	
	public void run(){	
		
		try {
			
				connmgr.LastUpdateTime2 = Calendar.getInstance().getTime(); 
		
				
	            if(connmgr.secondsFromLastTrailPoint2%T==0){   	 
	            	
	         	   connmgr.secondsFromLastTrailPoint2=0;
	         	   
	         	 
	         	   /*
	         	    * Read Data From SerialPort-1 (R)
	         	    */
	         	   

	     		   try
	     		   {
	     			   
	     			   
	     			   if(connmgr.SerialPortFileDescriptionFlag_1!=-1){
	     				   
	     				   if(HardwareControler.select(connmgr.SerialPortFileDescriptionFlag_1, 5, 20)==1){
	     					   
	     					 byte[] buf = new byte[100];
	     					 int n = HardwareControler.read(connmgr.SerialPortFileDescriptionFlag_1, buf, buf.length);
	     					 
	     					 if(n!=0&&n!=-1){
	     						 
	     						 String strtmmp = null;
			     				 try {
			     						strtmmp = new String(buf,"UTF8");
			     						buf = null;
			     				 } catch (UnsupportedEncodingException e) {}
			     				 
			     				 commandbox = CommandParser.GetCommand(strtmmp);
			     				 for(int x=0; x<commandbox.size();x++){
			     					 
			     					 Log.w("RecorderTask2_Command:",  commandbox.get(x).toString().trim());
			     					 
			     					 if(!CommandParser.ProcessSingleCommand(commandbox.get(x).toString().trim())){
			     						 Log.w("RecorderTask2_Command:", "Unrecognized Command.");
			     					 }else{
			     						 Log.w("RecorderTask2_Command:", "Recognized Command.");
			     					 }
			     				 }	     	
	     					 }
	     					 
	     				   }
	     				   
	     				   
	     			   }else{
	     				   
	     				   try{   
				     		 
	     					  connmgr.SerialPortFileDescriptionFlag_1 = HardwareControler.openSerialPort(connmgr.SerialPortDevName_1, 115200, 8, 1);
				     				  
				     	   }catch(Exception ev){
				     		
				     		   Log.w("RecorderTask2", "Exception occured while opening SerialPort.");
				     	   }
	     			   }
	     			   
	               	
	     		   }
	     		   catch(Exception ev){ }   	 
	         	   
	            }
	            	               
	       
	            
	           
	 
			
		} catch (Exception e) {
			core.recorderTask2 = null;			
		}finally{
			
			 connmgr.secondsFromLastTrailPoint2++;
		}	
		
	}
	

	
	
}
