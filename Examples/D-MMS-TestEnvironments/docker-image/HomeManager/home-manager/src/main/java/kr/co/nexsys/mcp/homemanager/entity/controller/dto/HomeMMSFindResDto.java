package kr.co.nexsys.mcp.homemanager.entity.controller.dto;

import kr.co.nexsys.mcp.homemanager.mms.controller.dto.MMSDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HomeMMSFindResDto {
    private String mrn;
    private String type;
    private MMSDto homeMmsDto;
}
