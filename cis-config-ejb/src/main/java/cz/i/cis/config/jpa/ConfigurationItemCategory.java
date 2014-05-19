package cz.i.cis.config.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Entity implementation class for {@code ConfigurationItemCategory}.
 */
@Entity
@Table(name = "configuration_item_categories")
public class ConfigurationItemCategory implements Comparable<ConfigurationItemCategory>, Serializable {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = -8775121697236124630L;

  /** Identifier number of configuration item category. */
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Name of configuration item category. */
  @Column(nullable = false, unique = true)
  private String name;


  /**
   * Returns identifier number of this configuration item category.
   *
   * @return Identifier number of this configuration item category.
   */
  public Integer getId() {
    return this.id;
  }


  /**
   * Sets identifier number of this configuration item category.
   *
   * @param id identifier number of this configuration item category to set.
   */
  public void setId(Integer id) {
    this.id = id;
  }


  /**
   * Returns name of this configuration item category.
   *
   * @return Name of this configuration item category.
   */
  public String getName() {
    return this.name;
  }


  /**
   * Returns name of this configuration item category.
   *
   * @param name name of this configuration item category to set.
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Compares this object with the entered object for order. Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the entered object.
   *
   * @param category the configuration item category to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   *         is less than, equal to, or greater than the entered object.
   */
  @Override
  public int compareTo(ConfigurationItemCategory category) {
    if (category == null) {
      return 1;
    }

    int id1 = this.getId() == null ? Integer.MIN_VALUE : this.getId();
    int id2 = category.getId() == null ? Integer.MIN_VALUE : category.getId();

    return Integer.compare(id1, id2);
  }


  /**
   * Returns a hash code value for the object. This method is
   * supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
   *
   * @return A hash code value for this object.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());

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

    if (!(obj instanceof ConfigurationItemCategory)) {
      return false;
    }

    ConfigurationItemCategory other = (ConfigurationItemCategory) obj;
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

    return true;
  }


  /**
   * Returns string value of configuration item category. This method uses
   * {@link org.apache.commons.lang3.builder.ToStringBuilder#reflectionToString
   * ToStringBuilder.reflectionToString} to generate a string value.
   *
   * @return Generated string value of configuration item category.
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
