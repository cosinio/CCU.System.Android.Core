package com.cooshare.os.kernel.objects;

public class EP_TRANS {

	public int TRANSACTION_ID;
	public int TARGET_EP_ID;
	public int TRIGGER_TYPE_ID;
	public String MISC;
	
	public EP_TRANS(){
		
		
	}
	
	public EP_TRANS(int _TRANSACTION_ID,int _TARGET_EP_ID,int _TRIGGER_TYPE_ID,String _MISC){
		
		this.TRANSACTION_ID = _TRANSACTION_ID;
		this.TARGET_EP_ID = _TARGET_EP_ID;
		this.TRIGGER_TYPE_ID = _TRIGGER_TYPE_ID;
		this.MISC = _MISC;
		
	}
	
}
