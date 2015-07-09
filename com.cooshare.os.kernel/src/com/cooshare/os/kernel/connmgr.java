package com.cooshare.os.kernel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.Date;
import java.util.regex.*;

import com.cooshare.os.kernel.db.LocalDataEngineManager;
import com.cooshare.os.kernel.helper.Base64;

public class connmgr {
	
	
	// Only ServerPath and HostPn are values that you need to set.
	
	public static String ServerPath = "";
	public static String HostPn = "1";
	
	
	private static String VendorId = "CooShare";
	
	
	public static String HCCU_collector = ServerPath+"HCCU.asmx/";
	public static String EVENTS_collector = ServerPath+"EVENTS.asmx/";
	public static String EPTYPE_collector = ServerPath+"EPTYPE.asmx/";
	public static String EP_collector = ServerPath+"EP.asmx/";
	public static String PROD_collector = ServerPath+"PROD.asmx/";
	public static String PROP_collector = ServerPath+"PROP.asmx/";
	public static String REQUESTLOGS_collector = ServerPath+"REQUESTLOGS.asmx/";
	public static String TRANS_collector = ServerPath+"TRANS.asmx/";
	public static String EXECORDER_collector = ServerPath+"EXEC_ORDER.asmx/";
	
	public static int recorder1_T = 30;
	public static int recorder2_T = 5;
	public static int recorder3_T = 10;
	
	
	public static final String HOST_PF = "HOST_PF";
	public static int secondsFromLastTrailPoint = 0;
	public static int secondsFromLastTrailPoint2 = 0;
	public static int secondsFromLastTrailPoint3 = 0;
	public static ConnectivityManager CM;
	public static NetworkInfo NIF;
	public static LocalDataEngineManager dbmgr = null;
	
	public static String SerialPortDevName_1 = "/dev/s3c2410_serial2";
	public static String SerialPortDevName_2 = "/dev/s3c2410_serial3";
	
	public static int SerialPortFileDescriptionFlag_1 = -1;
	public static int SerialPortFileDescriptionFlag_2 = -1;
	
	
	public static boolean AutoSelfCheck = true;
	
	
	public static Date LastUpdateTime = null;
	public static Date LastUpdateTime2 = null;
	public static Date LastUpdateTime3 = null;
	
	private static String global_APN = "";
	
	public static void set_APN(String apn) {
		global_APN = apn;
	}

	public static String get_APN() {
		return global_APN;
	}

	public static String getHostPn() {
		return HostPn;
	}

	public static String getVendorid() {
		return VendorId;
	}

	public static void setVendorid(String _vi) {
		VendorId = _vi;
	}


	public static String Parses_Post(String inputurl, String param) {

		Log.w("CONNMGR", "Establish Network Connection ...");
		
		HttpURLConnection httpurlconnection = null;
		int errorCounter = 0;
		
		try {
			InputStream stream = null;
			int code = -1;
			int iRetry = 0;

			while (iRetry < 10) {
				iRetry++;
			

				try {
					try {
						connmgr.NIF = connmgr.CM.getActiveNetworkInfo();
					} catch (Exception evv) {

					}
					if (NIF != null) {
						
						if ("WIFI".equals(NIF.getTypeName().toUpperCase())) {
							httpurlconnection = (HttpURLConnection) new URL(
									inputurl).openConnection();
						
						} else {
							String proxyHost = android.net.Proxy
									.getDefaultHost();
						
							if (proxyHost != null) {
						
								java.net.Proxy p = new java.net.Proxy(
										java.net.Proxy.Type.HTTP,
										new InetSocketAddress(android.net.Proxy
												.getDefaultHost(),
												android.net.Proxy
														.getDefaultPort()));

								httpurlconnection = (HttpURLConnection) new URL(
										inputurl).openConnection(p);
							} else {
								
								httpurlconnection = (HttpURLConnection) new URL(
										inputurl).openConnection();
							}
						}
						httpurlconnection.setConnectTimeout(10000);
						httpurlconnection.setReadTimeout(10000);
						httpurlconnection.setRequestMethod("POST");
						httpurlconnection.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
						httpurlconnection.setRequestProperty("Content-Length",
								"" + Integer.toString(param.getBytes().length));
						httpurlconnection.setUseCaches(false);
						httpurlconnection.setDoInput(true);
						httpurlconnection.setDoOutput(true);
						DataOutputStream wr = new DataOutputStream(
								httpurlconnection.getOutputStream());
						wr.writeBytes(param);
						wr.flush();
						wr.close();
						code = httpurlconnection.getResponseCode();
						stream = httpurlconnection.getInputStream();
					}
				} catch (Exception e) {
					errorCounter++;
					code = -1;
					e.printStackTrace();
					if (errorCounter > 1) {
						break;
					}
				}
				if (code == HttpURLConnection.HTTP_OK) {
					iRetry = 10;
				}
			}
			if (code == HttpURLConnection.HTTP_OK) {
				String str = convertStreamToString(stream);
				try {
					str = LocalParse(str);
					str = replacenoisywords(str);
					
				} catch (Exception ex) {
					Log.w("cne at", "3" + ex.getMessage());
					str = "CLIENTNETWORKERROR";
				}
				Log.w("CONNMGR_RETURN_B64", str);
				Log.w("CONNMGR_RETURN",Base64.Decode_Wall(str));
				return Base64.Decode_Wall(str);
			} else {
				Log.w("cne at", "2");
				return "CLIENTNETWORKERROR";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("cne at", "1");
			return "CLIENTNETWORKERROR";
		} finally {
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
	}

	public static String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException { /*
												 *  * To convert the InputStream
												 * to String we use the
												 * BufferedReader.readLine() *
												 * method. We iterate until the
												 * BufferedReader return null
												 * which means * there's no more
												 * data to read. Each line will
												 * appended to a StringBuilder *
												 * and returned as String.
												 */
		StringBuffer sb = new StringBuffer();
		InputStreamReader r = new InputStreamReader(is, "UTF-8");
		
		int read = 0;
		try {
			while ((read = r.read()) >= 0) {
		
				sb.append((char) read);
			}
		} catch (Exception e) {
		}
		String ret = new String(sb);
		return ret;
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	static public String toUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		String uStr = "";
		for (int i = 0; i < arChar.length; i++) {
			iValue = (int) str.charAt(i);
			if (iValue <= 256) {
				uStr += "^#x00" + Integer.toHexString(iValue) + ";";
			} else {
				uStr += "^#x" + Integer.toHexString(iValue) + ";";
			}
		}
		return uStr;
	}

	static Pattern pattern;
	static Matcher matcher;

	public static String LocalParse(String Htmlstring) {
		pattern = Pattern.compile("<.+?>");
		matcher = pattern.matcher(Htmlstring);
		while (matcher.find()) {
			Htmlstring = Htmlstring.replace(matcher.group(), "");
		}
		return Htmlstring;
	}

	public static String replacenoisywords(String Htmlstring) {
		pattern = Pattern.compile("\\s*|\t|\r|\n");
		matcher = pattern.matcher(Htmlstring);
		Htmlstring = matcher.replaceAll("");
		return Htmlstring;
	}

}