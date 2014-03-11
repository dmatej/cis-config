package cz.i.cis.config.jpa.usr.en;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationItem
 */
@Entity
public class ConfigurationItem implements Serializable {

  @Id
  private Integer id;
  @Column(unique = true)
  private String key;
  private String value;
  private Type type;
  private static final long serialVersionUID = 1L;


  public ConfigurationItem() {
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


  public String getValue() {
    return this.value;
  }


  public void setValue(String value) {
    this.value = value;
  }


  public Object getType() {
    return this.type;
  }


  public void setType(Type type) {
    this.type = type;
  }
}
