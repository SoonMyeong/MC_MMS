package kr.co.nexsys.mcp.homemanager.test.service.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private Integer id;
    private String name;
    private String department;
    private Integer age;
    private LocalDateTime createDate;
}
