package cz.i.cis.config.web.backing.user;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "userList")
@ViewScoped
public class UserListBean {

  private static final Logger LOG = LoggerFactory.getLogger(UserListBean.class);

  @EJB
  private CisUserDao userDao;

  private Integer userID;

  private List<CisUser> allUsers;


  public List<CisUser> getAllUsers() {
    LOG.trace("getAllUsers()");
    if (allUsers == null) {
      try {
        allUsers = userDao.listUsers();
      } catch (IllegalArgumentException exc) {
        FacesMessagesUtils.addErrorMessage("form:data-table","Cannot select users from database", null);
      }
    }
    return allUsers;
  }


  public String actionDeleteUser() {
    LOG.debug("actionDeleteUser()");
    try {
      userDao.removeUser(userID);
      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat uživatele", FacesMessagesUtils.getRootMessage(exc));
      return null;
    }
  }


  public String actionRestoreUser() {
    LOG.debug("actionRestoreUser()");
    try {
      userDao.restoreUser(userID);
      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se obnovit uživatele", FacesMessagesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String getRowClasses() {
    LOG.debug("getRowClasses()");
    StringBuilder classes = new StringBuilder();
    for (CisUser user : getAllUsers()) {
      classes.append(user.getStatus() == CisUser.STATUS_DELETED ? "deleted," : "none,");
    }
    if (classes.length() > 0) {
      classes.deleteCharAt(classes.length() - 1);
    }

    return classes.toString();
  }


  public void setUserID(Integer userID) {
    LOG.debug("setUserID(userID={})", userID);
    this.userID = userID;
  }


  public Integer getUserID() {
    LOG.trace("getUserID()");
    return userID;
  }
}
