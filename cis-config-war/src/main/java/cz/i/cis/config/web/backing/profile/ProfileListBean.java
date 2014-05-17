package cz.i.cis.config.web.backing.profile;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;

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


@Named(value = "profileList")
@ViewScoped
public class ProfileListBean {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileListBean.class);

  @EJB
  private ConfigurationProfileDao profileDao;
  @EJB
  private ConfigurationProfileItemDao profileItemDao;
  @EJB
  private ConfigurationItemDao itemDao;
  @EJB
  private CisUserDao userDao;

  private Integer profileID;



  public List<ConfigurationProfile> getAllProfiles() {
    LOG.trace("getAllProfiles()");
    return profileDao.listProfiles();
  }


  public String actionDeleteProfile() {
    LOG.debug("actionDeleteProfile()");
    try {
      profileDao.removeProfile(profileID);

      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat profil", FacesMessagesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String actionActivateProfile() {
    LOG.trace("actionActivateProfile()");
    try {
      String login = FacesUtils.getRemoteUser();
      if (login == null || login.isEmpty()) {
        throw new NullPointerException(
            "Somehow no user is not logged in and phantoms are not allowed to create configuration profiles.");
      }

      CisUser editor = userDao.getUser(login);
      if (editor == null) {
        throw new NoResultException("Logged in user has not been found in the database.");
      }

      List<ConfigurationProfileItem> profileItems= profileItemDao.listItems(profileID);
      itemDao.activateProfile(profileItems, editor);
    } catch (Exception exc) {
      //TODO osetrit vyjimky
    }
    return null;
  }


  public Integer getProfileID() {
    LOG.debug("getProfileID()");
    return profileID;
  }


  public void setProfileID(Integer profileID) {
    LOG.debug("setProfileID(profileID={})", profileID);
    this.profileID = profileID;
  }
}
