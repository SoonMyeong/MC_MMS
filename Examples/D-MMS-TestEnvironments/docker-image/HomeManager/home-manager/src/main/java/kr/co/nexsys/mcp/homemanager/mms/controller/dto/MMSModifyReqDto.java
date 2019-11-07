package kr.co.nexsys.mcp.homemanager.mms.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MMSModifyReqDto {
    private String certificate;
}
