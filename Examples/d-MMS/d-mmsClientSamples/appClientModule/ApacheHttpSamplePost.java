import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ApacheHttpSamplePost {
	public static void main(String[] args) {
		String url = "http://localhost:8588/";
	
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "MMSClient/0.9.1");
		post.setHeader("Content-type", "application/json;charset:UTF-8;");
		//post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		
		/*
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("geocasting", geoType));
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		
		String geoType = "";
		int mod = //Integer.parseInt(args[0]);
		2;///test;immediate
		switch(mod) {
		case 0:
			geoType = "circle";

			node.put("srcMRN", "SP");
			node.put("dstMRN", "*");
			node.put("lat", "33.5499499324243");
			node.put("long", "176.720328966827");
			node.put("radius", "400");
			break;
		case 1:
			geoType = "polygon";

			node.put("srcMRN", "SP");
			node.put("dstMRN", "*");
			node.put("lat", "[34.796251, 43.315407,  4.439433,7.548697]");
			node.put("long", "[121.410482,172.756282,167.188200,111.833566]");
			break;
		case 2:
			geoType = "unicasting";

			node.put("srcMRN", "SP");
			node.put("dstMRN", "SP");
			node.put("IPAddr", "127.0.0.1");
			break;
		default:
			break;
		}
		
		node.put("geocasting", geoType);
		
		
		String JSON_STRING = node.toString();
		post.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));
		
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd= null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (IllegalStateException | IOException e) {
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

		
	}
}
