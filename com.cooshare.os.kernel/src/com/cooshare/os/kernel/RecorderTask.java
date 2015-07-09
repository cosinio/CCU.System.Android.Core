package com.cooshare.os.kernel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.*;
import android.util.Log;

import com.cooshare.os.kernel.helper.Base64;
import com.cooshare.os.kernel.helper.RequestLogs;
import com.cooshare.os.kernel.helper.SecurityKey;
import com.cooshare.os.kernel.helper.StringUtil;
import com.cooshare.os.kernel.objects.EP;
import com.cooshare.os.kernel.objects.EP_EVENTS;
import com.cooshare.os.kernel.objects.EP_PROP;
import com.cooshare.os.kernel.objects.EP_TRANS;
import com.cooshare.os.kernel.objects.EXEC_ORDER;
import com.cooshare.os.kernel.objects.HCCU;
import com.cooshare.os.kernel.objects.HCCU_REQ_LOGS;

public class RecorderTask extends TimerTask {
	
	int T = connmgr.recorder1_T;
	
	
    Context context;
    
    NetworkInfo nf = null;
    List<EP> ep_Objects = null;
    List<EP_EVENTS> ev_Objects = null;
    List<EXEC_ORDER> eo_Objects = null; 
    List<EP_PROP> epr_Objects = null;
    List<EP_TRANS> ept_Objects = null;
    List<HCCU_REQ_LOGS> req_Objects = null;
    List<HCCU> hccu_Objects = null;
    List<EP> ep_Objects2 = null;
 
    String apn="";
     
	public RecorderTask(Context c){
			
				context =c;	   	  
				
				ep_Objects = new ArrayList<EP>();
				ev_Objects = new ArrayList<EP_EVENTS>();
				eo_Objects = new ArrayList<EXEC_ORDER>();
				epr_Objects = new ArrayList<EP_PROP>();
				ept_Objects = new ArrayList<EP_TRANS>();
				req_Objects = new ArrayList<HCCU_REQ_LOGS>();
	 }
	
	public String initAPN(Context context) {   
	        
	         ConnectivityManager manager = (ConnectivityManager) context   
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);   
	        
	         NetworkInfo info = manager.getActiveNetworkInfo();   
	         
