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
 * Entity implementation class for {@code ConfigurationProfileItem}.
 *
 * @author David Matějček
 * @author Mr.FrAnTA (Michal Dékány)
 */
@Entity
@Table(name = "configuration_profile_items", uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "key_id"}))
public class ConfigurationProfileItem implements Comparable<ConfigurationProfileItem>, Serializable {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = 8974664687352768035L;

  /** Identifier number of configuration profile item. */
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Configuration profile which contains this item. */
  @ManyToOne
  @JoinColumn(name = "profile_id", nullable = false)
  private ConfigurationProfile profile;

  /** Configuration item key of configuration profile item. */
  @ManyToOne
  @JoinColumn(name = "key_id", nullable = false)
  private ConfigurationItemKey key;

  /** Value of configuration profile item. */
  @Column(name = "item_value", nullable = false)
  private String value;


  /**
   * Constructs configuration profile item.
   */
  public ConfigurationProfileItem() {
  }


  /**
   * Returns identifier number of this profile item.
   *
   * @return Identifier number of this profile item.
   */
  public Integer getId() {
    return this.id;
  }


  /**
   * Sets identifier number of this profile item.
   *
   * @param id identifier number of this profile item to set.
   */
  public void setId(Integer id) {
    this.id = id;
  }


  /**
   * Returns configuration profile of this item.
   *
   * @return Configuration profile of this item.
   */
  public ConfigurationProfile getProfile() {
    return this.profile;
  }


  /**
   * Sets configuration profile of this item.
   *
   * @param profile configuration profile of this item to set.
   */
  public void setProfile(ConfigurationProfile profile) {
    this.profile = profile;
  }


  /**
   * Returns configuration item key of this profile item.
   *
   * @return Configuration item key of this profile item.
   */
  public ConfigurationItemKey getKey() {
    return this.key;
  }


  /**
   * Sets configuration item key of this profile item.
   *
   * @param key configuration item key of this profile item to set.
   */
  public void setKey(ConfigurationItemKey key) {
    this.key = key;
  }


  /**
   * Returns value of profile item.
   *
   * @return Value of profile item.
   */
  public String getValue() {
    return this.value;
  }


  /**
   * Sets value of profile item.
   *
   * @param value value of profile item to set.
   */
  public void setValue(String value) {
    this.value = value;
  }


  /**
   * Compares this object with the entered object for order. Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the entered object.
   *
   * @param item the configuration profile item to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the entered object.
   */
  @Override
  public int compareTo(ConfigurationProfileItem item) {
    if(item == null) {
      return 1;
    }

    int id1 = this.getId() == null ? Integer.MIN_VALUE : this.getId();
    int id2 = item.getId() == null ? Integer.MIN_VALUE : item.getId();

    return Integer.compare(id1, id2);
  }

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
   *
   * @return A hash code value for this object.
   */
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


  /**
   * Returns string value of configuration profile item in format:
   *
   * <pre>
   * ConfigurationProfileItem[id=%id, profile_id=%profile.id, key_id=%key.id, item_value=%value]
   * </pre>
   *
   * @return String value of configuration profile item.
   */
  @Override
  public String toString() {
    return new StringBuilder(getClass().getCanonicalName())
        .append("[id=").append(getId())
        .append(", profile_id=").append( getProfile().getId())
        .append(", key_id=").append(getKey().getId())
        .append(", item_value=").append(getValue())
        .append("]").toString();
  }
}
