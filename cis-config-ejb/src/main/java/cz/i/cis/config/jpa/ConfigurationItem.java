package cz.i.cis.config.jpa;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationItem
 */
@Entity
@Table(name = "configuration_item")
public class ConfigurationItem implements Serializable {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "key_id", nullable = false)
  private ConfigurationItemKey key;

  @Column(name = "item_value", nullable = false)
  private String value;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date update;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private CisUser user;

  private static final long serialVersionUID = 1L;


  public ConfigurationItem() {
    super();
  }


  public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public ConfigurationItemKey getKey() {
    return key;
  }


  public void setKey(ConfigurationItemKey key) {
    this.key = key;
  }


  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }


  public Date getUpdate() {
    return update;
  }


  public void setUpdate(Date update) {
    this.update = update;
  }


  public CisUser getUser() {
    return user;
  }


  public void setUser(CisUser user) {
    this.user = user;
  }
}
