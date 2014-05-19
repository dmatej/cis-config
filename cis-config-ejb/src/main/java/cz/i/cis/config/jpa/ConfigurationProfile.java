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
 * Entity implementation class for {@code ConfigurationProfile}.
 *
 * @author David Matějček
 * @author Mr.FrAnTA (Michal Dékány)
 */
@Entity
@Table(name = "configuration_profiles")
public class ConfigurationProfile implements Comparable<ConfigurationProfile>, Serializable {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = -5456001107822637583L;

  /** Identifier number of configuration profile. */
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Configuration profile name. */
  @Column(nullable = false)
  private String name;

  /** Description of configuration profile. */
  @Column(nullable = false)
  private String description = "";

  /** Timestamp of last update of configuration profile. */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date update;

  /** CIS user which last changed configuration profile. */
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private CisUser user;

  /**
   * Constructs configuration profile.
   */
  public ConfigurationProfile() {
  }

  /**
   * Returns identifier number of this configuration profile.
   *
   * @return Identifier number of this configuration profile.
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Sets identifier number of this configuration profile.
   *
   * @param id identifier number of this configuration profile to set.
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Returns name of this configuration profile.
   *
   * @return Name of this configuration profile.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets name of this configuration profile.
   *
   * @param name name of this configuration profile to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns description of this configuration profile.
   *
   * @return Description of configuration profile.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets description of this configuration profile.
   *
   * @param description description of this configuration profile to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns timestamp of last update of this configuration profile.
   *
   * @return Timestamp of last update of this configuration profile.
   */
  public Date getUpdate() {
    return this.update;
  }

  /**
   * Sets timestamp of last update of this configuration profile.
   *
   * @param update timestamp of last update of this configuration profile to set.
   */
  public void setUpdate(Date update) {
    this.update = update;
  }

  /**
   * Returns CIS user which last changed this configuration profile.
   *
   * @return CIS user which last changed this configuration profile to set.
   */
  public CisUser getUser() {
    return this.user;
  }

  /**
   * Sets CIS user which last changed this configuration profile.
   *
   * @param user CIS user which last changed this configuration profile.
   */
  public void setUser(CisUser user) {
    this.user = user;
  }

  /**
   * Compares this object with the entered object for order. Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the entered object.
   *
   * @param profile the configuration profile to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the entered object.
   */
  @Override
  public int compareTo(ConfigurationProfile profile) {
    if(profile == null) {
      return 1;
    }

    int id1 = this.getId() == null ? Integer.MIN_VALUE : this.getId();
    int id2 = profile.getId() == null ? Integer.MIN_VALUE : profile.getId();

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

    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((update == null) ? 0 : update.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());

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


  /**
   * Returns string value of configuration profile in format:
   *
   * <pre>
   * ConfigurationProfile[id=%id, name=%name, description=%description, update=%update, user_id=%user.id]
   * </pre>
   *
   * @return String value of configuration profile.
   */
  @Override
  public String toString() {
    return new StringBuilder(this.getClass().getCanonicalName())
      .append("[id=").append(this.getId())
      .append(", name=").append(this.getName())
      .append(", description=").append(this.getDescription())
      .append(", update=").append(this.getUpdate().toString())
      .append(", user_id=").append(this.getUser().getId())
      .append("]").toString();
  }
}
