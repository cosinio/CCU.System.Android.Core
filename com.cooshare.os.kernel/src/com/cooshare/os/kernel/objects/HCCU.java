package com.cooshare.os.kernel.objects;

public class HCCU {
	
	public int HCCU_ID;
	public String HCCU_UID;
	public String HCCU_MAC_ID;
	public int HCCU_MAC_STATUS;
	public int HCCU_ACC_STATUS;
	
	public HCCU(){
		
	}
	
	public HCCU(int _HCCU_ID, String _HCCU_UID, String _HCCU_MAC_ID, int _HCCU_MAC_STATUS, int _HCCU_ACC_STATUS){
		
		this.HCCU_ID = _HCCU_ID;
		this.HCCU_UID = _HCCU_UID;
		this.HCCU_MAC_ID = _HCCU_MAC_ID;
		this.HCCU_MAC_STATUS = _HCCU_MAC_STATUS;
		this.HCCU_ACC_STATUS = _HCCU_ACC_STATUS;
	}

}
