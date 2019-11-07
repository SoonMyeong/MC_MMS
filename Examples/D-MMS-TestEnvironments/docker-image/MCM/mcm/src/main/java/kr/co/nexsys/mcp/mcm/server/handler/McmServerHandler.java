package kr.co.nexsys.mcp.mcm.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import kr.co.nexsys.mcp.mcm.clientmanager.dao.dvo.McmEntityDvo;
import kr.co.nexsys.mcp.mcm.clientmanager.service.ClientManagerService;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.geocasting.service.GeocastCircleService;
import kr.co.nexsys.mcp.mcm.server.calc.CircleRangeCalculate;
import kr.co.nexsys.mcp.mcm.server.communicator.HomeManagerConnector;
import kr.co.nexsys.mcp.mcm.server.geometric.GeoFencing;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandler.ConnType.Polling;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandler.ConnType.Push;

@Slf4j
@ChannelHandler.Sharable
@Component
public class McmServerHandler extends ChannelInboundHandlerAdapter {
	
	@Autowired
	private GeocastCircleService geoService;

	@Autowired
	private ClientManagerService clientManagerService;
	/**
	 * mms占쎈퓠占쎄퐣 request占쎈막 占쎈르 header占쎈퓠 占쎈뼄占쎌벉占쎌벥 3�넫�굝履잍에占� �뤃�됲뀋占쎈립占쎈뼄.
	 */
	private enum ReqType {
		CIRCLE,
		
		POLYGON,
		
		UNICASTING,
		
		CALL_HOMEMANAGER;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			///throws Exception
	{
		ByteBuf inBuffer = (ByteBuf) msg;

		String received = inBuffer.toString(CharsetUtil.UTF_8);
		log.debug("///-----------------------Server received: {}", received);
		
		MessageParser msgParser = new MessageParser(received);

		Map<String, Object> msgMap = msgParser.getObjectType();

		String mapType = (String) msgMap.get("mapType");
		JSONArray retJsonArray = new JSONArray();
		String dstMrn = "";
		if(null != msgMap.get("dstMRN")) {
			dstMrn = msgMap.get("dstMRN").toString();
		}
		
		if(mapType.equals(ReqType.CALL_HOMEMANAGER.toString())) {
			String result=null;
			if(null != msgMap.get("insertMms")) {
				// msgMap.get("insertMms").toString();
				result = cudMcmEntity(msgMap);
			}else {
				result = "insertMms";
			}
			
			
			if(result != null && "OK".equals(result)) {
				result = cudCoreTR(msgMap);
				if(result != null && "OK".equals(result)) {
					HomeManagerConnector homeManagerConnector = new HomeManagerConnector();
					retJsonArray = homeManagerConnector.apacheHttpRequest(msgMap);
				}else {
					retJsonArray.add("{\"httpStatus\":500,\"httpResult\":\"unknown MCM server error\"}");
				}
			}else if(result != null && "insertMms".equals(result)) {
				HomeManagerConnector homeManagerConnector = new HomeManagerConnector();
				retJsonArray = homeManagerConnector.apacheHttpRequest(msgMap);
			}
			
			//retJsonArray = this.postReqHomeManager(msgMap);

			//MCM 占쏙옙占쎌삢 占쎌뜎 Home Manager call
			//cudMcmEntity(retJsonArray, msgMap);
			
		}
		else
		if(mapType.equals(ReqType.CIRCLE.toString())) {
			retJsonArray = this.getCircleResultJson(msgMap, dstMrn);
		}else
		if(mapType.equals(ReqType.POLYGON.toString())) {
			retJsonArray = this.getPolygonResultJson(msgMap, dstMrn);
		}else
		if(mapType.equals(ReqType.UNICASTING.toString())) {
			retJsonArray = this.getUnicastResultJson(msgMap, dstMrn);
		}

		log.debug("///--------------return Json\"{}\"",retJsonArray);
		ctx.write(Unpooled.copiedBuffer(retJsonArray.toString(), CharsetUtil.UTF_8));
	}

