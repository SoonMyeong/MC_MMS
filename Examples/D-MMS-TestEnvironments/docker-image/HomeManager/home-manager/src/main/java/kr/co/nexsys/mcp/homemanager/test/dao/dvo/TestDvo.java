package kr.co.nexsys.mcp.homemanager.test.dao.dvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_TEST")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TestDvo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String department;
    private Integer age;
    private LocalDateTime createDate;
}
