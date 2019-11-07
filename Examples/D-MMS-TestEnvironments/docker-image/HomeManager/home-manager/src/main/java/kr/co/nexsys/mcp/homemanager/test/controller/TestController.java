package kr.co.nexsys.mcp.homemanager.test.controller;

import kr.co.nexsys.mcp.homemanager.test.controller.dto.TestDto;
import kr.co.nexsys.mcp.homemanager.test.controller.dto.TestRequestDto;
import kr.co.nexsys.mcp.homemanager.test.controller.dto.TestResponseDto;
import kr.co.nexsys.mcp.homemanager.test.service.TestService;
import kr.co.nexsys.mcp.homemanager.test.service.vo.Test;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    private TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    private static TestDto valueOf(Test test) {
        return TestDto.builder()
                .age(test.getAge())
                .name(test.getName())
                .department(test.getDepartment())
                .build();
    }

    @PostMapping("/find")
    public TestResponseDto findTest(@RequestHeader("test-language") String testLang,
                                    @RequestBody TestRequestDto requestDto) {
        log.debug("test-language: {}", testLang);
        log.debug(requestDto.toString());
        return TestResponseDto.builder()
                .testDtoList(testService.findTestByDepartment(requestDto.getDepartment()).stream()
                        .map(TestController::valueOf)
                        .collect(Collectors.toList())).build();
    }
}
