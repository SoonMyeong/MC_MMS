package kr.co.nexsys.mcp.homemanager.test.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TestResponseDto {
    private List<TestDto> testDtoList;
}
