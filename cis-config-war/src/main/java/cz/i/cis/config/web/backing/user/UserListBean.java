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

/**
 * Backing bean for user listing.
 */
@Named(value = "userList")
@ViewScoped
public class UserListBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(UserListBean.class);

  /** Data access object for user manipulation. */
  @EJB
  private CisUserDao userDao;

  /** Collection of all users. */
  private List<CisUser> allUsers;

  /** User identifier. */
  private Integer userID;


  /**
   * Returns all users.
   *
   * @return All users.
   */
  public List<CisUser> getAllUsers() {
    LOG.trace("getAllUsers()");
    if (allUsers == null) {
      try {
        allUsers = userDao.listUsers();
      } catch (IllegalArgumentException e) {
        LOG.error("Failed to load users.", e);
        FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se získat uživatele z databaze", "");
      }
    }
    return allUsers;
  }


  /**
   * Marks selected user as deleted.
   */
  public void actionDeleteUser() {
    LOG.debug("actionDeleteUser()");
    try {
      userDao.removeUser(userID);
      FacesMessagesUtils.addInfoMessage("form", "Uživatel byl smazán", "");
      FacesUtils.redirectToOutcome("list");
    } catch (Exception e) {
      LOG.error("Failed to mark user as deleted", e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se smazat uživatele", e);
    }
  }


  /**
   * Cancels deletion mark for selected user.
   */
  public void actionRestoreUser() {
    LOG.debug("actionRestoreUser()");
    try {
      userDao.restoreUser(userID);
      FacesMessagesUtils.addInfoMessage("form", "Uživatel byl obnoven", "");
      FacesUtils.redirectToOutcome("list");
    } catch (Exception e) {
      LOG.error("Failed to restore user", e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se obnovit uživatele", e);
    }
  }


  /**
   * Returns CSS classes for user listing.
   *
   * @return CSS classes for user listing.
   */
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


  /**
   * Sets user identifier.
   *
   * @param userID user identifier to set.
   */
  public void setUserID(Integer userID) {
    LOG.debug("setUserID(userID={})", userID);
    this.userID = userID;
  }

  /**
   * Returns user identifier.
   *
   * @return User identifier.
   */
  public Integer getUserID() {
    LOG.trace("getUserID()");
    return userID;
  }
}
