package cz.i.cis.config.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: CisUser
 */
@Entity
@Table(name = "cisuser")
public class CisUser implements Serializable {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date birthDate;

  private static final long serialVersionUID = 1L;


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
}
