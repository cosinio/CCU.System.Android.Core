package com.cooshare.os.kernel.helper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.cooshare.os.kernel.connmgr;
import com.cooshare.os.kernel.objects.EP;
import com.cooshare.os.kernel.objects.EP_PROPC;
import com.cooshare.os.kernel.objects.EXEC_ORDER;

public class CommandParser {

	private static String[] commandArray=null;
	private static List<EXEC_ORDER> eo_Objects = null;
	private static List<EP_PROPC> eppropc_Objects = null;
	private static List<EP> ep_Objects = null;
	private static List<EP_PROPC> CheckObjects = null;
	private static List<EP_PROPC> CheckObjects2 = null;
	
	private static String d_mac="";
	private static String d_length="";
	private static String i_mac="";
	private static String i_ip="";
	//private static String i_cluster_id;
	//private static String i_group_id;
	//private static String i_endpoint;
	
	private static int _EP_ID;
	private static String _EP_MAC_ID;
	private static int _EP_PRODUCTID;
	private static int _EP_TYPEID;
	private static String _EP_USERDEFINED_ALIAS;
	private static int _HCCU_ID;
	private static String _EP_IP;
	private static String[] _Command;
	private static boolean IfRepeat = false;
	
	
	
	public static ArrayList<String> GetCommand(String commands){
		
	
		ArrayList<String> ret = new ArrayList<String>();
		
		int end_order = commands.indexOf("*");
		
		if(end_order!=-1){
			
			String[] x = StringUtil.split(commands, '*');
			
			for(int s=0; s<x.length;s++){
				
				if(x[s].trim()!=""){
				
						ret.add(x[s].trim());
				}
			}
			
			return ret;
			
		}else{
			
			return null;
		}
		
		
		
	}
	
	public static int Get_EP_ID_From_MAC(String _d_mac){
		
		ep_Objects =  connmgr.dbmgr.query_EP("select * from EP where EP_MAC_ID = ?", new String[]{_d_mac});
		if(ep_Objects.size()==0)	return -1;
		return ((EP)ep_Objects.get(0)).EP_ID;
		
	}
	
	public static boolean ProcessSingleCommand(String command){
		
		IfRepeat = false;
		commandArray = StringUtil.split(command, '|');
		
		if(commandArray == null) return false;
		
		String prefix = commandArray[0];
		
		
		if(prefix.equals("/D")){
			
			
			if(commandArray.length<4) return false;
			
			
			d_mac = commandArray[1];
			d_length=commandArray[2];
			
			if(d_mac.length()!=16)	return false;
			
			if(commandArray.length!=3+Integer.parseInt(d_length)+1) return false;
			
			
			
			
			int _local_EP_ID = Get_EP_ID_From_MAC(d_mac);
			
			
			if(_local_EP_ID == -1){
				
				// Pending.
				
			}else{
				
				String PropertyCollection="";
				eo_Objects = new ArrayList<EXEC_ORDER>();
				String FilterRet="";
				
				for(int j=3;j<3+Integer.parseInt(d_length);j++){
					
					if(commandArray[j]!=""){
						
					
										PropertyCollection+=commandArray[j]+"|";

										RemoveEffectCommand(_local_EP_ID,StringUtil.split(commandArray[j], ',')[0], StringUtil.split(commandArray[j], ',')[1]);										
										FilterRet = EventsFilter.Filter(_local_EP_ID,StringUtil.split(commandArray[j], ',')[0], StringUtil.split(commandArray[j], ',')[1]); 
										
										if(FilterRet!="-1"){
											
											
											_Command = StringUtil.split(FilterRet, ',');
											
											if(!CheckCurrentPropertyValue(_local_EP_ID,_Command[0],_Command[1])){
												
												EXEC_ORDER eo_Object = new EXEC_ORDER(_Command[0],_Command[1],0,_local_EP_ID,1);
												eo_Objects.add(eo_Object);
												
											}
											
										}
					}
				}
				
			
				
				if(eo_Objects.size()!=0) connmgr.dbmgr.insert_EXEC_ORDER(eo_Objects);
				
				
				EP_PROPC Object = new EP_PROPC(_local_EP_ID,PropertyCollection);			
				CheckObjects2 =  connmgr.dbmgr.query_EP_PROPC("select * from EP_PROPC WHERE EP_ID = ?", new String[]{String.valueOf(_local_EP_ID)});
				
				if(CheckObjects2.size()==0){
					
					CheckObjects2.add(Object);
					connmgr.dbmgr.insert_EP_PROPC(CheckObjects2);
					
				}else{
					
					CheckObjects = connmgr.dbmgr.query_EP_PROPC("select * from EP_PROPC WHERE EP_ID = ? AND PROPERTYCOLLECTION = ?", new String[]{String.valueOf(_local_EP_ID), PropertyCollection});	
					
					if(CheckObjects.size()==0){
						connmgr.dbmgr.update_EP_PROPC("PROPERTYCOLLECTION", Object.PROPERTYCOLLECTION, Object);
					}else{
						IfRepeat = true;
						Log.w("ComandParser", "Repeart Property / Ignore Sync");
					}
				}
				
				if(!IfRepeat) UplinkData(_local_EP_ID,PropertyCollection);
			}
			
			
		}else if(prefix.equals("/I")){
			
			/*
			 * Initial EP
			 * 
			 */
			Log.w("CommandParser_Command", "/I command");
			
			i_mac = commandArray[1];
			i_ip = commandArray[2];
			//i_cluster_id = commandArray[3];
			//i_group_id = commandArray[4];
			//i_endpoint = commandArray[5];
			
			if(i_mac.length()!=16)	return false;
			
			int _local_EP_ID = Get_EP_ID_From_MAC(i_mac);
			
			if(_local_EP_ID == -1){
				
				IntialProcedure();
			}
		}else{
			
			return false;
			
		}
		
		return true;
	}
	
