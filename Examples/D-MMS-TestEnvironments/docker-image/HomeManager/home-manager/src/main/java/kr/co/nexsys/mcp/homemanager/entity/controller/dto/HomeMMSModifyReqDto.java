package kr.co.nexsys.mcp.homemanager.entity.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeMMSModifyReqDto {
    @NotEmpty @Pattern(regexp = "(urn:mrn:kr:)[a-z]\\w{0,6}:[a-z]\\w{0,10}:.*")
    private String homeMmsMrn;
}
