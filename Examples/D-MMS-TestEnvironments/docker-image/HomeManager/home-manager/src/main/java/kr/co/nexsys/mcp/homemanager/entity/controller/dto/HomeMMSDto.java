package kr.co.nexsys.mcp.homemanager.entity.controller.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HomeMMSDto {
    @NotEmpty @Pattern(regexp = "(urn:mrn:kr:)[a-z]\\w{0,6}:[a-z]\\w{0,10}:.*")
    private String mrn;
    @NotEmpty
    private String type;
    @NotEmpty @Pattern(regexp = "(urn:mrn:kr:)[a-z]\\w{0,6}:[a-z]\\w{0,10}:.*")
    private String homeMmsMrn;
}
