package cz.i.cis.config.jpa;

import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfile;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationProfileItem
 */
@Entity
@Table(name = "configuration_profile_item")
public class ConfigurationProfileItem implements Serializable {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "profile_id", nullable = false)
  private ConfigurationProfile profile;

  @ManyToOne
  @JoinColumn(name = "key_id", nullable = false)
  private ConfigurationItemKey key;

  @Column(name = "item_value", nullable = false)
  private String value;

  private static final long serialVersionUID = 1L;


  public ConfigurationProfileItem() {
    super();
  }


  public Long getId() {
    return this.id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public ConfigurationProfile getProfile() {
    return this.profile;
  }


  public void setProfile(ConfigurationProfile profile) {
    this.profile = profile;
  }


  public ConfigurationItemKey getKey() {
    return this.key;
  }


  public void setKey(ConfigurationItemKey key) {
    this.key = key;
  }


  public String getValue() {
    return this.value;
  }


  public void setValue(String value) {
    this.value = value;
  }

}
