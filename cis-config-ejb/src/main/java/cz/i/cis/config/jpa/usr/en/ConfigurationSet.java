package cz.i.cis.config.jpa.usr.en;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationSet
 *
 */
@Entity

public class ConfigurationSet implements Serializable {

	   
	@Id
	private Integer id;
	@ManyToOne
	private CisUser idUserUpdated;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeUpdated;
	private String name;
	private static final long serialVersionUID = 1L;

	public ConfigurationSet() {
		super();
	}   
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	public CisUser getIdUserUpdated() {
		return this.idUserUpdated;
	}

	public void setIdUserUpdated(CisUser idUserUpdated) {
		this.idUserUpdated = idUserUpdated;
	}   
	public Date getTimeUpdated() {
		return this.timeUpdated;
	}

	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
}
