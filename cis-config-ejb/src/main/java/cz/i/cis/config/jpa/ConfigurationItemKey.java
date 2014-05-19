package cz.i.cis.config.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity implementation class for {@code ConfigurationItemKey}.
 */
@Entity
@Table(name = "configuration_item_keys")
public class ConfigurationItemKey implements Comparable<ConfigurationItemKey>, Serializable {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = -5212516150429420381L;

  /** Identifier number of configuration item key. */
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Configuration item category of this key. */
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private ConfigurationItemCategory category;

  /** Type of configuration item key. */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ConfigurationItemKeyType type;

  /** Name of configuration item key. */
  @Column(nullable = false, unique = true)
  private String key;

  /** Description for configuration item key. */
  @Column(nullable = false)
  private String description = "";


  /**
   * Returns identifier number of this configuration item key.
   *
   * @return Identifier number of this configuration item key.
   */
  public Integer getId() {
    return this.id;
  }


  /**
   * Sets identifier number of this configuration item key.
   *
   * @param id identifier number of this configuration item key to set.
   */
  public void setId(Integer id) {
    this.id = id;
  }


  /**
   * Returns configuration item category of this key.
   *
   * @return Configuration item category of this key.
   */
  public ConfigurationItemCategory getCategory() {
    return this.category;
  }


  /**
   * Sets configuration item category of this key.
   *
   * @param category configuration item category of this key to set.
   */
  public void setCategory(ConfigurationItemCategory category) {
    this.category = category;
  }


  /**
   * Returns type of this configuration item key.
   *
   * @return Type of this configuration item key.
   */
  public ConfigurationItemKeyType getType() {
    return this.type;
  }


  /**
   * Returns type of this configuration item key.
   *
   * @param type type of this configuration item key to set.
   */
  public void setType(ConfigurationItemKeyType type) {
    this.type = type;
  }


  /**
   * Returns name of configuration item key.
   *
   * @param key name of configuration item key
   */
  public String getKey() {
    return this.key;
  }


  /**
   * Sets name of this configuration item key.
   *
   * @param key name of this configuration item key to set.
   */
  public void setKey(String key) {
    this.key = key;
  }


  /**
   * Returns description for this configuration item key.
   *
   * @return Description for this configuration item key.
   */
  public String getDescription() {
    return this.description;
  }


  /**
   * Sets description for this configuration item key.
   *
   * @param description description for this configuration item key to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }


  /**
   * Compares this object with the entered object for order. Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the entered object.
   *
   * @param key the configuration item key to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   *         is less than, equal to, or greater than the entered object.
   */
  @Override
  public int compareTo(ConfigurationItemKey key) {
    if (key == null) {
      return 1;
    }

    int id1 = this.getId() == null ? Integer.MIN_VALUE : this.getId();
    int id2 = key.getId() == null ? Integer.MIN_VALUE : key.getId();

    return Integer.compare(id1, id2);
  }


  /**
   * Returns a hash code value for the object. This method is supported for the benefit
   * of hash tables such as those provided by {@link java.util.HashMap}.
   *
   * @return A hash code value for this object.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());

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

    if (!(obj instanceof ConfigurationItemKey)) {
      return false;
    }

    ConfigurationItemKey other = (ConfigurationItemKey) obj;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }

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

    if (key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }

    if (type != other.type) {
      return false;
    }

    return true;
  }


  /**
   * Returns string value of configuration item key in format:
   *
   * <pre>
   * ConfigurationItemKey[id=%id, category_id=%category.id, type=%type, key=%key, description=%user.id]
   * </pre>
   *
   * @return String value of configuration item key.
   */
  @Override
  public String toString() {
    return new StringBuilder(getClass().getCanonicalName())
      .append("[id=").append(getId())
      .append(", category_id=").append(getCategory().getId())
      .append(", type=").append(getType())
      .append(", key=").append(getKey())
      .append(", description=").append(getDescription())
      .append("]").toString();
  }
}
