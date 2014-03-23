package cz.i.cis.config.web.backing;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "userCreate")
@ViewScoped
public class UserCreateBean {

  @EJB
  private CisUserDao userDao;

  private String name;
  private String surname;
  private String login;
  private Date birthDate;


  public String actionAddUser() throws IOException{
    CisUser newUser = new CisUser();
      newUser.setFirstName(name);
      newUser.setLastName(surname);
      newUser.setBirthDate(birthDate);
      newUser.setLogin(login);

    try{
      userDao.addUser(newUser);
//    return "user-edit?faces-redirect=true&includeViewParams=true&id=" + newUser.getId();
      FacesUtils.redirect("user-list.xhtml#user-" + newUser.getId());
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se přidat nového uživatele: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
}
