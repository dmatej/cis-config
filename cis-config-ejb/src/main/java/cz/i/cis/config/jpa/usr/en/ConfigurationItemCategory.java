package cz.i.cis.config.jpa.usr.en;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationItemCategory
 */
@Entity
@Table(name = "configuration_item_category")
public class ConfigurationItemCategory implements Serializable {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  private static final long serialVersionUID = 1L;


  public ConfigurationItemCategory() {
    super();
  }


  public Integer getId() {
    return this.id;
  }


  public void setId(Integer id) {
    this.id = id;
  }


  public String getName() {
    return this.name;
  }


  public void setName(String name) {
    this.name = name;
  }
}
