package kr.co.nexsys.mcp.homemanager.test.service;

import kr.co.nexsys.mcp.homemanager.test.dao.TestDao;
import kr.co.nexsys.mcp.homemanager.test.dao.dvo.TestDvo;
import kr.co.nexsys.mcp.homemanager.util.TestDvoDataGenerator;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TestServiceTest {

    @Mock
    private TestDao testDao;

    @InjectMocks
    private TestService testService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldfindTestByDepartment() {
        TestDvo originDvo = TestDvoDataGenerator.buildTestDvo();
        when(testDao.findAllByDepartment(any())).thenReturn(Lists.list(originDvo));

        List<kr.co.nexsys.mcp.homemanager.test.service.vo.Test> actual =
                testService.findTestByDepartment(originDvo.getDepartment());
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getDepartment(), equalTo(originDvo.getDepartment()));
        assertThat(actual.get(0).getAge(), equalTo(originDvo.getAge()));
        assertThat(actual.get(0).getName(), equalTo(originDvo.getName()));
    }


}
