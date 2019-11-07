package kr.co.nexsys.mcp.mcm.clientmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.nexsys.mcp.mcm.clientmanager.dao.dvo.McmEntityDvo;

public interface McmEntityDao extends JpaRepository<McmEntityDvo, Integer> {
	@Query(value="SELECT * FROM MCM_ENTITY WHERE MRN = :key1", nativeQuery = true)
	McmEntityDvo findByItemIdContainingMcmEntitys(@Param("key1") String key1);

	@Query(value="SELECT * FROM MCM_ENTITY WHERE MRN = :key1", nativeQuery = true)
	McmEntityDvo findByMrn(@Param("key1") String key1);

}
