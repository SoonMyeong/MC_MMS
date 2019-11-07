package kr.co.nexsys.mcp.mcm.clientmanager.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nexsys.mcp.mcm.clientmanager.dao.McmEntityDao;
import kr.co.nexsys.mcp.mcm.clientmanager.dao.dvo.McmEntityDvo;

/**
 * Client : MMS's client as SC, SP
 * @param mcmEntityDao
 */
//@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class ClientManagerService {

	/**
	 * 
	 */
	private final McmEntityDao mcmEntityDao;

	/**
	 * 
	 * @param mcmEntityDao
	 */
	public ClientManagerService(final McmEntityDao mcmEntityDao) {
		this.mcmEntityDao = mcmEntityDao;
	}

	/**
	 * 
	 * @param key1
	 * @return
	 */
	public McmEntityDvo findByItemIdContainingMcmEntitys(final String key1) {
		return (McmEntityDvo) mcmEntityDao.findByItemIdContainingMcmEntitys(key1);
	}
	
	/**
	 * 
	 * @param key1
	 * @return
	 */
	public McmEntityDvo findByMrn(final String key1) {
		return (McmEntityDvo) mcmEntityDao.findByMrn(key1);
	}

	/**
	 * 
	 * @param dvo
	 * @return
	 */
	public static McmEntityDvo valueOf(final McmEntityDvo dvo) {
		return McmEntityDvo.builder()
				.mrn(dvo.getMrn())
				.port(dvo.getPort())
				.ip(dvo.getIp())
				.type(dvo.getType())
				.build();
	}

	/**
	 * 
	 * @param msgMap
	 * @throws UnknownError
	 */
	@Transactional
	public McmEntityDvo insertMcmEntity(final Map<String, Object> msgMap) throws UnknownError{
		final McmEntityDvo dvo = new McmEntityDvo();
		
		dvo.setMrn (msgMap.get("mrn").toString());
		dvo.setIp  (msgMap.get("ip").toString());
		dvo.setPort(msgMap.get("port").toString());
		String tp = msgMap.get("type").toString().toUpperCase();
		dvo.setType(tp.equals("SC")?"1":tp.equals("SP")?"2":"unknown type");
		return mcmEntityDao.save(dvo);
	}

	@Transactional
	public void deleteMcmEntity(Map<String, Object> msgMap) throws Exception{
		final McmEntityDvo dvo = new McmEntityDvo();
		dvo.setMrn (msgMap.get("delMrn").toString());
		mcmEntityDao.delete(dvo);
	}

	public McmEntityDvo updateMcmEntity(Map<String, Object> msgMap) {
		final McmEntityDvo dvo = new McmEntityDvo();
		
		dvo.setMrn (msgMap.get("mrn").toString());
		dvo.setIp  (msgMap.get("ip").toString());
		dvo.setPort(msgMap.get("port").toString());
		String tp = msgMap.get("type").toString().toUpperCase();
		dvo.setType(tp.equals("SC")?"1":tp.equals("SP")?"2":"unknown type");
		return mcmEntityDao.save(dvo);
	}
}
