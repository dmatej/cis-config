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
 * Entity implementation class for Entity: CisUser
 */
@Entity
@Table(name = "cisuser")
public class CisUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String login;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date birthDate;

  @Column(nullable = false)
  private Integer status = new Integer(0);


  public CisUser() {
    super();
  }


  public Integer getId() {
    return this.id;
  }


  public void setId(Integer id) {
    this.id = id;
  }


  public String getLogin() {
    return this.login;
  }


  public void setLogin(String login) {
    this.login = login;
  }


  public String getFirstName() {
    return this.firstName;
  }


  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public String getLastName() {
    return this.lastName;
  }


  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  public Date getBirthDate() {
    return this.birthDate;
  }


  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }


  public Integer getStatus() {
    return status;
  }


  public void setStatus(Integer status) {
    this.status = status;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + (id == null ? 0 : id.hashCode());
    result = prime * result + (login == null ? 0 : login.hashCode());

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


  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
