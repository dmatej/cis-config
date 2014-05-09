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

@Named(value = "userEdit")
@ViewScoped
public class UserEditBean {
  private static final Logger LOG = LoggerFactory.getLogger(UserEditBean.class);

  @EJB
  private CisUserDao userDao;

  private Integer id;

  private CisUser user;

  private String name;
  private String surname;
  private String login;
  private Date birthDate;


  public void init() {
    LOG.debug("init()");
    user = userDao.getUser(id);

    if (user != null) {
      name = user.getFirstName();
      surname = user.getLastName();
      login = user.getLogin();
      birthDate = user.getBirthDate();
    } else {
      FacesMessagesUtils.addErrorMessage("Zvolený uživatel nebyl nalezen v databázi - ID = " + id, null);
    }
  }


  public String actionUpdateUser() {
    LOG.debug("actionUpdateUser()");
    if (user != null) {
      user.setFirstName(name);
      user.setLastName(surname);
      user.setLogin(login);
      user.setBirthDate(birthDate);

      String link = "";
      try {
        user = userDao.updateUser(user);

        // FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Změny byly uloženy.");
        link = "list.xhtml#user-" + user.getId();
        FacesUtils.redirect(link);
      } catch (IOException exc) {
        FacesMessagesUtils.failedRedirectMessage(link, exc);
      } catch (Exception exc) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(exc));
      }
    } else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existujícího uživatele, abyste mohli uložit jeho změny.",
          null);
    }
    return null; // stay on the same page to display the messages
  }


  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
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
    LOG.trace("getLogin()");
    return login;
  }


  public void setLogin(String login) {
    LOG.debug("setLogin(login={})", login);
    this.login = login;
  }


  public Date getBirthDate() {
    return birthDate;
  }


  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
}
