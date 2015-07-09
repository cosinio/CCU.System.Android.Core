package com.cooshare.os.kernel.objects;

public class HCCU_REQ_LOGS {

	public int REQUEST_HCCU_ID;
	public String REQUEST_IP;
	public String REQUEST_DATETIME;
	public int REQUEST_TYPE_ID;
	public int REQUEST_RESULT_ID;
	
	public HCCU_REQ_LOGS(){
		
		
	}
	
	public HCCU_REQ_LOGS(int _REQUEST_HCCU_ID, String _REQUEST_IP, String _REQUEST_DATETIME, int _REQUEST_TYPE_ID, int _REQUEST_RESULT_ID){
		
		this.REQUEST_HCCU_ID = _REQUEST_HCCU_ID;
		this.REQUEST_IP = _REQUEST_IP;
		this.REQUEST_DATETIME = _REQUEST_DATETIME;
		this.REQUEST_TYPE_ID = _REQUEST_TYPE_ID;
		this.REQUEST_RESULT_ID = _REQUEST_RESULT_ID;
		
		
	}
	
}
