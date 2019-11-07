package spSendToSc;
import java.util.List;
import java.util.Map;

import kr.ac.kaist.mms_client.*;

/* -------------------------------------------------------- */
/** 
File name : ServiceProvider.java
	Service Provider only forwards messages to SC having urn:mrn:mcl:vessel:dma:paul-lowenorn
Author : Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2018-10-05
*/
/* -------------------------------------------------------- */

public class Sp001SendToSc001 {
	public static void main(String args[]) throws Exception{
		String myMRN = "urn:mrn:kr:service:instance:neonexsoft:sp1";

//		MMSConfiguration.MMS_URL="mms-kaist.com:8088";
		MMSConfiguration.MMS_URL="192.168.81.57:8088";
		MMSConfiguration.DEBUG = true; // If you are debugging client, set this variable true.
		
		MMSClientHandler sender = new MMSClientHandler(myMRN);
		sender.setSender(new MMSClientHandler.ResponseCallback() {
			//Response Callback from the request message
			@Override
			public void callbackMethod(Map<String, List<String>> headerField, String message) {
				// TODO Auto-generated method stub
				System.out.println(message);
			}
		});
		
		for (int i = 1; i <= 1;i++){
			String dstMRN = "urn:mrn:kr:vessel:neonexsoft:sc1";
			String message = "안녕 hi "+dstMRN +" \"hello\" " + i;
			sender.sendPostMsg(dstMRN, message, 300000);
			Thread.sleep(1000);
		}
	}
}
