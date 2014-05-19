package cz.i.cis.config.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Entity implementation class for {@code CisUser}.
 */
@Entity
@Table(name = "cis_users")
public class CisUser implements Comparable<CisUser>, Serializable {

  /**
   * Determines if a de-serialized file is compatible with this class.
   * Maintainers must change this value if and only if the new version of this
   * class is not compatible with old versions. See Oracle docs for <a
   * href="http://docs.oracle.com/javase/1.4.2/docs/guide/
   * serialization/">details</a>.
   * Not necessary to include in first version of the class, but included here
   * as a reminder of its importance.
   */
  private static final long serialVersionUID = 2103422942760297008L;

  /** Constant for valid status of user. */
  public static final Integer STATUS_VALID = 0;
  /** Constant for deleted status of user. */
  public static final Integer STATUS_DELETED = 1;

  /** Identifier number of CIS user. */
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Fist name of CIS user. */
  @Column(nullable = false)
  private String firstName;

  /** Second name of CIS user. */
  @Column(nullable = false)
  private String lastName;

  /** Unique login of CIS user. */
  @Column(nullable = false, unique = true)
  private String login;

  /** Birth date of CIS user. */
  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date birthDate;

  /**
   * Status of CIS user. Status of CIS user should be {@link #STATUS_VALID} or
   * {@link #STATUS_DELETED}.
   */
  @Column(nullable = false)
  private Integer status = STATUS_VALID;


  /**
   * Returns identifier number of this user.
   *
   * @return Identifier number of this user.
   */
  public Integer getId() {
    return this.id;
  }


  /**
   * Sets identifier number of this user.
   *
   * @param id identifier number of this user to set.
   */
  public void setId(Integer id) {
    this.id = id;
  }


  /**
   * Returns unique login of this user.
   *
   * @return Unique login of this user.
   */
  public String getLogin() {
    return this.login;
  }


  /**
   * Sets unique login of this user.
   *
   * @param login unique login of this user to set.
   */
  public void setLogin(String login) {
    this.login = login;
  }


  /**
   * Returns first name of this user.
   *
   * @return First name of this user.
   */
  public String getFirstName() {
    return this.firstName;
  }


  /**
   * Sets first name of this user.
   *
   * @param firstName first name of this user to set.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  /**
   * Returns last name of this user.
   *
   * @return Last name of this user.
   */
  public String getLastName() {
    return this.lastName;
  }


  /**
   * Sets last name of this user.
   *
   * @param lastName last name of this user to set.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  /**
   * Returns birth date of this user.
   *
   * @return Birth date of this user.
   */
  public Date getBirthDate() {
    return this.birthDate;
  }


  /**
   * Sets birth date of this user.
   *
   * @param birthDate birth date of this user to set.
   */
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }


  /**
   * Returns status of this user. Status of CIS user should be {@link #STATUS_VALID} or
   * {@link #STATUS_DELETED}.
   *
   * @return Status of this user.
   */
  public Integer getStatus() {
    return status;
  }


  /**
   * Sets status of this user. Status of CIS user should be {@link #STATUS_VALID} or
   * {@link #STATUS_DELETED}.
   *
   * @param status status of this user to set.
   */
  public void setStatus(Integer status) {
    this.status = status;
  }


  /**
   * Returns information whether is this user valid (status == {@link #STATUS_VALID}).
   *
   * @return Information whether is this user valid.
   */
  public boolean isValid() {
    return status == STATUS_VALID;
  }


  /**
   * Returns information whether is this user deleted (status == {@link #STATUS_DELETED}).
   *
   * @return Information whether is this user deleted.
   */
  public boolean isDeleted() {
    return status == STATUS_DELETED;
  }


  /**
   * Compares this object with the entered object for order. Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the entered object.
   *
   * @param user CIS user to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   *         is less than, equal to, or greater than the entered object.
   */
  @Override
  public int compareTo(CisUser user) {
    if (user == null) {
      return 1;
    }

    int id1 = this.getId() == null ? Integer.MIN_VALUE : this.getId();
    int id2 = user.getId() == null ? Integer.MIN_VALUE : user.getId();

    return Integer.compare(id1, id2);
  }


  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hash tables such as those provided by {@link java.util.HashMap}.
   *
   * @return A hash code value for this object.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + (id == null ? 0 : id.hashCode());
    result = prime * result + (login == null ? 0 : login.hashCode());

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

    if (!(obj instanceof CisUser)) {
      return false;
    }

    CisUser other = (CisUser) obj;
    if (birthDate == null) {
      if (other.birthDate != null) {
        return false;
      }
    } else if (!DateUtils.isSameDay(birthDate, other.birthDate)) {
      return false;
    }

    if (firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!firstName.equals(other.firstName)) {
      return false;
    }

    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }

    if (lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!lastName.equals(other.lastName)) {
      return false;
    }

    if (login == null) {
      if (other.login != null) {
        return false;
      }
    } else if (!login.equals(other.login)) {
      return false;
    }

    if (status == null) {
      if (other.status != null) {
        return false;
      }
    } else if (!status.equals(other.status)) {
      return false;
    }

    return true;
  }


  /**
   * Returns string value of CIS user. This method uses
   * {@link org.apache.commons.lang3.builder.ToStringBuilder#reflectionToString
   * ToStringBuilder.reflectionToString} to generate a string value.
   *
   * @return Generated string value of CIS user.
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