	/**
	 * Create Update Delete only : CORE_T_R(for legacy MNS)
	 * @param msgMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private String cudCoreTR(Map<String, Object> msgMap) {
		Object obj = msgMap.get("bodyMap");
		String method = msgMap.get("method").toString();
		CoreTRDvo cDvo = new CoreTRDvo();

		if (method.toUpperCase().equals("POST")) {
			//cDvo = geoService.saveCoreTR((Map<String, Object>) obj);
		} else if (method.toUpperCase().equals("DELETE")) {
			/*
			try {
				geoService.deleteCoreTR((Map<String, Object>) obj);
			}catch(Exception e) {
				cDvo = null;
			}
			*/
		} else if (method.toUpperCase().equals("PUT")) {
			cDvo = geoService.saveCoreTR((Map<String, Object>) obj);
		}
		if(null!=cDvo) {
			return "OK";
		}else {
			return null;
		}
	}

	/**
	 * Create Update Delete only : MCM_ENTITY
	 * @param msgMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	private String cudMcmEntity(Map<String, Object> msgMap) {
		Object obj = msgMap.get("bodyMap");
		String method = msgMap.get("method").toString();
		McmEntityDvo mDvo = new McmEntityDvo();

		if (method.toUpperCase().equals("POST")) {
			mDvo = clientManagerService.insertMcmEntity((Map<String, Object>) obj);
		} else if (method.toUpperCase().equals("DELETE")) {
			try {
				clientManagerService.deleteMcmEntity((Map<String, Object>) obj);
			}catch(Exception e) {
				mDvo = null;
			}
		} else if (method.toUpperCase().equals("PUT")) {
			mDvo = clientManagerService.updateMcmEntity((Map<String, Object>) obj);
		}
		if(null!=mDvo) {
			return "OK";
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	private void cudMcmEntity(JSONArray retJsonArray, Map<String, Object> msgMap) {
		org.json.simple.JSONObject resultJObject = null;
		try {
			resultJObject = (org.json.simple.JSONObject) new JSONParser().parse(retJsonArray.get(0).toString());//{"httpStatus":200,"httpResult":"{\"homeMmsDto\":{\"mrn\":\"urn:mrn:imo:imo-no:1000002\",\"type\":\"SC\",\"homeMmsMrn\":\"urn:mrn:smart-navi:device:mms1\"}}"}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Object statObj = resultJObject.get("httpStatus");
		int stat = -1;
		if(statObj instanceof Long) {
			stat = Integer.parseInt(Long.toString((long)resultJObject.get("httpStatus")));
		}else if(statObj instanceof Integer) {
			stat = (int) resultJObject.get("httpStatus");
		}
		if (HttpStatus.SC_OK == stat) {
			Object obj = msgMap.get("bodyMap");
			String method = msgMap.get("method").toString();
			
			if(method.toUpperCase().equals("POST")) {
				clientManagerService.insertMcmEntity((Map<String, Object>) obj);
			}else if(method.toUpperCase().equals("DELETE")) {
				try {
					clientManagerService.deleteMcmEntity((Map<String, Object>) obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.toUpperCase().equals("PUT")) {
				clientManagerService.updateMcmEntity((Map<String, Object>) obj);
			}
			
		}
	}
	
	/**
	 * MMS揶쏉옙 占쎌뒄筌ｏ옙占쎈립 MMS占쎌벥 client 占쎌젟癰귣�占쏙옙 Home Manager占쎈쾻嚥∽옙 占쎈릭占쎈뮉 entity�몴占� MCM 占쎈퓠占쎈즲 占쎈쾻嚥≪빜釉놂옙�뼄.
	 * @param retJsonArray
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	private void insertMcmEntity(JSONArray retJsonArray, Map<String, Object> msgMap) {
		org.json.simple.JSONObject postResultObject = null;
		try {
			postResultObject = (org.json.simple.JSONObject) new JSONParser().parse(retJsonArray.get(0).toString());//{"httpStatus":200,"httpResult":"{\"homeMmsDto\":{\"mrn\":\"urn:mrn:imo:imo-no:1000002\",\"type\":\"SC\",\"homeMmsMrn\":\"urn:mrn:smart-navi:device:mms1\"}}"}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Object statObj = postResultObject.get("httpStatus");
		int stat = -1;
		if(statObj instanceof Long) {
			stat = Integer.parseInt(Long.toString((long)postResultObject.get("httpStatus")));
		}else if(statObj instanceof Integer) {
			stat = (int) postResultObject.get("httpStatus");
		}
		if (HttpStatus.SC_OK == stat) {
			Object obj = msgMap.get("bodyMap");
			System.out.println(obj.getClass());
			clientManagerService.insertMcmEntity((Map<String, Object>) obj);
		}
	}

	@SuppressWarnings("unchecked")
	private JSONArray postReqHomeManager(Map<String, Object> msgMap) {
		
		JSONObject jsonObj = new JSONObject();
		JSONArray retJsonArray = new JSONArray();
		
		String HOME_MANAGER_URL = "http://localhost:8081";

		Map<String, Object> bodyMap=(Map<String, Object>) msgMap.get("bodyMap");
		Map<String, Object> headerMap=(Map<String, Object>) msgMap.get("headerMap");
		String reqUri = bodyMap.get("reqUri").toString();
		//Map<String, Object> resultMap = new ConcurrentHashMap<>();
		String url = new String("");
		
		url = HOME_MANAGER_URL + reqUri;
	
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "MMSClient/0.9.1");
		post.setHeader("Content-type", "application/json;charset:UTF-8;");
		
		headerMap.forEach((key, value) -> {
			System.out.println("Key : " + key + " Value : " + value);
			post.setHeader(key, value.toString());
		});
		
		//post.setHeader("mrn", "urn:mrn:smart-navi:device:mms1");
		//post.setHeader("url", "localhost:8088");
/*
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("mrn", "urn:mrn:smart-navi:device:mms1"));
		urlParameters.add(new BasicNameValuePair("url", "localhost:8088"));
*/
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		
		bodyMap.forEach((key, value) -> {
			System.out.println("Key : " + key + " Value : " + value);
			node.put(key, value.toString());
		});
		
		//node.put("mrn", msgMap.get("mrn").toString());
		//node.put("url", msgMap.get("url").toString());
		String JSON_STRING = node.toString();
		post.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd= null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(result.toString());
		System.out.println(org.json.JSONObject.stringToValue(result.toString()));

		//resultMap.put("httpResult", result.toString());

		//resultMap.put("httpStatus", response.getStatusLine().getStatusCode());

		//return resultMap;
		jsonObj.put("httpResult", result.toString());
		jsonObj.put("httpStatus", response.getStatusLine().getStatusCode());
		retJsonArray.add(jsonObj);
		return retJsonArray;
	}

	/**
	 * 
	 * @param msgMap
	 * @param dstMrn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getCircleResultJson(Map<String, Object> msgMap, String dstMrn) {

		JSONObject jsonObj = new JSONObject();
		JSONArray retJsonArray = new JSONArray();
		log.debug("///---srcMrn[{}]\tdstMrn[{}]\tlati[{}]\tlongi[{}]\tradius[{}]", msgMap.get("srcMRN"),msgMap.get("dstMRN"), msgMap.get("lat"), msgMap.get("long"), msgMap.get("radius"));
		CircleRangeCalculate crc = new CircleRangeCalculate();

		Map<String, Double> cMapRangeResult = new ConcurrentHashMap<>();
		cMapRangeResult = crc.rangeCalculate(Double.parseDouble(msgMap.get("lat").toString()), Double.parseDouble(msgMap.get("long").toString()), Double.parseDouble(msgMap.get("radius").toString()));

		log.debug("circle range result... \n{}", cMapRangeResult);
		List<CoreTRDvo> trList = Collections.synchronizedList(new ArrayList<CoreTRDvo>());
		trList = geoService.findByItemNameOrIdContainingCoreTRs(cMapRangeResult.get("minLati"),cMapRangeResult.get("maxLati"), cMapRangeResult.get("minLongi"), cMapRangeResult.get("maxLongi"));

		int trSize = trList.size();
		if(trSize<1) {
			jsonObj.put("exception", "absent dst: " + dstMrn);
			retJsonArray.add(jsonObj);
		}else {
			for(int i=0;i<trSize;i++) {
				jsonObj.put("connType", "polling");
				jsonObj.put("dstMRN", trList.get(i).getMrn());
				jsonObj.put("netType", getNetType(trList.get(i).getCommNo()));
				retJsonArray.add(jsonObj);
			}
		}
		log.debug("///---///---circle{}", retJsonArray);
		return retJsonArray;
	}
	
	/**
	 * 
	 * @param msgMap
	 * @param dstMrn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getPolygonResultJson(Map<String, Object> msgMap, String dstMrn) {

		JSONObject jsonObj = new JSONObject();
		JSONArray retJsonArray = new JSONArray();
		
		log.debug("///---srcMrn[{}]\tdstMrn[{}]\tlati[{}]\tlongi[{}]",msgMap.get("srcMRN"), msgMap.get("dstMRN"),msgMap.get("lat"), msgMap.get("long"));

		List<Double> latList   = Collections.synchronizedList(new ArrayList<>());
		List<Double> longiList = Collections.synchronizedList(new ArrayList<>());

		latList   = (ArrayList<Double>) msgMap.get("lat");
		longiList = (ArrayList<Double>) msgMap.get("long");

		GeoFencing geoFenc = new GeoFencing(geoService);

		List<CoreTRDvo> trList = Collections.synchronizedList(new ArrayList<>());

		trList = geoFenc.geoFencingCalculation(latList, longiList);

		int trSize = trList.size();

		if(trSize<1) {
			jsonObj.put("exception", "absent dst: " + dstMrn);
			retJsonArray.add(jsonObj);
		}else {
			for(int i=0;i<trSize;i++) {
				jsonObj.put("connType", "polling");
				jsonObj.put("dstMRN", trList.get(i).getMrn());
				jsonObj.put("netType", getNetType(trList.get(i).getCommNo()));
				retJsonArray.add(jsonObj);
			}
		}
		log.debug("///---///---polygon{}", retJsonArray);
		return retJsonArray;
	}
	
	/**
	 * 
	 * @param msgMap
	 * @param dstMrn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getUnicastResultJson(Map<String, Object> msgMap, String dstMrn) {

		JSONObject jsonObj = new JSONObject();
		JSONArray retJsonArray = new JSONArray();
		
		log.debug("///---srcMrn[{}]\tdstMrn[{}]\tip[{}]",msgMap.get("srcMRN"), msgMap.get("dstMRN"),msgMap.get("IPAddr"));
		McmEntityDvo me = new McmEntityDvo();
		me = clientManagerService.findByMrn(dstMrn);

		if(me==null) {
			jsonObj.put("exception", "absent dst: " + dstMrn);
			retJsonArray.add(jsonObj);
		}else {

			if ("2".equals(me.getType().toString())) {//client_type : 1(SC), 2(SP)
			
			//if (dstMrn.contains("SP") || dstMrn.contains("sp")) {
				jsonObj.put("connType", "push");
				jsonObj.put("dstMRN", dstMrn);
				jsonObj.put("IPAddr", me.getIp()); 
				jsonObj.put("portNum", me.getPort());
				retJsonArray.add(jsonObj);
			} else {
				jsonObj.put("connType", "polling");
				jsonObj.put("dstMRN", dstMrn);
				jsonObj.put("netType", getNetType(0));
				retJsonArray.add(jsonObj);
			}
		}
		log.debug("///---///---unicast{}", retJsonArray);
		return retJsonArray;
	}
	
	/**
	 * 
	 * @param msgMap
	 * @param dstMrn
	 * @return
	 */
	@Deprecated
	private JSONArray getUnicastResultJsonOLD(Map<String, Object> msgMap, String dstMrn) {

		JSONObject jsonObj = new JSONObject();
		JSONArray retJsonArray = new JSONArray();
		
		log.debug("///---srcMrn[{}]\tdstMrn[{}]\tip[{}]",msgMap.get("srcMRN"), msgMap.get("dstMRN"),msgMap.get("IPAddr"));
		List<CoreTRDvo> trList = Collections.synchronizedList(new ArrayList<>());
		trList = geoService.findByMrn(dstMrn);
		int trSize = trList.size();

		if(trSize<1) {
			jsonObj.put("exception", "absent dst: " + dstMrn);
			retJsonArray.add(jsonObj);
		}else {

			//if ("2".equals(trList.get(0).getClientType().toString())) {//client_type : 1(SC), 2(SP)
			
			if (dstMrn.contains("SP") || dstMrn.contains("sp")) {
				jsonObj.put("connType", "push");
				jsonObj.put("dstMRN", dstMrn);
				jsonObj.put("IPAddr", trList.get(0).getSIp()); 
				jsonObj.put("portNum", trList.get(0).getPortNumber());
				retJsonArray.add(jsonObj);
			} else {
				jsonObj.put("connType", "polling");
				jsonObj.put("dstMRN", dstMrn);
				jsonObj.put("netType", getNetType(trList.get(0).getCommNo()));
				retJsonArray.add(jsonObj);
			}
		}
		log.debug("///---///---unicast{}", retJsonArray);
		return retJsonArray;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	public static String getNetType(int h) {
		int hh =h;
		String temp="";
		switch(hh) {
		case 0: temp="LTE-M";
		break;
		case 1: temp="VDES";
		break;
		case 2: temp="HF-MF";
		break;
		case 3: temp="Satellite";
		break;
		}
		return temp;
	}


	public static JSONObject resultPush(
			final String dstMrn,
			final Integer commNo){
		return resultJsonObject(Push, dstMrn, commNo);
	}

	public static JSONObject resultPolling(
			final String dstMrn,
			final Integer commNo){
		return resultJsonObject(Polling, dstMrn, commNo);
	}

	public static JSONObject resultPush(
			final String dstMrn,
			final String ip,
			final String port){
		return resultJsonObject(Push, dstMrn, ip, port);
	}
	public static JSONObject resultPolling(
			final String dstMrn,
			final String ip,
			final String port){
		return resultJsonObject(Polling, dstMrn, ip, port);
	}


	@SuppressWarnings("unchecked")
	private static JSONObject resultJsonObject(
								final ConnType connType,
								final String dstMrn,
								final Integer commNo){
		final JSONObject jsonObj = new JSONObject();
		jsonObj.put("connType", connType.getType());
		jsonObj.put("dstMRN", dstMrn);
		jsonObj.put("netType", getNetType(commNo));
		return jsonObj;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject resultJsonObject(
								final ConnType connType,
								final String dstMrn,
								final String ip,
								final String port){
		final JSONObject jsonObj = new JSONObject();
		jsonObj.put("connType", connType.getType());
		jsonObj.put("dstMRN", dstMrn);
		jsonObj.put("IPAddr", ip);
		jsonObj.put("portNum", port);
		return jsonObj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject errorJsonObject(final String dstMrn){
		final JSONObject jsonObj = new JSONObject();
		jsonObj.put("exception", "absent dst: " + dstMrn);
		return jsonObj;
	}


	public enum ConnType{

		Push("push"), Polling("polling");

		private String type;

		ConnType(String type){
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
}
