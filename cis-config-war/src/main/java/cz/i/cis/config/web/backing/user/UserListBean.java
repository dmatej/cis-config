package cz.i.cis.config.web.backing.user;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "userList")
@ViewScoped
public class UserListBean {

  @EJB
  private CisUserDao userDao;

  private Integer userID;

  private List<CisUser> allUsers;


  public List<CisUser> getAllUsers() {
    if (allUsers == null)
      allUsers = userDao.listUsers();
    return allUsers;
  }


  public String actionDeleteUser() {
    try {
      userDao.removeUser(userID);
      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat uživatele", FacesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String actionRestoreUser() {
    try {
      userDao.restoreUser(userID);
      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se obnovit uživatele", FacesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String getRowClasses() {
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
    this.userID = userID;
  }


  public Integer getUserID() {
    return userID;
  }
}
