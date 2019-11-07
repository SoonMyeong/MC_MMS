import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApacheHttpSampleGet {
	public static void main(String[] args) {
		String url = "http://localhost:8088/";
	
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
	
		// add request header
		request.addHeader("User-Agent", "MMSClient/0.9.1");
		request.addHeader("mrn", "urn:mrn:smart-navi:device:mms1");
		request.addHeader("srcMRN", "urn:mrn:smart-navi:device:mms1");
		
		String geoType = "";
		int mod = Integer.parseInt(args[0]);
		mod=0;///test;immediate
		switch(mod) {
		case 0:
			geoType = "circle";

			request.addHeader("srcMRN", "SP");
			request.addHeader("dstMRN", "*");
			request.addHeader("lat", "33.5499499324243");
			request.addHeader("long", "176.720328966827");
			request.addHeader("radius", "400");
			break;
		case 1:
			geoType = "polygon";

			request.addHeader("srcMRN", "SP");
			request.addHeader("dstMRN", "*");
			request.addHeader("lat", "[34.796251, 43.315407,  4.439433,7.548697]");
			request.addHeader("long", "[121.410482,172.756282,167.188200,111.833566]");
			break;
		case 2:
			geoType = "unicasting";

			request.addHeader("srcMRN", "SP");
			request.addHeader("dstMRN", "SP");
			request.addHeader("IPAddr", "172.10.10.117");
			break;
		default:
			break;
		}
		
		request.addHeader("geocasting", geoType);
		System.out.println(request.toString());
		System.out.println(request.getAllHeaders());
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
	
		BufferedReader rd=null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(result.toString());
		System.out.println(org.json.JSONObject.stringToValue(result.toString()));
		JSONObject jobj = new JSONObject(result.toString());
		Object obj = jobj.get("mmsDto");

		
	}
}