	         connmgr.CM = manager;
	         connmgr.NIF = info;
	         
	      
	         if (info != null) {   
	             apn = info.getExtraInfo();   
	             
	             if (apn == null) {   
	                 apn = "";   
	         }   
	     } else {   
	         apn = "Empty_Home";   
	     }
	     Log.w("=======================APN:", apn);
	         return apn;	         
	  }  
    		 
	@Override
	public void run(){	
		
		try {
			connmgr.LastUpdateTime = Calendar.getInstance().getTime(); 
			
			if(connmgr.CM==null) connmgr.set_APN(initAPN(context));
			nf = connmgr.CM.getActiveNetworkInfo(); 
			connmgr.NIF = nf;
		
			InitialData();
			
			if(nf!=null){
				
	            if(connmgr.secondsFromLastTrailPoint%T==0){   	     
	         	  
	              connmgr.secondsFromLastTrailPoint=0;
	         	   
	         	  ep_Objects = new ArrayList<EP>();
	         	  ep_Objects = connmgr.dbmgr.query_EP("SELECT * FROM EP WHERE 1 = ?", new String[]{"1"});
	         	  
	         	  if(ep_Objects.size()!=0){
	         		  
	         		 for(int x=0; x<ep_Objects.size();x++){
	         			 
	         			Sync_EXCE_ORDER(String.valueOf(ep_Objects.get(x).EP_ID));
	         			
	         		 }
	         	  }
	         	  
	         	  
	         	 Sync_EP_EVENTS();
		         
	         	 if(connmgr.secondsFromLastTrailPoint%(2*T)==0){   	
				         	 
	         		
	         		 		 Sync_PROP();
					         Sync_HCCU_EP_Info();
	         	 }
		          
	         	 
		          ept_Objects = connmgr.dbmgr.query_EP_TRANS("SELECT * FROM EP_TRANS", null);
		          
		          Log.w("RECORDERTASK", "1s");
		          
		          if(ept_Objects.size()!=0){
		        	  
		        	  Log.w("RECORDERTASK", "2s");
		        	  
		        	  for(int x=0; x<ept_Objects.size();x++){
		        		  
		        		  
		        		  Sync_TRANS(String.valueOf(ept_Objects.get(x).TARGET_EP_ID),
		        				  String.valueOf(ept_Objects.get(x).TRIGGER_TYPE_ID),
		        				  ept_Objects.get(x).MISC);
		        				  
		        	  }
		        	  Log.w("RECORDERTASK", "3s");
		        	  
		        	  connmgr.dbmgr.delete_EP_TRANS();
		        	  
		        	  Log.w("RECORDERTASK", "4s");
		          }
		          
		          req_Objects = connmgr.dbmgr.query_HCCU_REQ_LOGS("SELECT * FROM HCCU_REQ_LOGS", null);
		          
		          Log.w("RECORDERTASK", "5s");
		          
		          
		          if(req_Objects.size()!=0){
		        	  
		        	  Log.w("RECORDERTASK", "AAAAAAAAAAAAA");
		        	  for(int x=0; x<req_Objects.size();x++){
		        		  
		        		  Log.w("RECORDERTASK", "BBBB");
		        		  
		        		  Sync_REQ_Logs(String.valueOf(req_Objects.get(x).REQUEST_HCCU_ID),
		        				  req_Objects.get(x).REQUEST_IP,
		        				  req_Objects.get(x).REQUEST_DATETIME,
		        				  String.valueOf(req_Objects.get(x).REQUEST_TYPE_ID),
		        				  String.valueOf(req_Objects.get(x).REQUEST_RESULT_ID));
		        			
		        		  Log.w("RECORDERTASK", "CCCC");
		        		  
		        		  
		        	  }
		        	  connmgr.dbmgr.delete_HCCU_REQ_LOGS();
		        	  
		          }
		          
		          
		          Log.w("RECORDERTASK", "EXECUTING END");
	         	   
	            }
	            
	          
		    	
			}
			
			Log.w("RECORDERTASK", "LOOPING END");
			
		} catch (Exception e) {
			core.recorderTask = null;			
		}finally{
			
			  connmgr.secondsFromLastTrailPoint++;
		}	
		
	}
	
	public void InitialData(){
		
		if(connmgr.AutoSelfCheck){
			new GetInitialData().start();	
			connmgr.AutoSelfCheck = false;
		}
		
	}
	
	public void Sync_HCCU_EP_Info(){
		
		String returns = "";
		String requrl = connmgr.HCCU_collector+"HCCU_EP_Get_Info";
		String rawparameter = "HCCU_ID="+Base64.Encode_Wall(connmgr.HostPn);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;
		
		Log.w("RECORDERTASK_SYNC_HCCU_EP_INFO", rawparameter);
		
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			if(returns.indexOf('^')!=-1){
				
				String[] a = StringUtil.split(returns, '^');
				
				if(a[0]=="-4"||a[1]=="-4")	RequestLogs.AddNetworkErrorLogs(3, 1);
				if(a[0]=="-2"||a[1]=="-2")	RequestLogs.AddNetworkErrorLogs(5, 1);
				if(a[0]=="-1"||a[1]=="-1")	RequestLogs.AddNetworkErrorLogs(2, 1);
				
				
				String[] HCCU_S = StringUtil.split(a[0], ',');
				
				hccu_Objects = connmgr.dbmgr.query_HCCU("SELECT * FROM HCCU", null);
				
				
				if(hccu_Objects.size()!=0){
					
					connmgr.dbmgr.update_HCCU("HCCU_ACC_STATUS", HCCU_S[4], hccu_Objects.get(0));
					connmgr.dbmgr.update_HCCU("HCCU_MAC_STATUS", HCCU_S[3], hccu_Objects.get(0));
					
				}else{
					
					int mac_status = 0;
					int acc_status = 0;
					
					if(HCCU_S[3].trim().equals("")){	
						mac_status = 0;
					}else{
						mac_status = Integer.parseInt(HCCU_S[3]);
					}
					
					if(HCCU_S[4].trim().equals("")){	
						acc_status = 0;
					}else{
						acc_status = Integer.parseInt(HCCU_S[4]);
					}
					
					
					
					HCCU hccu_Object = new HCCU(Integer.parseInt(HCCU_S[0]),HCCU_S[1],HCCU_S[2],mac_status,acc_status);
					hccu_Objects.add(hccu_Object);
					connmgr.dbmgr.insert_HCCU(hccu_Objects);
				}
				
				
				
	
				
				String[] EP_S = StringUtil.split(a[1], '|');
				String[] i_S = null;
				
				for(int x=0;x<EP_S.length;x++){
					
					if(!EP_S[x].equals("")){
						

						i_S = StringUtil.split(EP_S[x], ',');
						
						EP ep_Object = new EP(Integer.parseInt(i_S[0]),Integer.parseInt(i_S[1]),i_S[2],Integer.parseInt(i_S[3]),Integer.parseInt(i_S[4]),i_S[5],"");
						
						ep_Objects = connmgr.dbmgr.query_EP("SELECT * FROM EP WHERE EP_ID = ?", new String[]{String.valueOf(ep_Object.EP_ID)});
						
						if(ep_Objects.size()!=0){
							
							connmgr.dbmgr.update_EP("EP_TYPEID", String.valueOf(ep_Object.EP_TYPEID), ep_Object);
							connmgr.dbmgr.update_EP("EP_USERDEFINED_ALIAS", String.valueOf(ep_Object.EP_USERDEFINED_ALIAS), ep_Object);
							connmgr.dbmgr.update_EP("EP_PRODUCTID", String.valueOf(ep_Object.EP_PRODUCTID), ep_Object);
						}
						
						
					}
				}
				
			}
			
			
		}
		
	}
	
	public void Sync_EXCE_ORDER(String _EP_ID){
		
		String returns = "";
		String requrl = connmgr.EXECORDER_collector+"EXEC_ORDER_Get";
		String rawparameter = "EP_ID="+Base64.Encode_Wall(_EP_ID);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;			
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			
			if(returns=="-4")	RequestLogs.AddNetworkErrorLogs(3, 1);
			if(returns=="-2")	RequestLogs.AddNetworkErrorLogs(5, 1);
			if(returns=="-1")	RequestLogs.AddNetworkErrorLogs(2, 1);
			
		
			if(returns.indexOf('|')!=-1){
				
				String[] s = StringUtil.split(returns, '|');
				String[] m = null;
				eo_Objects = new ArrayList<EXEC_ORDER>();
				
				for(int x=0;x<s.length;x++){
					
					if(s[x]!=""){
						
						m = StringUtil.split(s[x], ',');
						EXEC_ORDER Object = new EXEC_ORDER(m[1],m[2],0,Integer.parseInt(m[0]),2);
						eo_Objects.add(Object);
						
					}
				}
				
				connmgr.dbmgr.insert_EXEC_ORDER(eo_Objects);
			}
			
			
		}
	}
	
	public void Sync_EP_EVENTS(){
		
		String returns = "";
		String requrl = connmgr.EVENTS_collector+"EVENTS_Sync";
		String rawparameter = "HCCU_ID="+Base64.Encode_Wall(connmgr.HostPn);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;			
		
		Log.w("RECORDERTASK", rawparameter);
		
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			
			if(returns=="-4")	RequestLogs.AddNetworkErrorLogs(3, 1);
			if(returns=="-2")	RequestLogs.AddNetworkErrorLogs(5, 1);
			
			
			connmgr.dbmgr.delete_EP_EVENTS();
			
			
			if(returns.indexOf('^')!=-1){
				
				ev_Objects =  new ArrayList<EP_EVENTS>();
				String[] c = StringUtil.split(returns, '^');
				String[] content = null;
				
				
				for(int x=0; x<c.length; x++){
					
					if(!c[x].trim().equals("")){
						
						
						content = StringUtil.split(c[x], '*');
						EP_EVENTS Object = new EP_EVENTS(Integer.parseInt(content[0]),
								Integer.parseInt(content[1]),
								Float.parseFloat(content[2]),
								Integer.parseInt(content[3]),
								Integer.parseInt(content[4]),
								content[5],
								content[6],
								Integer.parseInt(content[7]),
								Integer.parseInt(content[8]),
								content[9]
								);
						

						
						ev_Objects.add(Object);
						
						
					}
					
				}
				
				
				connmgr.dbmgr.insert_EP_EVENTS(ev_Objects);
				
				
			}
		
		}
		
	}
	
	public void Sync_PROP(){
		
		
		String returns = "";
		String requrl = connmgr.PROP_collector+"EP_PROPERTYFACT_Sync";
		String rawparameter = "HCCU_ID="+Base64.Encode_Wall(connmgr.HostPn);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;			
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			
			if(returns.indexOf('|')!=-1){
			
				epr_Objects = new ArrayList<EP_PROP>();
				String[] c =  StringUtil.split(returns, '|');
				String[] d = null;
				
				for(int x=0;x<c.length;x++){
					
					
					if(!c[x].equals("")){
						
						d = StringUtil.split(c[x], ',');
						
						EP_PROP epr_Object = new EP_PROP(Integer.parseInt(d[0]),d[1]);
						epr_Objects.add(epr_Object);
						
					}
					
				}
				
				
				connmgr.dbmgr.insert_EP_PROP(epr_Objects);
				
			}
			
			
		}
	}
	
	public void Sync_TRANS(String _Target_EP_ID, String _Trigger_Type_ID,String _Misc){
		
		String returns = "";
		String requrl = connmgr.TRANS_collector+"TRANS_Add";
		String rawparameter = "TARGET_EP_ID="+Base64.Encode_Wall(_Target_EP_ID)
				+"&TRIGGER_TYPE_ID="+Base64.Encode_Wall(_Trigger_Type_ID)
				+"&MISC="+Base64.Encode_Wall(_Misc);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;			
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			if(returns=="-4")	RequestLogs.AddNetworkErrorLogs(3, 1);
			if(returns=="-2")	RequestLogs.AddNetworkErrorLogs(5, 1);
			if(returns=="-1")	RequestLogs.AddNetworkErrorLogs(2, 1);
			
		}
		
	}
	
	public void Sync_REQ_Logs(String _REQUEST_HCCU_ID,String _REQUEST_IP,String _REQUEST_DATETIME, String _REQUEST_TYPE_ID,String _REQUEST_RESULT_ID){
		
		
		String returns = "";
		String requrl = connmgr.REQUESTLOGS_collector+"REQUESTLOGS_Add";
		String rawparameter = "REQUEST_HCCU_ID="+Base64.Encode_Wall(_REQUEST_HCCU_ID)
				+"&REQUEST_IP="+Base64.Encode_Wall(_REQUEST_IP)
				+"&REQUEST_DATETIME="+Base64.Encode_Wall(_REQUEST_DATETIME)
				+"&REQUEST_TYPE_ID="+Base64.Encode_Wall(_REQUEST_TYPE_ID)
				+"&REQUEST_RESULT_ID="+Base64.Encode_Wall(_REQUEST_RESULT_ID);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;			
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			if(returns=="-4")	RequestLogs.AddNetworkErrorLogs(3, 1);
			if(returns=="-2")	RequestLogs.AddNetworkErrorLogs(5, 1);
			if(returns=="-1")	RequestLogs.AddNetworkErrorLogs(2, 1);
			
		}
		
	}
	
	private class GetInitialData extends Thread {

    	public void run(){            		
    		try{
    			
    		} catch (Exception e) {}

    	}			   
    
	}
	
}

