package kr.co.nexsys.mcp.homemanager.entity.service.vo;


import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HomeMMS {
    private String mrn;
    private String homeMmsMrn;
    private String type;
}
