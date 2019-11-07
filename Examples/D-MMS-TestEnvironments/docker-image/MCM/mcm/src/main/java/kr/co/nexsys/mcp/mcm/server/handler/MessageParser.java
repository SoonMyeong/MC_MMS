package kr.co.nexsys.mcp.mcm.server.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class MessageParser {
	
	/**
	 * client message
	 */
	private String rcvMsg;
	
	/**
	 * circle json
	 */
	private Map<String, Object> circleMap ;
	/**
	 * polygon json
	 */
	private Map<String, Object> polygonMap;
	/**
	 * unicasting json
	 */
	private Map<String, Object> unicastMap;
	
	private Map<String, Object> callHomeManagerMap;
	
	MessageParser(String rcvMsg) {
		
		this.rcvMsg     = rcvMsg;
		this.circleMap  = new ConcurrentHashMap<>();
		this.polygonMap = new ConcurrentHashMap<>();
		this.unicastMap = new ConcurrentHashMap<>();
		this.callHomeManagerMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * circle?polygon? unicasting?
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getObjectType() {
		Map<String, Object> returnMap = new ConcurrentHashMap<>();
		JsonParser springParser = JsonParserFactory.getJsonParser();
		Map<String, Object> map = new ConcurrentHashMap<>();
		
		map = springParser.parseMap(this.rcvMsg);
		callHomeManagerMap = (Map<String, Object>) map.get("callHomeManager");
		
		if(null != callHomeManagerMap ) {
			returnMap = callHomeManagerMap;
			returnMap.put("mapType", "CALL_HOMEMANAGER");
		}else {
			circleMap = (Map<String, Object>) map.get("geocasting_circle");
			
			polygonMap = (Map<String, Object>) map.get("geocasting_polygon");
			
			unicastMap = (Map<String, Object>) map.get("unicasting");
			if(null != circleMap ) {
				returnMap = circleMap;
				returnMap.put("mapType", "CIRCLE");
				log.debug("circleMap==========={}", circleMap);
			}else
			if(null != polygonMap ) {
				returnMap = polygonMap;
				returnMap.put("mapType", "POLYGON");
				log.debug("polygonMap=========={}", polygonMap);
			}else
			if(null != unicastMap ) {
				returnMap = unicastMap;
				returnMap.put("mapType", "UNICASTING");
				log.debug("unicastMap==========={}", unicastMap);
			}
		}
		
		return returnMap;
	}
}
