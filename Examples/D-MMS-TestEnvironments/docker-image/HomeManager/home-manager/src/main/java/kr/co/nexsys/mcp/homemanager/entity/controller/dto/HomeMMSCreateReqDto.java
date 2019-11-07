package kr.co.nexsys.mcp.homemanager.entity.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HomeMMSCreateReqDto {
    @NotEmpty @Pattern(regexp = "(urn:mrn:kr:)[a-z]\\w{0,6}:[a-z]\\w{0,10}:.*")
    private String mrn;
    @NotEmpty
    private String type;
    @NotEmpty
    private String certificate;
}
