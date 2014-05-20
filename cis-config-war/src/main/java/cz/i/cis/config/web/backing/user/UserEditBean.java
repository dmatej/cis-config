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
@Named(value = "userEdit")
@ViewScoped
public class UserEditBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(UserEditBean.class);

  /** Data access object for user manipulation. */
  @EJB
  private CisUserDao userDao;

  /** ID of currently edited user. It is set via request parameter. */
  private Integer id;
  /** Currently edited user. It is initialized in init() method with use of id field. */
  private CisUser user;

  /** Edited user name. */
  private String name;
  /** Edited user surname. */
  private String surname;
  /** Edited user login. */
  private String login;
  /** Edited user birth date. */
  private Date birthDate;


  /**
   * Loads user with given ID.
   */
  public void init() {
    LOG.debug("init()");
    try {
      user = userDao.getUser(id);
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to load user during initialization: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("Uživatel není validní: ID = " + id, e);
      FacesUtils.redirectToOutcome("list");
      return;
    }

    if (user == null) {
      LOG.error("User not loaded during initialization, redirecting to list: ID = {}", id);
      FacesMessagesUtils.addErrorMessage("Zvolený uživatel nebyl nalezen v databázi: ID = " + id, "");
      FacesUtils.redirectToOutcome("list");
      return;
    }

    name = user.getFirstName();
    surname = user.getLastName();
    login = user.getLogin();
    birthDate = user.getBirthDate();
  }


  /**
   * Persists changes.
   *
   * @return Navigation outcome.
   */
  public String actionUpdateUser() {
    LOG.debug("actionUpdateUser()");
    if (user == null) {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existujícího uživatele, abyste mohli uložit jeho změny.", "");
      return null;
    }

    CisUser oldUser = userDao.getUser(login);
    if (oldUser != null && oldUser.getId() != user.getId()) {
      FacesMessagesUtils.addErrorMessage("form:login", "Uživatel se zadaným loginem již existuje", "");
      return null;
    }

    user.setFirstName(name);
    user.setLastName(surname);
    user.setLogin(login);
    user.setBirthDate(birthDate);

    String link = "";
    try {
      user = userDao.updateUser(user);

      link = "list.xhtml#user-" + user.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException e) {
      LOG.error("Failed to persist changes: failed to redirect", e);
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (Exception e) {
      LOG.error("Failed to persist changes.", e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se uložit změny", e);
    }

    return null;
  }


  /**
   * Returns ID of edited user.
   *
   * @return ID of edited user.
   */
  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  /**
   * Sets ID of edited user.
   *
   * @param id ID of edited user.
   */
  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }


  /**
   * Returns edited user name.
   *
   * @return Edited user name.
   */
  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  /**
   * Sets edited user name.
   *
   * @param name Edited user name.
   */
  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }


  /**
   * Returns edited user surname.
   *
   * @return Edited user surname.
   */
  public String getSurname() {
    LOG.trace("getSurname()");
    return surname;
  }


  /**
   * Sets edited user surname.
   *
   * @param surname Edited user surname.
   */
  public void setSurname(String surname) {
    LOG.debug("setSurname(surname={})", surname);
    this.surname = surname;
  }


  /**
   * Returns edited user login.
   *
   * @return Edited user login.
   */
  public String getLogin() {
    LOG.trace("getLogin()");
    return login;
  }


  /**
   * Sets edited user login.
   *
   * @param login Edited user login.
   */
  public void setLogin(String login) {
    LOG.debug("setLogin(login={})", login);
    this.login = login;
  }


  /**
   * Returns edited user birth date.
   *
   * @return Edited user birth date.
   */
  public Date getBirthDate() {
    return birthDate;
  }


  /**
   * Sets edited user birth date.
   *
   * @param birthDate Edited user birth date.
   */
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
}
