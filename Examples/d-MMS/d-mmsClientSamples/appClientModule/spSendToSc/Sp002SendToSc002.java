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

public class Sp002SendToSc002 {
	public static void main(String args[]) throws Exception{
		String myMRN = "urn:mrn:smart-navi:service:svc-002";

//		MMSConfiguration.MMS_URL="mms-kaist.com:8088";
		MMSConfiguration.MMS_URL="127.0.0.1:8089";
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
			String dstMRN = "urn:mrn:imo:imo-no:2000001";
			String message = "안녕 hi "+dstMRN +" \"hello\" " + i;
			sender.sendPostMsg(dstMRN, message, 300000);
			Thread.sleep(1000);
		}
	}
}
