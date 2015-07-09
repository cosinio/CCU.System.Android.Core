package com.cooshare.os.kernel.helper;

public class SecurityKey {
	
	private static String PrivateKey = "329KDBS";

	public static String GenerateSK(String rawurl){
		
		rawurl+= PrivateKey;
		
		try {
			return MD5Security.md5(rawurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
		
		
	}
}
