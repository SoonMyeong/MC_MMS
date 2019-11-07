package kr.co.nexsys.mcp.mcm.clientmanager.dao.dvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mcm_entity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class McmEntityDvo {
	@Id
	private String mrn;
	private String ip;
	private String port;
	private String type;
}
