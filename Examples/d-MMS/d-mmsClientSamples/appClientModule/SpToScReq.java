import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.slf4j.Logger;

public class SpToScReq {
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SpToScReq.class);
	
	public static void main(String[] args) {

		String linkUrl = "http://localhost:8088/";
		
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(linkUrl).openConnection();
		} catch (IOException e2) {
			LOGGER.info("/-/-/ IOException");
		}	
		
		conn.setRequestProperty("srcMRN", "urn:mrn:smart-navi:device:service-provider");
		conn.setRequestProperty("dstMRN", "urn:mrn:mcl:vessel:dma:poul-lowenorn");
		
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e2) {
			LOGGER.info("/-/-/ ProtocolException");
		}
		
		try {
			conn.connect();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		StringBuilder params = new StringBuilder("?contents=");
		String contents = "&nbsp;³»¿ë<br/><br/>";
		try{
			params.append(URLEncoder.encode(contents, "UTF-8"));
		}catch(UnsupportedEncodingException e){
			LOGGER.info("/-/-/ UnsupportedEncodingException");
		}
		
		OutputStream wr = null;
		
		try {
			byte[] b = params.toString().getBytes("UTF-8");
			
			wr = conn.getOutputStream();
			wr.write(b);
		} catch(IOException e1) {
			LOGGER.info("/-/-/ IOException");
		} finally {
			if (wr != null) {
				try {
					wr.flush();
					wr.close();
				} catch (IOException e) {
					LOGGER.info("/-/-/ IOException");
				}
			}
		}
		
		int httpResult = new Integer(0);
		try {
			httpResult = conn.getResponseCode();
		} catch (IOException e2) {
			LOGGER.info("/-/-/ IOException");
		}

		StringBuffer jb = null;
		String line = null;
		BufferedReader reader= null;
		InputStreamReader inputStreamReader = null;
		
		try{
			if (httpResult == HttpURLConnection.HTTP_OK) {
				jb = new StringBuffer();
				inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
				reader = new BufferedReader(inputStreamReader);
	
				while ((line = reader.readLine()) != null) {
					jb.append(line);
				}
				
			} else {
				
			}
		}catch(IOException e1) {
			LOGGER.info("/-/-/ IOException");
		}finally{
			try {
				if(null!=reader) {reader.close();}
				if(null!=inputStreamReader) {inputStreamReader.close();}
			} catch (IOException e) {
				LOGGER.error("error on closing");
			}
		}
		LOGGER.debug(jb.toString());
		System.out.println("---------------"+jb.toString());
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public SpToScReq() {
		super();
	}

}