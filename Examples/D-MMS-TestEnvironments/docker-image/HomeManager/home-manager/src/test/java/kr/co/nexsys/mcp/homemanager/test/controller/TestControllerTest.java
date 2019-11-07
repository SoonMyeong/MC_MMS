package kr.co.nexsys.mcp.homemanager.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.nexsys.mcp.homemanager.test.controller.dto.TestRequestDto;
import kr.co.nexsys.mcp.homemanager.test.service.TestService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
public class TestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TestService testService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldFindTestByDepartment() throws Exception {
        String department = "test";

        TestRequestDto requestDto =
                TestRequestDto.builder()
                        .department(department)
                        .build();

        kr.co.nexsys.mcp.homemanager.test.service.vo.Test test =
                kr.co.nexsys.mcp.homemanager.test.service.vo.Test.builder()
                        .department(department)
                        .age(12)
                        .name("tester")
                        .build();


        given(testService.findTestByDepartment(department)).willReturn(Lists.list(test));

        mockMvc.perform(
                post("/test/find")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("test-language", "ko"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.testDtoList.[0].department", equalTo(test.getDepartment())))
                .andExpect(jsonPath("$.testDtoList.[0].name", equalTo(test.getName())))
                .andExpect(jsonPath("$.testDtoList.[0].age", equalTo(test.getAge())));
    }


}
