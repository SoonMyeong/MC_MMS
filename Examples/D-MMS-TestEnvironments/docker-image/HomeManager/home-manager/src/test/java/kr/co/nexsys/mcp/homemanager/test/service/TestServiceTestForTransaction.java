package kr.co.nexsys.mcp.homemanager.test.service;

import kr.co.nexsys.mcp.homemanager.test.dao.TestDao;
import kr.co.nexsys.mcp.homemanager.test.dao.dvo.TestDvo;
import kr.co.nexsys.mcp.homemanager.util.TestDvoDataGenerator;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestServiceTestForTransaction {

    @Autowired
    private TestDao testDao;

    @Autowired
    private TestService testService;


    @Test(expected = Exception.class)
    public void shouldRollback() {
        TestDvo origin = testDao.saveAndFlush(TestDvoDataGenerator.buildTestDvo());
        List<Integer> ids = Lists.list(origin.getId(), null);
        String afterDepartment = "test2";
        try {
            testService.updateDepartment(ids, afterDepartment);
        } catch (Exception e) {
            throw e;
        } finally {
            String actualDepartment = testDao.findById(ids.get(0)).get().getDepartment();
            assertThat(actualDepartment, not(equalTo(afterDepartment)));
            assertThat(actualDepartment, equalTo(origin.getDepartment()));
        }

    }
}
