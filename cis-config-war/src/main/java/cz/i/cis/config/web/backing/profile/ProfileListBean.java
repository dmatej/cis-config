package cz.i.cis.config.web.backing.profile;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileItemDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;
import cz.i.cis.config.web.exceptions.NonExistentCategoryException;
import cz.i.cis.config.web.exceptions.UserNotFoundException;

/**
 * Backing bean for profile listing.
 */
@Named(value = "profileList")
@ViewScoped
public class ProfileListBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(ProfileListBean.class);

  /** Data access object for profile manipulation. */
  @EJB
  private ConfigurationProfileDao profileDao;
  /** Data access object for profile items manipulation. */
  @EJB
  private ConfigurationProfileItemDao profileItemDao;
  /** Data access object for active configuration manipulation. */
  @EJB
  private ConfigurationItemDao itemDao;
  /** Data access object for user manipulation. */
  @EJB
  private CisUserDao userDao;


  /**
   * Returns all configuration profiles.
   *
   * @return All configuration profiles.
   */
  public List<ConfigurationProfile> getAllProfiles() {
    LOG.trace("getAllProfiles()");
    return profileDao.listProfiles();
  }


  /**
   * Deletes selected profile.
   *
   * @param id ID of profile to delete.
   */
  public void actionDeleteProfile(String id) {
    LOG.debug("actionDeleteProfile(id={})", id);
    try {
      Integer profileID = Integer.valueOf(id);
      profileDao.removeProfile(profileID);
      FacesMessagesUtils.addInfoMessage("form", "Profil byl smazán", "");
    } catch (Exception e) {
      LOG.error("Failed to delete profile: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se smazat profil", e);
    }
  }


  /**
   * Activates selected profile. All profile item will be set to active configuration.
   *
   * @param id ID of profile to activate.
   */
  public void actionActivateProfile(String id) {
    LOG.trace("actionActivateProfile(id={})", id);
    try {
      String login = FacesUtils.getRemoteUser();
      if (login == null || login.isEmpty()) {
        throw new NonExistentCategoryException();
      }

      CisUser editor = userDao.getUser(login);
      if (editor == null) {
        throw new UserNotFoundException();
      }

      Integer profileID = Integer.valueOf(id);
      List<ConfigurationProfileItem> profileItems = profileItemDao.listItems(profileID);
      itemDao.activateProfile(profileItems, editor);
      FacesMessagesUtils.addInfoMessage("form", "Profil byl aktivován", "");
    } catch (NonExistentCategoryException | UserNotFoundException e) { // only JRE7
      FacesMessagesUtils.addErrorMessage(FacesMessagesUtils.getRootMessage(e), "");
    } catch (Exception e) {
      LOG.error("Failed to activate profile: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se aktivovat profil", e);
    }
  }
}
