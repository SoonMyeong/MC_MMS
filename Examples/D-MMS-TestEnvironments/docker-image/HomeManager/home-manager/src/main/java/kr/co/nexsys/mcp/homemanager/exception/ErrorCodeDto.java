package kr.co.nexsys.mcp.homemanager.exception;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCodeDto {
    private String code;
    private String message;
}
