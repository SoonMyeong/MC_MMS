package kr.co.nexsys.mcp.homemanager.mms.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MMSFindAllResDto {
    private List<MMSDto> MMSList;
}
