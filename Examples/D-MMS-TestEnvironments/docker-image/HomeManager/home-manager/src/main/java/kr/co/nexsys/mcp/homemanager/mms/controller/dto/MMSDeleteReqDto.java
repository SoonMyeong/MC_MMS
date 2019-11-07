package kr.co.nexsys.mcp.homemanager.mms.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MMSDeleteReqDto {
    private String certificate;
}
