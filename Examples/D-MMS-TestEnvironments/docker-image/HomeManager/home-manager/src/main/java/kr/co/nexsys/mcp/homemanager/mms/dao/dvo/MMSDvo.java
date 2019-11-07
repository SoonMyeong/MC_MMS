package kr.co.nexsys.mcp.homemanager.mms.dao.dvo;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MMS")
@Builder
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MMSDvo {
    @Id
    private String mrn;
    private String url;

    @Column(name = "create_date",updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name="update_date",insertable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;
}
