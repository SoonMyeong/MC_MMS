package kr.ac.kaist.mns_interaction;
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

Rev. history : 2019-06-18
Version : 0.9.2
	Added ErrorCode.
Modifier : Jaehee ha (jaehee.ha@kaist.ac.kr)
*/
/* -------------------------------------------------------- */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.ac.kaist.mms_server.ErrorCode;
import kr.ac.kaist.mms_server.MMSConfiguration;
import kr.ac.kaist.mms_server.MMSLog;

class MIH_MessageOutputChannel {

	private static final Logger logger = LoggerFactory.getLogger(MIH_MessageOutputChannel.class);
	private String SESSION_ID = "";
	
	private MMSLog mmsLog = null;

	private static HashMap<String, String> MRNtoIP = new HashMap<String, String>();
	
	MIH_MessageOutputChannel(String sessionId) {
		this.SESSION_ID = sessionId;
		mmsLog = MMSLog.getInstance();
	}
	

	String sendToMNS(String request) {
//		MRNtoIP.put("urn:mrn:mcl:vessel:dma:poul-lowenorn","172.18.0.5:0:1");
//		MRNtoIP.put("urn:mrn:smart-navi:device:tm-server","172.18.0.4:8902:2");
//		MRNtoIP.put("mcc_test","192.168.11.159:9000:2");

		Socket MNSSocket = null;
		PrintWriter pw = null;	
		InputStreamReader isr = null;
		BufferedReader br = null;
		String queryReply = null;
    	try{
	    	//String modifiedSentence;

	    	MNSSocket = new Socket(MMSConfiguration.getMnsHost(), MMSConfiguration.getMnsPort());
	    	MNSSocket.setSoTimeout(5000);
	    	pw = new PrintWriter(MNSSocket.getOutputStream());
	    	isr = new InputStreamReader(MNSSocket.getInputStream());
	    	br = new BufferedReader(isr);
	    	String inputLine = null;
			StringBuffer response = new StringBuffer();
			
	    	mmsLog.trace(logger, this.SESSION_ID, request+".");
		
		    pw.println(request);
		    pw.flush();
		    if (!MNSSocket.isOutputShutdown()) {
		    	MNSSocket.shutdownOutput();
		    }
		   
		    
	    	while ((inputLine = br.readLine()) != null) {
	    		response.append(inputLine);
	    	}
		    
	    	
	    	queryReply = response.toString();
	    	mmsLog.trace(logger, this.SESSION_ID, "From MNS server=" + queryReply+".");

    	} catch (UnknownHostException e) {
    		mmsLog.errorException(logger, SESSION_ID, ErrorCode.MNS_CONNECTION_OPEN_ERROR.toString(), e, 5);

		} catch (IOException e) {
			mmsLog.errorException(logger, SESSION_ID, ErrorCode.MNS_CONNECTION_OPEN_ERROR.toString(), e, 5);
		} finally {
    		if (pw != null) {
    			pw.close();
    		}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					mmsLog.errorException(logger, SESSION_ID, ErrorCode.MNS_CONNECTION_CLOSE_ERROR.toString(), e, 5);
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					mmsLog.errorException(logger, SESSION_ID, ErrorCode.MNS_CONNECTION_CLOSE_ERROR.toString(), e, 5);
				}
			}
    		if (MNSSocket != null) {
    			try {
					MNSSocket.close();
				} catch (IOException e) {
					mmsLog.errorException(logger, SESSION_ID, ErrorCode.MNS_CONNECTION_CLOSE_ERROR.toString(), e, 5);
				}
    		}
		}
    	return queryReply;

		//return "{\"IPAddr\":\"127.0.0.1\",\"connType\":\"push\",\"portNum\":\"8902\",\"dstMRN\":\"EncZoneRes\"}";

	}


		/*
		String data = request;
		String result ="";

		// newly designed interfaces
		if (data.startsWith("{")) {
			try {
				String dataToReply = "";

				JSONParser queryParser = new JSONParser();

				JSONObject query = (JSONObject) queryParser.parse(data);

				if (query.get("unicasting") != null) {
					JSONObject unicastingQuery = (JSONObject) query.get("unicasting");
					String srcMRN = unicastingQuery.get("srcMRN").toString();
					String dstMRN = unicastingQuery.get("dstMRN").toString();
					String IPAddr = unicastingQuery.get("IPAddr").toString();

					String dstInfo = (String) MRNtoIP.get(dstMRN);
					if (dstInfo != null) {
						String splittedDstInfo[] = dstInfo.split(":");
						if (splittedDstInfo[2].equals("1")) { //polling model
							JSONObject connTypePolling = new JSONObject();
							connTypePolling.put("connType", "polling");
							connTypePolling.put("dstMRN", dstMRN);
							connTypePolling.put("netType", "LTE-M");
							dataToReply = connTypePolling.toJSONString();
						} else if (splittedDstInfo[2].equals("2")) { //push model
							JSONObject connTypePush = new JSONObject();
							connTypePush.put("connType", "push");
							connTypePush.put("dstMRN", dstMRN);
							connTypePush.put("IPAddr", splittedDstInfo[0]);
							connTypePush.put("portNum", splittedDstInfo[1]);
							dataToReply = connTypePush.toJSONString();
						}
					} else {
						dataToReply = "No";
					}


				} else if (query.get("geocasting_circle") != null) {
					JSONObject geocastingQuery = (JSONObject) query.get("geocasting_circle");
					String srcMRN = geocastingQuery.get("srcMRN").toString();
					String dstMRN = geocastingQuery.get("dstMRN").toString();
					String geoLat = geocastingQuery.get("lat").toString();
					String geoLong = geocastingQuery.get("long").toString();
					String geoRadius = geocastingQuery.get("radius").toString();

					float lat = Float.parseFloat(geoLat);
					float lon = Float.parseFloat(geoLat);
					float rad = Float.parseFloat(geoRadius);

					if (20000 >= rad && 90 >= Math.abs(lat) && 180 >= Math.abs(lon)) {
						Set<String> keys = MRNtoIP.keySet();

						Iterator<String> keysIter = keys.iterator();
						// MRN lists are returned by json format.
						JSONArray objList = new JSONArray();


						if (keysIter.hasNext()) {
							do {
								String key = keysIter.next();
								String value = MRNtoIP.get(key);
								String[] parsedVal = value.split(":");
								if (parsedVal.length == 4) { // Geo-information exists.
									String[] curGeoMRN = parsedVal[3].split("-");
									float curLat = Float.parseFloat(curGeoMRN[1]);
									float curLong = Float.parseFloat(curGeoMRN[3]);


									if (((lat - curLat) * (lat - curLat) + (lon - curLong) * (lon - curLong)) < rad * rad) {
										JSONObject item = new JSONObject();
										item.put("dstMRN", key);
										item.put("netType", "LTE-M");
										if (parsedVal[2].equals("1")) {
											item.put("connType", "polling");
										} else if (parsedVal[1].equals("2")) {
											item.put("connType", "push");
										}
										objList.add(item);
									}
								}


							} while (keysIter.hasNext());
						}
						dataToReply = objList.toJSONString();
					}
				} else if (query.get("geocasting_polygon") != null) {
					JSONObject geocastingQuery = (JSONObject) query.get("geocasting_polygon");
					String srcMRN = geocastingQuery.get("srcMRN").toString();
					String dstMRN = geocastingQuery.get("dstMRN").toString();
					String geoLat = geocastingQuery.get("lat").toString();
					String geoLong = geocastingQuery.get("long").toString();

					System.out.println("Geocating polygon, srcMRN=" + srcMRN + ", dstMRN=" + dstMRN + ", geoLat=" + geoLat + ", geoLong=" + geoLong);
					dataToReply = "[{\"exception\":\"absent MRN\"}]";
				}
				result = dataToReply;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		//logger.debug(data);

		String dataToReply = "MNSDummy-Reply:";

		if (data.regionMatches(0, "MRN-Request:", 0, 12)) {

			data = data.substring(12);


			//loggerdebug("MNSDummy:data=" + data);
			if (!data.regionMatches(0, "urn:mrn:mcs:casting:geocast:smart:", 0, 34)) {
				try {
					if (MRNtoIP.containsKey(data)) {
						dataToReply += MRNtoIP.get(data);
					} else {
						//loggerdebug("No MRN to IP Mapping.");
						dataToReply = "No";
					}
					//loggerdebug(dataToReply);

					result = dataToReply;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			} else { // if geocasting (urn:mrn:mcs:casting:geocasting:smart:-)


				String geoMRN = data.substring(34);
				String[] parsedGeoMRN = geoMRN.split("-");
				//loggerinfo("Geocasting MRN="+geoMRN+".");
				float lat = Float.parseFloat(parsedGeoMRN[1]);
				float lon = Float.parseFloat(parsedGeoMRN[3]);
				float rad = Float.parseFloat(parsedGeoMRN[5]);

				if (20000 <= rad && 90 >= Math.abs(lat) && 180 >= Math.abs(lon)) {
					try {
						Set<String> keys = MRNtoIP.keySet();

						Iterator<String> keysIter = keys.iterator();
						// MRN lists are returned by json format.
						// {"poll":[{"mrn":"urn:mrn:-"},{"mrn":"urn:mrn:-"},{"mrn":"urn:mrn:-"},....]}
						JSONArray objlist = new JSONArray();


						if (keysIter.hasNext()) {
							do {
								String key = keysIter.next();
								String value = MRNtoIP.get(key);
								String[] parsedVal = value.split(":");
								if (parsedVal.length == 4) { // Geo-information exists.
									String[] curGeoMRN = parsedVal[3].split("-");
									float curLat = Float.parseFloat(curGeoMRN[1]);
									float curLong = Float.parseFloat(curGeoMRN[3]);


									if (((lat - curLat) * (lat - curLat) + (lon - curLong) * (lon - curLong)) < rad * rad) {
										JSONObject item = new JSONObject();
										item.put("dstMRN", key);
										objlist.add(item);
									}
								}


							} while (keysIter.hasNext());
						}
						JSONObject dstMRNs = new JSONObject();
						dstMRNs.put("poll", objlist);


						result = "MNSDUmmy-Reply:"+dstMRNs.toString();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
					}
				} else {
					try {
						JSONArray objlist = new JSONArray();
						JSONObject dstMRNs = new JSONObject();
						dstMRNs.put("poll", objlist);


						result = "MNSDUmmy-Reply:"+dstMRNs.toString();

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
					}
				}
			}
		} else if (data.regionMatches(0, "Location-Update:", 0, 16)) {
			try {
				data = data.substring(16);

				//loggerinfo("MNSDummy:data=" + data);
				String[] data_sub = data.split(",");

				if (MRNtoIP.get(data_sub[1]) == null || MRNtoIP.get(data_sub[1]).split(":").length == 3) {
					// data_sub = IP_address, MRN, Port
					MRNtoIP.put(data_sub[1], data_sub[0] + ":" + data_sub[2] + ":" + data_sub[3]);
				}

				result = "OK";
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

		} else if (data.regionMatches(0, "Dump-MNS:", 0, 9)) {
			try {
				if (!MRNtoIP.isEmpty()) {
					SortedSet<String> keys = new TreeSet<String>(MRNtoIP.keySet());
					for (String key : keys) {
						String value = MRNtoIP.get(key);
						String values[] = value.split(":");
						dataToReply = dataToReply + "<tr>"
								+ "<td>" + key + "</td>"
								+ "<td>" + values[0] + "</td>"
								+ "<td>" + values[1] + "</td>"
								+ "<td>" + values[2] + "</td>"
								+ "</tr>";
					}
				} else {
					//loggerdebug("No MRN to IP Mapping.");
					dataToReply = "No";
				}

				result = dataToReply;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

		} else if (data.equals("Empty-MNS:")) {
			try {
				MRNtoIP.clear();
				//loggerwarn("MNSDummy:EMPTY.");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

		} else if (data.regionMatches(0, "Remove-Entry:", 0, 13)) {
			try {
				String mrn = data.substring(13);
				MRNtoIP.remove(mrn);
				//loggerwarn("MNSDummy:REMOVE="+mrn+".");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		} else if (data.regionMatches(0, "Add-Entry:", 0, 10)) {
			try {
				String[] params = data.substring(10).split(",");
				String mrn = params[0];
				String locator = params[1] + ":" + params[2] + ":" + params[3];
				System.out.println(mrn + locator);
				MRNtoIP.put(mrn, locator);
				//loggerwarn("MNSDummy:ADD="+mrn+".");

				//Geo-location update function.

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		} else if (data.regionMatches(0, "Geo-location-Update:", 0, 20)) {
			try {
				//data format: Geo-location-update:
				String[] data_sub = data.split(",");
				//loggerdebug("MNSDummy:Geolocationupdate "+data_sub[1]);
				MRNtoIP.put(data_sub[1], "127.0.0.1" + ":" + data_sub[2] + ":" + data_sub[3] + ":" + data_sub[4]);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		} else if (data.regionMatches(0, "IP-Request:", 0, 11)) {
			try {
				String address = data.substring(11).split(",")[0];
				String[] parseAddress = address.split(":");
				String mrn = null;
				for (String value : MRNtoIP.keySet()) {
					String[] parseValue = MRNtoIP.get(value).split(":");
					if (parseAddress[0].equals(parseValue[0])
							&& parseAddress[1].equals(parseValue[1])) {
						mrn = value;
						break;
					}
				}

				if (mrn == null) {
					dataToReply += "Unregistered MRN in MNS";
				} else {
					dataToReply += mrn;
				}
				result = dataToReply;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}
		return result;

		 */
}

