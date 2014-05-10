package cz.i.cis.config.web.backing.profile;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "profileList")
@ViewScoped
public class ProfileListBean {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileListBean.class);

  @EJB
  private ConfigurationProfileDao profileDao;

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
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat profil", FacesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String actionActivateProfile() {
    LOG.trace("actionActivateProfile()");
    // TODO zkopírovat všechny položky do aktivní konfigurace
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
