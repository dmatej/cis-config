package cz.i.cis.config.web.backing.user;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

/**
 * Backing bean for profile items manipulation.
 */
@Named(value = "userCreate")
@ViewScoped
public class UserCreateBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(UserCreateBean.class);

  /**Data access object for user manipulation.*/
  @EJB
  private CisUserDao userDao;

  /** Name of new user. */
  private String name;
  /** Surname of new user. */
  private String surname;
  /** Login of new user. */
  private String login;
  /** Birth date of new user. */
  private Date birthDate;


  /**
   * Adds new user to database.
   *
   * @return Navigation outcome.
   */
  public String actionAddUser() {
    LOG.debug("actionAddUser()");

    CisUser newUser = userDao.getUser(login);
    if(newUser != null) {
      FacesMessagesUtils.addErrorMessage("form:login", "Uživatel se zadaným loginem již existuje", "");
      return null;
    }

    newUser = new CisUser();
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
    } catch (IOException e) {
      LOG.error("Failed to redirect to: " + link, e);
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (Exception e) {
      LOG.error("Failed to add user.", e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se vytvořit uživatele", e);
    }
    return null;
  }


  /**
   * Returns user name.
   *
   * @return User name.
   */
  public String getName() {
    LOG.debug("getName()");
    return name;
  }


  /**
   * Sets user name.
   *
   * @param name User name.
   */
  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }


  /**
   * Returns user surname.
   *
   * @return User surname.
   */
  public String getSurname() {
    LOG.trace("getSurname()");
    return surname;
  }


  /**
   * Sets user surname.
   *
   * @param surname User surname.
   */
  public void setSurname(String surname) {
    LOG.debug("setSurname(surname={})", surname);
    this.surname = surname;
  }


  /**
   * Returns user login.
   *
   * @return User login.
   */
  public String getLogin() {
    LOG.trace("setLogin(login)");
    return login;
  }


  /**
   * Sets user login.
   *
   * @param login User login.
   */
  public void setLogin(String login) {
    LOG.debug("setLogin(login={})", login);
    this.login = login;
  }


  /**
   * Returns user birth date.
   *
   * @return User birth date.
   */
  public Date getBirthDate() {
    LOG.trace("getBirthDate()");
    return birthDate;
  }


  /**
   * Sets user birth date.
   *
   * @param birthDate User birth date.
   */
  public void setBirthDate(Date birthDate) {
    LOG.debug("setBirthDate(birthDate={})", birthDate);
    this.birthDate = birthDate;
  }
}
