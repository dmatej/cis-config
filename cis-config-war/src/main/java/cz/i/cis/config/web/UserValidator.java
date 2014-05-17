package cz.i.cis.config.web;

import java.security.Principal;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;

@Named(value = "userValidator")
public class UserValidator {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(UserValidator.class);


  @EJB
  /**Data access object for user manipulation.*/
  private CisUserDao userDao;


  /**
   * Tests login existence.
   * @return True if login is not used or related user is marked as deleted.
   */
  public boolean loginExist() {
    LOG.debug("loginExist()");
    Principal currentUser = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    // FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, currentUser.getName());
    CisUser temp = userDao.getUser(currentUser.getName());
    if (temp == null)
      return false;
    else if (temp.isDeleted())
      return false; //FIXME toto je doporučení pro přidání uživatele -> duplicitní login v DB -> chyba

    return true;
  }
}
