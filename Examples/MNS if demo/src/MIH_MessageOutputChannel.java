/* -------------------------------------------------------- */
/** 
File name : MIH_MessageOutputChannel.java
Author : Jaehyun Park (jae519@kaist.ac.kr)
	Jin Jung (jungst0001@kaist.ac.kr)
Creation Date : 2017-01-24
Version : 0.3.01

Rev. history : 2017-02-01
	Added locator registering features.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2017-06-19
Version : 0.5.7
	Applied LogBack framework in order to log events
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2017-09-26
Version : 0.6.0
	Replaced from random int SESSION_ID to String SESSION_ID as connection context channel id.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2018-04-23
Version : 0.7.1
	Removed RESOURCE_LEAK hazard.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)	

Rev. history : 2018-06-06
Version : 0.7.1
	Removed reply socket features.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2018-06-06
Version : 0.7.2
	Set the IP address of MNS_Dummy from "127.0.0.1" to "mns_dummy"
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)

Rev. history : 2018-07-27
Version : 0.7.2
	Added geocasting features which cast message to circle or polygon area.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)
*/
/* -------------------------------------------------------- */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import kr.ac.kaist.mms_server.MMSConfiguration;
//import kr.ac.kaist.mms_server.MMSLog;

class MIH_MessageOutputChannel {

	//private static final Logger logger = LoggerFactory.getLogger(MIH_MessageOutputChannel.class);
	private String SESSION_ID = "";
	
	MIH_MessageOutputChannel(String sessionId) {
		this.SESSION_ID = sessionId;

	}
	
	@SuppressWarnings("finally")
	String sendToMNS(String request) {
		
		Socket MNSSocket = null;
		PrintWriter pw = null;	
		InputStreamReader isr = null;
		BufferedReader br = null;
		String queryReply = null;
    	try{
	    	//String modifiedSentence;

	    	MNSSocket = new Socket("59.21.46.76", 8588);
	    	MNSSocket.setSoTimeout(5000);
	    	
	    	/**
	           * Reader, Writer are starting here.
	        **/
	    	pw = new PrintWriter(MNSSocket.getOutputStream()); //Initialize Writer.
	    	isr = new InputStreamReader(MNSSocket.getInputStream()); 
	    	br = new BufferedReader(isr); //Initialize Reader.
	    	String inputLine = null;
			StringBuffer response = new StringBuffer();
			
	    	
		//    logger.trace("SessionID="+this.SESSION_ID+" "+request+".");
		
			System.out.println("Request="+request);
		    pw.println(request);
		    pw.flush();
		    if (!MNSSocket.isOutputShutdown()) { //If the Writer ends writing message, shutdown the Writer.
		    	MNSSocket.shutdownOutput();
		    }
		    /**
	           * Writer ends.
	          **/
		    
	    	while ((inputLine = br.readLine()) != null) {
	    		response.append(inputLine);
	    	}
		    
	    	
	    	queryReply = response.toString();
	    //	logger.trace("SessionID="+this.SESSION_ID+" From server=" + queryReply+".");
	    	/**
	           * Reader ends.
	          **/
	    	
	    	if (queryReply.equals("No")) {
	    		return "No";
	    	} else if (queryReply.equals("OK")) {
	    		return "OK";
	    	}    	
	    	
    	} catch (UnknownHostException e) {
    	//	logger.warn("SessionID="+this.SESSION_ID+" "+e.getMessage()+".");
			return null;
		} catch (IOException e) {
		//	logger.warn("SessionID="+this.SESSION_ID+" "+e.getMessage()+".");
			return null;
		} finally {
		

			/**
             * Close Reader, Writer and Connection.
            **/
    		if (pw != null) {
    			pw.close();
    		}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
		//			logger.warn("SessionID="+this.SESSION_ID+" "+e.getMessage()+".");
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
		//			logger.warn("SessionID="+this.SESSION_ID+" "+e.getMessage()+".");
				}
			}
    		if (MNSSocket != null) {
    			try {
					MNSSocket.close();
				} catch (IOException e) {
		//			logger.warn("SessionID="+this.SESSION_ID+" "+e.getMessage()+".");
				}
    		}
    		
			return queryReply;
		}
	}
}