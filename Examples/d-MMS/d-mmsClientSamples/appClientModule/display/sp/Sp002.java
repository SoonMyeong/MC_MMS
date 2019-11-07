package display.sp;

import kr.ac.kaist.mms_client.MMSClientHandler;
import kr.ac.kaist.mms_client.MMSConfiguration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Sp002 {
	private static String myMRN = "urn:mrn:kr:service:instance:neonexsoft:sp2";
	private static MMSClientHandler myHandler = null;
	
	public static void main(String args[]) throws Exception{
		myHandler = new MMSClientHandler(myMRN);
		MMSConfiguration.MMS_HOST ="192.168.81.57";
		int port = 9092;
		
		myHandler.setServerPort(port, new MMSClientHandler.RequestCallback() {
			
			@Override
			public Map<String, List<String>> setResponseHeader() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int setResponseCode() {
				// TODO Auto-generated method stub
				return 200;
			}
			
			@Override
			public String respondToClient(Map<String, List<String>> headerField, String message) {
				// TODO Auto-generated method stub
				
				compareHeaderfield(headerField);
				System.out.println(message);
				
				return "OK";
			}
		});

		System.out.println(myMRN+" service activating...");
		
	}
	private static int content_length = 0;
	public static int getContentLength() {
		return content_length;
	}
	
	public static void compareHeaderfield(Map<String,List<String>> headerField) {
		try {
			Iterator<String> iter = headerField.keySet().iterator();
			while (iter.hasNext()){
				String key = iter.next();
				System.out.println(key+":"+headerField.get(key).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	public void terminateServer() {
		myHandler.terminateServer();
	}
}
