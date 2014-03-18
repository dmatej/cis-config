package cz.i.cis.config.jpa;

import cz.i.cis.config.jpa.CisUser;

import java.io.Serializable;
import java.lang.String;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ConfigurationProfile
 */
@Entity
@Table(name = "configuration_profile")
public class ConfigurationProfile implements Serializable {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description = "";

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date update;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
  private Collection<ConfigurationProfileItem> configurationProfileItems;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private CisUser user;

  private static final long serialVersionUID = 1L;


  public ConfigurationProfile() {
    super();
  }


  public long getId() {
    return this.id;
  }


  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return this.name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getDescription() {
    return this.description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public Date getUpdate() {
    return this.update;
  }


  public void setUpdate(Date update) {
    this.update = update;
  }


  public CisUser getUser() {
    return this.user;
  }


  public void setUser(CisUser user) {
    this.user = user;
  }

  public Collection<ConfigurationProfileItem> getConfigurationProfileItems() {
    return configurationProfileItems;
  }

  public void setConfigurationProfileItems(Collection<ConfigurationProfileItem> configurationProfileItems) {
    this.configurationProfileItems = configurationProfileItems;
  }
}
