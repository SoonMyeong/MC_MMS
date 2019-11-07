package kr.co.nexsys.mcp.homemanager.mms.controller.dto;

import lombok.*;

import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MMSDto {
    @NotEmpty @Pattern(regexp = "(urn:mrn:kr:)[a-z]\\w{0,6}:[a-z]\\w{0,10}:.*")
    private String mrn;
    @NotEmpty
    private String url;
}
