package com.cooshare.os.kernel.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.cooshare.os.kernel.connmgr;
import com.cooshare.os.kernel.objects.EP_EVENTS;
import com.cooshare.os.kernel.objects.EP_PROP;

public class EventsFilter {
	
	static List<EP_EVENTS> ev_Objects = null;
	static List<EP_PROP> eprop_Objects = null;
    
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Date min;
	static Date max;
	static Date now;
	
	public EventsFilter(){
		
		ev_Objects = new ArrayList<EP_EVENTS>();
		eprop_Objects = new ArrayList<EP_PROP>();
	}
	
	public static String Filter(int EP_ID, String property, String value){
		
	
		ev_Objects = connmgr.dbmgr.query_EP_EVENTS("SELECT * FROM EP_EVENTS WHERE ISACTIVE = ? AND EP_ID = ?", new String[]{"1",String.valueOf(EP_ID)});

		
		
		if(ev_Objects.size()!=0){
			
			
			for(int x=0; x<ev_Objects.size();x++){
				
				eprop_Objects = connmgr.dbmgr.query_EP_PROP("SELECT * FROM EP_PROP WHERE EP_PROPERTY_ID = ?", new String[]{String.valueOf(ev_Objects.get(x).EP_PROPERTY_ID)});		
				if(eprop_Objects.get(0).EP_PROPERTY_NAME.trim()!=property) continue;
				
				if(ev_Objects.get(x).ACTIVERANGE_IFCONTAINBOUNDARY==1){
					
					 try {
						 min = df.parse(ev_Objects.get(x).ACTIVERANGE_MIN);
						 max = df.parse(ev_Objects.get(x).ACTIVERANGE_MAX);
						 now = new Date();
						 
						 if(now.compareTo(min)>0 && now.compareTo(max)<0){
							 
							 
							 if(CheckPropertyValue(ev_Objects.get(x).OPERATION_TYPE_ID, ev_Objects.get(x).TARGET,value)){
								 
								 return ev_Objects.get(x).COMMAND;
							 }
							 
						 }else{
							 
							 return "-1";
						 }
						 
						
					} catch (ParseException e) {
						
						return "-1";
					}
					
					
				}else{
					
					if(CheckPropertyValue(ev_Objects.get(x).OPERATION_TYPE_ID, ev_Objects.get(x).TARGET,value)){
						
						return ev_Objects.get(x).COMMAND;
					}
					
				}
				
				
			}
			
			return "-1";
			
			
		}else{
			
			return "-1";
		}
	}
	
	public static boolean CheckPropertyValue(int _Operation_Type, float _target, String sensor_value){
		
		
			float _sensor_value = Float.parseFloat(sensor_value);
			
					
					switch(_Operation_Type)

					{

					case 1: if(_sensor_value<_target){ 	return true; } else {return false; }

					case 2: if(_sensor_value>_target){ 	return true; } else {return false; }

					case 3: if(_sensor_value==_target){ return true; } else {return false; }

					case 4: if(_sensor_value<=_target){ return true; } else {return false; }
					
					case 5: if(_sensor_value>=_target){ return true; } else {return false; }
					
					case 6: if(_sensor_value!=_target){ return true; } else {return false; }
					
					default: return false;

					}
		
	}

}
