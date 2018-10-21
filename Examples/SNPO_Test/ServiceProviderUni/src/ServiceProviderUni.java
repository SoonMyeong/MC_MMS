import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.ac.kaist.mms_client.*;

/* -------------------------------------------------------- */
/** 
File name : ServiceProviderUni.java
	Service Provider only forwards messages to SC having urn:mrn:mcl:vessel:dma:poul-lowenorn
Author : Jaehyun Park (jae519@kaist.ac.kr)
	Haeun Kim (hukim@kaist.ac.kr)
	Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2016-12-03

Rev. history : 2017-02-01
Version : 0.3.01
	Added header field features.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2017-04-20 
Version : 0.5.0
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2017-04-25
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2017-05-02
Version : 0.5.4
	Added setting response header
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2017-11-21
Version : 0.7.0
	Compatible with MMS Client beta-0.7.0.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2018-10-21
Version : 0.8.0
	Created for SNPO test.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)
*/
/* -------------------------------------------------------- */

public class ServiceProviderUni {
	static MMSClientHandler sender;
	static MMSClientHandler server;
	public static void main(String args[]) throws Exception{
		String myMRN = "URN:MRN:MCP:Service:Instance:SP-Uni";
		int port = 8902;

		MMSConfiguration.MMS_URL="143.248.57.144:8088";
		//MMSConfiguration.MMS_URL="211.43.202.193:8088";
		MMSConfiguration.DEBUG = false; // If you are debugging client, set this variable true.
		
		server = new MMSClientHandler(myMRN);
		sender = new MMSClientHandler(myMRN);
		sender.setSender(new MMSClientHandler.ResponseCallback() {
			//Response Callback from the request message
			@Override
			public void callbackMethod(Map<String, List<String>> headerField, String message) {
				// TODO Auto-generated method stub
				System.out.println(message);
			}
		});
		
		server.setServerPort(port, new MMSClientHandler.RequestCallback() {
			//Request Callback from the request message
			//it is called when client receives a message
			
			@Override
			public int setResponseCode() {
				// TODO Auto-generated method stub
				return 200;
			}
			
			@Override
			public String respondToClient(Map<String,List<String>> headerField, String message) {
				try {
					Iterator<String> iter = headerField.keySet().iterator();
					while (iter.hasNext()){
						String key = iter.next();
						System.out.println(key+":"+headerField.get(key).toString());
					}
					System.out.println(message);

					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//it only forwards messages to sc having urn:mrn:mcl:vessel:dma:poul-lowenorn
							
							String dstMRN = "urn:mrn:mcl:vessel:dma:poul-lowenorn";
							for(int i = 0 ; i < 10 ; i++) {
								String resMsg = "�ȳ� hi \"hello\" " + i;
								try {
									sender.sendPostMsg(dstMRN, resMsg);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "OK";
			}

			@Override
			public Map<String, List<String>> setResponseHeader() {
				// TODO Auto-generated method stub
				return null;
			}
		}); //server has a context '/forwarding'
		/* It is not same with:
		 * server.setPort(port); //It sets default context as '/'
		 * server.addContext("/forwarding"); //Finally server has two context '/' and '/forwarding'
		 */

	}
}