package cz.i.cis.config.web;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;


@Named(value = "userManager")
@ViewScoped
public class UserManager {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);

  @EJB
  /**Data access object for user manipulation.*/
  private CisUserDao userDao;


  /**
   * Tests logged in user validity.
   * @return True if user is logged in, has DB record and is not marked as deleted. False otherwise.
   */
  public boolean isValid() {
    LOG.debug("isValid()");
    String login = FacesUtils.getRemoteUser();

    if (login == null || login.isEmpty()) {
      return false;
    }

    CisUser user = userDao.getUser(login);
    if (user == null) {
      return false;
    }

    return (user.isValid());
  }

  /**
   * Tests if user has DB record.
   * @return True if user is logged in and has record in DB.
   */
  public boolean isRegistered() {
    LOG.debug("isRegistered()");
    String login = FacesUtils.getRemoteUser();

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
