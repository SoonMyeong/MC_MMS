package kr.co.nexsys.mcp.homemanager.entity.dao.dvo;


import kr.co.nexsys.mcp.homemanager.mms.dao.MMSDao;
import kr.co.nexsys.mcp.homemanager.mms.dao.dvo.MMSDvo;
import kr.co.nexsys.mcp.homemanager.mms.service.vo.MMS;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="ENTITY_HOME_MMS")
@Builder
@Entity
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeMMSDvo {
    @Id
    private String mrn;
    private String mrn_mms;
    private String type;
    @CreationTimestamp @Column(name = "create_date",updatable = false)
    private LocalDateTime create_date;
    @UpdateTimestamp @Column(name="update_date",insertable = false)
    private LocalDateTime update_date;
    @ManyToOne @JoinColumn(name= "mrn_mms", insertable = false , updatable = false)
    private MMSDvo mmsDvo;
}
