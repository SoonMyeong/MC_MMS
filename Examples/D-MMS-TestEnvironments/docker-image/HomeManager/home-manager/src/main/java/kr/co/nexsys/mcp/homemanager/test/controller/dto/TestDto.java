package kr.co.nexsys.mcp.homemanager.test.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestDto {
    private String department;
    private String name;
    private Integer age;
}
