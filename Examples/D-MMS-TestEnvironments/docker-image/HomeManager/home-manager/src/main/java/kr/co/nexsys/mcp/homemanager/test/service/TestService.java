package kr.co.nexsys.mcp.homemanager.test.service;

import kr.co.nexsys.mcp.homemanager.test.dao.TestDao;
import kr.co.nexsys.mcp.homemanager.test.dao.dvo.TestDvo;
import kr.co.nexsys.mcp.homemanager.test.service.vo.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class TestService {

    private TestDao testDao;

    @Autowired
    public TestService(TestDao testDao) {
        this.testDao = testDao;
    }

    private static Test valueOf(TestDvo dvo) {
        return Test.builder()
                .id(dvo.getId())
                .name(dvo.getName())
                .age(dvo.getAge())
                .department(dvo.getDepartment())
                .createDate(dvo.getCreateDate())
                .build();
    }

    private static TestDvo valueOf(Test dvo) {
        return TestDvo.builder()
                .id(dvo.getId())
                .name(dvo.getName())
                .age(dvo.getAge())
                .department(dvo.getDepartment())
                .createDate(dvo.getCreateDate())
                .build();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Test> findTestByDepartment(String department) {
        return testDao.findAllByDepartment(department).stream()
                .map(TestService::valueOf)
                .collect(Collectors.toList());
    }

    public void updateDepartment(List<Integer> ids, String department) {
        for (int id : ids) {
            TestDvo dvo = testDao.findById(id).get();
            dvo.setDepartment(department);
            testDao.saveAndFlush(dvo);
        }
    }
}
