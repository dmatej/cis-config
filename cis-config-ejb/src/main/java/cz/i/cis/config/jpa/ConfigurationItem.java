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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for {@code ConfigurationItem}.
 */
@Entity
@Table(name = "configuration_items")
public class ConfigurationItem implements Comparable<ConfigurationItem>, Serializable {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = 7710574135022457359L;

  /** Identifier number of configuration item. */
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Key of configuration item. */
  @OneToOne
  @JoinColumn(name = "key_id", nullable = false, unique = true)
  private ConfigurationItemKey key;

  /** Value of configuration item. */
  @Column(name = "item_value", nullable = false)
  private String value;

  /** Timestamp of last update of configuration item. */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date update;

  /** CIS user which last changed configuration item. */
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private CisUser user;


  /**
   * Returns identifier number of this configuration item.
   *
   * @return Identifier number of this configuration item.
   */
  public Integer getId() {
    return id;
  }


  /**
   * Sets identifier number of this configuration item.
   *
   * @param id identifier number of this configuration item to set.
   */
  public void setId(Integer id) {
    this.id = id;
  }


  /**
   * Returns key of this configuration item.
   *
   * @return Key of this configuration item.
   */
  public ConfigurationItemKey getKey() {
    return key;
  }


  /**
   * Sets key of this configuration item.
   *
   * @param key key of this configuration item to set.
   */
  public void setKey(ConfigurationItemKey key) {
    this.key = key;
  }


  /**
   * Returns value of this configuration item.
   *
   * @return Value of this configuration item.
   */
  public String getValue() {
    return value;
  }


  /**
   * Sets value of this configuration item.
   *
   * @param value value of this configuration item to set.
   */
  public void setValue(String value) {
    this.value = value;
  }


  /**
   * Returns timestamp of last update of this configuration item.
   *
   * @return Timestamp of last update of this configuration item.
   */
  public Date getUpdate() {
    return update;
  }


  /**
   * Sets timestamp of last update of this configuration item.
   *
   * @param update timestamp of last update of this configuration item to set.
   */
  public void setUpdate(Date update) {
    this.update = update;
  }


  /**
   * Returns CIS user which last changed this configuration item.
   *
   * @return CIS user which last changed this configuration item.
   */
  public CisUser getUser() {
    return user;
  }


  /**
   * Sets CIS user which last changed configuration item.
   *
   * @param user CIS user which last changed this configuration item to set.
   */
  public void setUser(CisUser user) {
    this.user = user;
  }


  /**
   * Compares this object with the entered object for order. Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the entered object.
   *
   * @param item the configuration item to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   *         is less than, equal to, or greater than the entered object.
   */
  @Override
  public int compareTo(ConfigurationItem item) {
    if (item == null) {
      return 1;
    }

    int id1 = this.getId() == null ? Integer.MIN_VALUE : this.getId();
    int id2 = item.getId() == null ? Integer.MIN_VALUE : item.getId();

    return Integer.compare(id1, id2);
  }


  /**
   * Returns a hash code value for the object. This method is supported for the benefit of hash
   * tables such as those provided by {@link java.util.HashMap}.
   *
   * @return A hash code value for this object.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((update == null) ? 0 : update.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());

    return result;
  }


  /**
   * Indicates whether some object is "equal to" this one.
   *
   * @param obj the reference object with which to compare.
   * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ConfigurationItem)) {
      return false;
    }

    ConfigurationItem other = (ConfigurationItem) obj;
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

    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }

    return true;
  }


  /**
   * Returns string value of configuration item in format:
   *
   * <pre>
   * ConfigurationProfileItem[id=%id, key_id=%key.id, item_value=%value, update=%update, user_id=%user.id]
   * </pre>
   *
   * @return String value of configuration item.
   */
  @Override
  public String toString() {
    return new StringBuilder(getClass().getCanonicalName())
      .append("[id=").append(getId())
      .append(", key_id=").append(getKey().getId())
      .append(", item_value=").append(getValue())
      .append(", update=").append(getUpdate().toString())
      .append(", user_id=").append(getUser().getId())
      .append("]").toString();
  }
}