	public static void UplinkData(int _EP_ID,String _PROPERTY_COLLECTION){
		
		
		String returns="";
		String requrl = connmgr.PROP_collector+"EP_PROPERTYDATA_Uplink";
		String rawparameter ="HCCU_ID="+Base64.Encode_Wall(connmgr.HostPn)
				+"&EP_ID="+Base64.Encode_Wall(String.valueOf(_EP_ID))
				+"&PROPERTY_COLLECTION="+Base64.Encode_Wall(_PROPERTY_COLLECTION);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;
		
		Log.w("CommandParser_REQ", rawparameter);
		
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""){				
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			
			if(returns=="-4")	RequestLogs.AddNetworkErrorLogs(3, 1);
			if(returns=="-2")	RequestLogs.AddNetworkErrorLogs(5, 1);
			if(returns=="-1")	RequestLogs.AddNetworkErrorLogs(2, 1);
			
			
		}
		
	}
	
	public static boolean CheckCurrentPropertyValue(int _EP_ID, String _PropertyName, String _ToCompareValue){
		
		eppropc_Objects =  new ArrayList<EP_PROPC>();
		eppropc_Objects = connmgr.dbmgr.query_EP_PROPC("SELECT * FROM EP_PROPC WHERE EP_ID = ?", new String[]{String.valueOf(_EP_ID)});
		String _PropertyCollection = "";
		String[] _PropertyPare = null;
		
		if(eppropc_Objects.size()!=0){
			
			_PropertyCollection = eppropc_Objects.get(0).PROPERTYCOLLECTION;
			
			if(_PropertyCollection.indexOf('|')!=-1){
				
						
						_PropertyPare = StringUtil.split(_PropertyCollection, '|');
						
						for(int x=0; x<_PropertyPare.length; x++){
							
							if(_PropertyPare[x]!=""){
								
								
								if(_PropertyName == StringUtil.split(_PropertyPare[x],',')[0]
										&&
										_ToCompareValue == StringUtil.split(_PropertyPare[x],',')[1]
										){
									
										return true;
									
									
								}
								
							}
							
						}
						
						return false;
				
			}else{
				
						return false;
			}
			
			
		}else{
			
			return false;
		}
		
	}
	
	public static void IntialProcedure(){
		
		String returns = "";
		String requrl = connmgr.EP_collector+"EP_Add";
		String rawparameter = "EP_TypeId="+Base64.Encode_Wall("1")
				+"&EP_UserDefined_Alias="+Base64.Encode_Wall("Unknown")
				+"&EP_ProductId="+Base64.Encode_Wall("1")
				+"&HCCU_Id="+Base64.Encode_Wall(connmgr.HostPn)
				+"&EP_MAC_Id="+Base64.Encode_Wall(i_mac);
		
		String SK = SecurityKey.GenerateSK(rawparameter);
		rawparameter +="&SK="+SK;			
		
		
		returns = connmgr.Parses_Post(requrl, rawparameter);
		
		if(returns.trim().equals("CLIENTNETWORKERROR")||returns.trim()==""||returns.indexOf(',')==-1){				
			
			
			RequestLogs.AddNetworkErrorLogs(4, 1);
			
		}else{
			
			String[] return_array = StringUtil.split(returns, ',');
			
			if(return_array[0].equals("-4"))	RequestLogs.AddNetworkErrorLogs(3, 1);
			if(return_array[0].equals("-2"))	RequestLogs.AddNetworkErrorLogs(5, 1);
			if(return_array[0].equals("-1"))	RequestLogs.AddNetworkErrorLogs(2, 1);
			
			
			if(return_array[1].equals("1")||return_array[1].equals("2")||return_array[1].equals("3")){
				
				_EP_ID = Integer.parseInt(return_array[0]);
				_EP_TYPEID= 1;
				_EP_USERDEFINED_ALIAS="Unknown";
				_EP_PRODUCTID=1;
				_HCCU_ID=Integer.parseInt(connmgr.HostPn);
				_EP_MAC_ID=i_mac;
				_EP_IP = i_ip;
				
				EP object = new EP(_EP_ID,_EP_TYPEID,_EP_USERDEFINED_ALIAS,_EP_PRODUCTID,_HCCU_ID,_EP_MAC_ID,_EP_IP);
				List<EP> objects =  new ArrayList<EP>();
				objects.add(object);
			
				connmgr.dbmgr.insert_EP(objects);
				
			}
			
		}
	}
	
	public static void RemoveEffectCommand(int _EP_ID, String _PropertyName, String _PropertyValue){
		
		
		eo_Objects = connmgr.dbmgr.query_EXEC_ORDER("SELECT * FROM EXEC_ORDER WHERE EP_ID = ?", new String[]{String.valueOf(_EP_ID)});
		
		if(eo_Objects.size()!=0){
			
			for(int x=0; x<eo_Objects.size(); x++){
				
				if(eo_Objects.get(x).PROP == _PropertyName && eo_Objects.get(x).VALUE == _PropertyValue){
					
						connmgr.dbmgr.delete_EXEC_ORDER(eo_Objects.get(x));
					
				}
				
			}
			
		}
		
	}
	
	
}
