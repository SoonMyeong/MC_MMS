package kr.co.nexsys.mcp.homemanager.util;

import kr.co.nexsys.mcp.homemanager.test.dao.dvo.TestDvo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
public class TestDvoDataGenerator {

    public final static String defaultDepartment = "test";
    public final static Random random = new Random(System.currentTimeMillis());

    public static TestDvo buildTestDvo() {
        return buildTestDvo(defaultDepartment);
    }

    public static TestDvo buildTestDvo(String department) {
        return TestDvo.builder()
                .department(department)
                .name("tester" + random.nextInt())
                .age(random.nextInt(100))
                .createDate(LocalDateTime.now())
                .build();
    }
}
