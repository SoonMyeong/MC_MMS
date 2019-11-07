package kr.co.nexsys.mcp.homemanager.test.dao;

import kr.co.nexsys.mcp.homemanager.test.dao.dvo.TestDvo;
import kr.co.nexsys.mcp.homemanager.util.TestDvoDataGenerator;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static kr.co.nexsys.mcp.homemanager.util.TestDvoDataGenerator.defaultDepartment;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class TestDaoTest {

    @Autowired
    private TestDao dao;

    @Test
    public void shouldSave() {
        TestDvo dvo = TestDvoDataGenerator.buildTestDvo();
        assertThat(dvo.getId(), nullValue());
        dao.saveAndFlush(dvo);
        assertThat(dvo.getId(), notNullValue());
    }


    @Test
    public void shouldFindById() {
        TestDvo origin = TestDvoDataGenerator.buildTestDvo();
        dao.save(origin);

        Optional<TestDvo> actual = dao.findById(origin.getId());
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get().getId(), is(origin.getId()));
    }

    @Test
    public void shouldDelete() {
        TestDvo origin = dao.save(TestDvoDataGenerator.buildTestDvo());
        assertThat(dao.findById(origin.getId()).isPresent(), is(true));
        dao.delete(origin);
        assertThat(dao.findById(origin.getId()).isPresent(), is(false));
    }


    @Test
    public void shouldfindAllByDepartment() {
        List<TestDvo> origin =
                dao.saveAll(Lists.list(TestDvoDataGenerator.buildTestDvo(), TestDvoDataGenerator.buildTestDvo()));

        List<TestDvo> actual = dao.findAllByDepartment(defaultDepartment);
        assertThat(actual.stream().filter(origin::contains).count(), is(2L));

    }


}
