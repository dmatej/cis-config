package cz.i.cis.config.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: ConfigurationProfile
 */
@Entity
@Table(name = "configuration_profile")
public class ConfigurationProfile implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description = "";

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date update;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private CisUser user;


  public ConfigurationProfile() {
    super();
  }


  public Long getId() {
    return this.id;
  }


  public void setId(Long id) {
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


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((update == null) ? 0 : update.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());

    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof ConfigurationProfile)) {
      return false;
    }

    ConfigurationProfile other = (ConfigurationProfile) obj;
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }

    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }

    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }

    if (update == null) {
      if (other.update != null) {
        return false;
      }
    } else if (!update.equals(other.update)) {
      return false;
    }

    if (user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!user.equals(other.user)) {
      return false;
    }

    return true;
  }
}
