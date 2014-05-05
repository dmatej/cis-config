package cz.i.cis.config.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * Entity implementation class for Entity: ConfigurationProfileItem
 */
@Entity
@Table(name = "configuration_profile_item", uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "key_id"}))
public class ConfigurationProfileItem implements Serializable {

  private static final long serialVersionUID = 1L;


  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "profile_id", nullable = false)
  private ConfigurationProfile profile;

  @ManyToOne
  @JoinColumn(name = "key_id", nullable = false)
  private ConfigurationItemKey key;

  @Column(name = "item_value", nullable = false)
  private String value;

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((profile == null) ? 0 : profile.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());

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

    if (!(obj instanceof ConfigurationProfileItem)) {
      return false;
    }

    ConfigurationProfileItem other = (ConfigurationProfileItem) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }

    if (key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }

    if (profile == null) {
      if (other.profile != null) {
        return false;
      }
    } else if (!profile.equals(other.profile)) {
      return false;
    }

    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }

    return true;
  }


  @Override
  public String toString() {
    return new StringBuilder(getClass().getCanonicalName())
      .append("[id=").append(getId())
      .append(", profile_id=").append(getProfile().getId())
      .append(", key_id=").append(getKey().getId())
      .append(", item_value=").append(getValue())
      .append("]").toString();
  }
}
