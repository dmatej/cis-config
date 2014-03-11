package cz.i.cis.config.jpa.usr.en;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationSetItem
 *
 */
@Entity
@Table(name="ConfigurationSetItem", 
uniqueConstraints=@UniqueConstraint(columnNames={"key", "idConfigurationSet"}))
public class ConfigurationSetItem implements Serializable {

	   
	@Id
	private Integer id;
	private String key;
	private Object value;
	private Type type;
	@ManyToOne
	private ConfigurationSet idConfigurationSet;
	private static final long serialVersionUID = 1L;

	public ConfigurationSetItem() {
		super();
	}   
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}   
	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}   
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}   
	public ConfigurationSet getIdConfigurationSet() {
		return this.idConfigurationSet;
	}

	public void setIdConfigurationSet(ConfigurationSet idConfigurationSet) {
		this.idConfigurationSet = idConfigurationSet;
	}
   
}
