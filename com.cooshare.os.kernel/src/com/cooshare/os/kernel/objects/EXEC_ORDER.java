package com.cooshare.os.kernel.objects;

public class EXEC_ORDER {
	
	public String PROP;
	public String VALUE;
	public int _id;
	public int EP_ID;
	public int OWNER;
	
	public EXEC_ORDER(){
		
		
	}
	
	public EXEC_ORDER(String _PROP, String _VALUE, int __id, int _EP_ID, int _OWNER)
	{
		this.PROP = _PROP;
		this.VALUE=_VALUE;
		this._id=__id;
		this.EP_ID = _EP_ID;
		this.OWNER = _OWNER;
	}

}
