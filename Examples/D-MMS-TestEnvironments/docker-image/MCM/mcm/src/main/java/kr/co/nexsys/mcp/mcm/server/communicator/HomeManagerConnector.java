package kr.co.nexsys.mcp.mcm.server.communicator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HomeManagerConnector {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeManagerConnector.class);
	private static final String HOME_MANAGER_URL = "http://192.168.81.57:8081/";
	private static final String USER_AGENT = "MMSServer/0.9.5";
	private static final String TAG = "HomeManagerConnector ";
	private Map<String, Object> msgMap = null;
	private String method;
	
	public HomeManagerConnector() {
		this.msgMap = new ConcurrentHashMap<String, Object>();
		this.method = new String();
	}

	@SuppressWarnings("unchecked")
	public static JSONArray apacheHttpDelete(final Map<String, Object> msgMap) {

		final JSONObject jsonObj = new JSONObject();
		final JSONArray retJsonArray = new JSONArray();

		String url = new String("");

		final Map<String, Object> bodyMap = (Map<String, Object>) msgMap.get("bodyMap");
		final Map<String, Object> headerMap = (Map<String, Object>) msgMap.get("headerMap");

		final String reqUri = bodyMap.get("reqUri").toString();

		url = HOME_MANAGER_URL + reqUri ;

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpDelete delete = new HttpDelete(url);

		delete.setHeader("User-Agent", USER_AGENT);
		//delete.setHeader("Content-type", "application/json;charset:UTF-8;");

		headerMap.forEach((key, value) -> {
			System.out.println("Key : " + key + " Value : " + value);
			delete.setHeader(key, value.toString());
		});

		delete.getRequestLine();
		HttpResponse response = null;
		try {
			response = client.execute(delete);
		} catch (final IOException e) {
			e.printStackTrace();

		}
		if(response==null){
			retJsonArray.add("{\"httpStatus\":500,\"httpResult\":\"unknown Home Manager server error\"}");
		}else {
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd= null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (final IllegalStateException | IOException e) {
			e.printStackTrace();
		}

			final StringBuffer result = new StringBuffer();
		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		System.out.println(result.toString());
		System.out.println(org.json.JSONObject.stringToValue(result.toString()));

		jsonObj.put("httpResult", result.toString());
		jsonObj.put("httpStatus", response.getStatusLine().getStatusCode());
		retJsonArray.add(jsonObj);
		}
		return retJsonArray;

	}

	@SuppressWarnings("unchecked")
	public static JSONArray apacheHttpGet(final Map<String, Object> msgMap) {
		final JSONObject jsonObj = new JSONObject();
		final JSONArray retJsonArray = new JSONArray();
		String url = new String("");

		final Map<String, Object> bodyMap = (Map<String, Object>) msgMap.get("bodyMap");
		final Map<String, Object> headerMap = (Map<String, Object>) msgMap.get("headerMap");

		final String reqUri = bodyMap.get("reqUri").toString();

		url = HOME_MANAGER_URL + reqUri ;
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);
		headerMap.forEach((key, value) -> {
			System.out.println("Key : " + key + " Value : " + value);
			request.addHeader(key, value.toString());
		});

		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd=null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (final UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}

		final StringBuffer result = new StringBuffer();
		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		System.out.println(result.toString());
		jsonObj.put("httpResult", result.toString());
		jsonObj.put("httpStatus", response.getStatusLine().getStatusCode());
		retJsonArray.add(jsonObj);
		return retJsonArray;
	}

	@SuppressWarnings("unchecked")
	private static JSONArray apacheHttpPost(final Map<String, Object> msgMap) {

		final JSONObject jsonObj = new JSONObject();
		final JSONArray retJsonArray = new JSONArray();

		final Map<String, Object> bodyMap = (Map<String, Object>) msgMap.get("bodyMap");
		final Map<String, Object> headerMap = (Map<String, Object>) msgMap.get("headerMap");
		final String reqUri = bodyMap.get("reqUri").toString();
		String url = new String("");

		url = HOME_MANAGER_URL + reqUri;

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "MMSClient/0.9.1");
		post.setHeader("Content-type", "application/json;charset:UTF-8;");

		headerMap.forEach((key, value) -> {
			System.out.println("Key : " + key + " Value : " + value);
			if(!(key.equals("insertRow") ||key.equals("deleteRow")) ) {
			post.setHeader(key, value.toString());
			}
		});

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode node = mapper.createObjectNode();

		bodyMap.forEach((key, value) -> {
			System.out.println("Key : " + key + " Value : " + value);
			node.put(key, value.toString());
		});

		final String JSON_STRING = node.toString();
		post.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		if(response.getStatusLine()!=null) {
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd= null;
			try {
				rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			} catch (final IllegalStateException | IOException e) {
				e.printStackTrace();
			}

			final StringBuffer result = new StringBuffer();
			String line = "";
			try {
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}

			System.out.println(result.toString());
			System.out.println(org.json.JSONObject.stringToValue(result.toString()));

			jsonObj.put("httpResult", result.toString());
			jsonObj.put("httpStatus", response.getStatusLine().getStatusCode());
			retJsonArray.add(jsonObj);
		}else {
			retJsonArray.add("{\"httpStatus\":500,\"httpResult\":\"unknown Home Manager server error\"}");
		}
		return retJsonArray;
	}

	@Deprecated
	public static Map<String, Object> apacheHttpPost(final String reqUri, final Map<String, String> headerMap) {
		String url = new String("");
		final Map<String, Object> resultMap = new HashMap<String, Object>();

		url = HOME_MANAGER_URL + reqUri;

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "MMSClient/0.9.1");
		post.setHeader("Content-type", "application/json;charset:UTF-8;");

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode node = mapper.createObjectNode();
		node.put("mrn", "urn:mrn:smart-navi:device:mms1");
		node.put("url", "localhost:8088");
		final String JSON_STRING = node.toString();
		post.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd= null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (final IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		final StringBuffer result = new StringBuffer();
		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		System.out.println(result.toString());
		System.out.println(org.json.JSONObject.stringToValue(result.toString()));

		resultMap.put("httpResult", result.toString());

		resultMap.put("httpStatus", response.getStatusLine().getStatusCode());
		return resultMap;
	}
	
	public JSONArray apacheHttpRequest(final Map<String, Object> msgMap) {
		this.msgMap = msgMap;
		method = this.msgMap.get("method").toString();


		JSONArray retJsonArray = new JSONArray();

		if (method.toUpperCase().equals("POST")) {
			retJsonArray = apacheHttpPost(msgMap);
		} else if (method.toUpperCase().equals("DELETE")) {
			retJsonArray = apacheHttpDelete(msgMap);
		} else if (method.toUpperCase().equals("PUT")) {
			retJsonArray = apacheHttpPost(msgMap);
		}

		return retJsonArray;
	}
	
	public JSONArray apacheHttpRequestMms(final Map<String, Object> msgMap) {
		this.msgMap = msgMap;
		method = this.msgMap.get("method").toString();


		JSONArray retJsonArray = new JSONArray();

		if (method.toUpperCase().equals("POST")) {
			retJsonArray = apacheHttpPost(msgMap);
		} else if (method.toUpperCase().equals("DELETE")) {
			retJsonArray = apacheHttpDelete(msgMap);
		} else if (method.toUpperCase().equals("PUT")) {
			retJsonArray = apacheHttpPost(msgMap);
		}

		return retJsonArray;
	}
}
