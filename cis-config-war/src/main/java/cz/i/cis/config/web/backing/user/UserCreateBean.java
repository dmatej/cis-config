package cz.i.cis.config.web.backing.user;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "userCreate")
@ViewScoped
public class UserCreateBean {
  private static final Logger LOG = LoggerFactory.getLogger(UserCreateBean.class);


  @EJB
  private CisUserDao userDao;

  private String name;
  private String surname;
  private String login;
  private Date birthDate;


  public String actionAddUser() {
    LOG.debug("actionAddUser()");
    CisUser newUser = new CisUser();
    newUser.setFirstName(name);
    newUser.setLastName(surname);
    newUser.setBirthDate(birthDate);
    newUser.setLogin(login);

    String link = "";
    try {
      userDao.addUser(newUser);

      // return "edit?faces-redirect=true&includeViewParams=true&id=" + newUser.getId();
      link = "list.xhtml#user-" + newUser.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException exc) {
      FacesMessagesUtils.failedRedirectMessage(link, exc);
    } catch (UserAlreadyExistsException exc) {
      FacesMessagesUtils.addErrorMessage("Uživatel již existuje", FacesMessagesUtils.getRootMessage(exc));
    }

    return null;
  }


  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }


  public String getSurname() {
    LOG.trace("getSurname()");
    return surname;
  }


  public void setSurname(String surname) {
    LOG.debug("setSurname(surname={})", surname);
    this.surname = surname;
  }


  public String getLogin() {
    LOG.trace("setLogin(login)");
    return login;
  }


  public void setLogin(String login) {
    LOG.debug("setLogin(login={})", login);
    this.login = login;
  }


  public Date getBirthDate() {
    LOG.trace("getBirthDate()");
    return birthDate;
  }


  public void setBirthDate(Date birthDate) {
    LOG.debug("setBirthDate(birthDate={})", birthDate);
    this.birthDate = birthDate;
  }
}
