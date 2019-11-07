package kr.co.nexsys.mcp.homemanager.mms.dao;

import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MMSDao extends JpaRepository<MMSDvo,Long> {
    MMSDvo findOneMMSByMrn(String mrn);
}
