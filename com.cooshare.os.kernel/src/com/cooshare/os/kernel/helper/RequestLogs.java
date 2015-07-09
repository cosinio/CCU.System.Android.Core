package com.cooshare.os.kernel.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.cooshare.os.kernel.connmgr;
import com.cooshare.os.kernel.objects.HCCU_REQ_LOGS;

public class RequestLogs {

	
public static void AddNetworkErrorLogs(int result_id, int request_type_id){
		
		HCCU_REQ_LOGS object = new HCCU_REQ_LOGS();
		object.REQUEST_DATETIME =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		object.REQUEST_HCCU_ID = Integer.parseInt(connmgr.HostPn);
		object.REQUEST_IP = "0.0.0.0";
		object.REQUEST_TYPE_ID = request_type_id;
		object.REQUEST_RESULT_ID = result_id;
		List<HCCU_REQ_LOGS> objects =  new ArrayList<HCCU_REQ_LOGS>();
		objects.add(object);
		connmgr.dbmgr.insert_HCCU_REQ_LOGS(objects);
		
	}
}
