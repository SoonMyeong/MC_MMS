package kr.co.nexsys.mcp.homemanager.test.dao;

import kr.co.nexsys.mcp.homemanager.test.dao.dvo.TestDvo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Observable;


public interface TestDao extends JpaRepository<TestDvo, Integer> {
    List<TestDvo> findAllByDepartment(String department);
}
