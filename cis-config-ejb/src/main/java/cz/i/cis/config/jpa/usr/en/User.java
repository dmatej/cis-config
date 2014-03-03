package cz.i.cis.config.jpa.usr.en;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import javax.persistence.*;


/**
 * Entity implementation class for Entity: User
 */
@Entity
public class User implements Serializable {
  private static final long serialVersionUID = 7483386111744622220L;
  
  @Id
  private Integer id;
  @Temporal(TemporalType.DATE)
  private Date birthDate;
  private String login;
  private String firstName;
  private String lastName;

  
  public User() {
    super();
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
  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
