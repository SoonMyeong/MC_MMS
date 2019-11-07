package kr.co.nexsys.mcp.mcm.geocasting.service;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.nexsys.mcp.mcm.clientmanager.dao.dvo.McmEntityDvo;
import kr.co.nexsys.mcp.mcm.geocasting.dao.CoreTRDao;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.geocasting.service.vo.CoreTR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class GeocastCircleService {

	private final CoreTRDao coreTRDao;

	public GeocastCircleService(CoreTRDao coreTRDao) {
		this.coreTRDao = coreTRDao;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<CoreTR> findCoreTR(int positionNo) {
		return coreTRDao.findById(positionNo).map(GeocastCircleService::valueOf);
	}

	public List<CoreTRDvo> findByItemNameOrIdContainingCoreTRs(
	double key1,
	double key2,
	double key3,
	double key4
	) {
	  return ((List<CoreTRDvo>) coreTRDao.findByItemNameOrIdContainingCoreTRs(key1,key2,key3,key4));///findByLatGreaterThanEqual(key1));
	}
	
	public List<CoreTRDvo> findByMrn(String key1) {
		return ((List<CoreTRDvo>) coreTRDao.findByMrn(key1));
	}
	
	@Transactional
	public void deleteCoreTR(Map<String, Object> msgMap) throws Exception{
		final CoreTRDvo dvo = new CoreTRDvo();
		dvo.setMrn (msgMap.get("delMrn").toString());
		coreTRDao.delete(dvo);
	}
	
	@Transactional
	public CoreTRDvo saveCoreTR(Map<String, Object> msgMap) {
		final CoreTRDvo dvo = new CoreTRDvo();
		List<CoreTRDvo> listDvo = new ArrayList<CoreTRDvo>();
		listDvo = coreTRDao.findByMrn(msgMap.get("mrn").toString());
		if(listDvo != null && listDvo.get(0) !=null) {
			System.out.println("aleady exists in CORE_T_R...");
			return new CoreTRDvo();
		}else {
			dvo.setCommNo(0);
			dvo.setMrn (msgMap.get("mrn").toString());
			// String tp = msgMap.get("type").toString().toUpperCase();
			// dvo.setClientType(tp.equals("SC")?"1":tp.equals("SP")?"2":"unknown type");
			return coreTRDao.save(dvo);
		}
		
	}


  public void updateTestData(List<Integer> positionNos, String testData){
    for(int positionNo : positionNos){
      CoreTRDvo dvo =  coreTRDao.findById(positionNo).get();
      dvo.setMrn(testData);
      coreTRDao.saveAndFlush(dvo);
    }
  }

  public static CoreTR valueOf(CoreTRDvo dvo) {
    return CoreTR.builder()
        .commNo(dvo.getCommNo())
        .gateway(dvo.getGateway())
        .latitude(dvo.getLatitude())
        .longitude(dvo.getLongitude())
        .macAddress(dvo.getMacAddress())
        .mmsi(dvo.getMmsi())
        .mrn(dvo.getMrn())
        .portNumber(dvo.getPortNumber())
        .positionDate(dvo.getPositionDate())
        .sIp(dvo.getSIp())
        .positionNo(dvo.getId())
        .build();
  }

  public static CoreTRDvo valueOf(CoreTR dvo) {
    return CoreTRDvo.builder()
        .commNo(dvo.getCommNo())
        .gateway(dvo.getGateway())
        .latitude(dvo.getLatitude())
        .longitude(dvo.getLongitude())
        .macAddress(dvo.getMacAddress())
        .mmsi(dvo.getMmsi())
        .mrn(dvo.getMrn())
        .portNumber(dvo.getPortNumber())
        .positionDate(dvo.getPositionDate())
        .sIp(dvo.getSIp())
        .id(dvo.getPositionNo())
        .build();
  }
}
