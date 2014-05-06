package cz.i.cis.config.web;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;

@Named(value = "userManager")
@ViewScoped
public class UserManager {

  @EJB
  private CisUserDao userDao;


  private String getRemoteUser() {
    return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
  }

  public boolean isValid() {
    String login = this.getRemoteUser();

    if (login == null || login.isEmpty()) {
      return false;
    }

    CisUser user = userDao.getUser(login);
    if (user == null) {
      return false;
    }

    return (user.isValid());
  }

  public boolean isRegistered() {
    String login = this.getRemoteUser();

    if (login == null || login.isEmpty()) {
      return false;
    }

    CisUser user = userDao.getUser(login);
    if (user == null) {
      return false;
    }

    return true;
  }
}