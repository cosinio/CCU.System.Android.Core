package com.cooshare.os.kernel.objects;

public class EP {
	
	public int EP_ID;
	public int EP_TYPEID;
	public String EP_USERDEFINED_ALIAS;
	public int EP_PRODUCTID;
	public int HCCU_ID;
	public String EP_MAC_ID;
	public String EP_IP;
	
	public EP(){
			
	}
	
	public EP(int _EP_ID,int _EP_TYPEID,String _EP_USERDEFINED_ALIAS,int _EP_PRODUCTID,int _HCCU_ID,String _EP_MAC_ID, String _EP_IP){
		
		
		this.EP_ID = _EP_ID;
		this.EP_TYPEID = _EP_TYPEID;
		this.EP_USERDEFINED_ALIAS = _EP_USERDEFINED_ALIAS;
		this.EP_PRODUCTID = _EP_PRODUCTID;
		this.HCCU_ID = _HCCU_ID;
		this.EP_MAC_ID = _EP_MAC_ID;
		this.EP_IP = _EP_IP;
		
	}

}
