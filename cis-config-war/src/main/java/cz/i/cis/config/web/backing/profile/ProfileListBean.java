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


  public List<ConfigurationProfile> getAllProfiles() {
    LOG.trace("getAllProfiles()");
    return profileDao.listProfiles();
  }


  public void actionDeleteProfile(Integer profileID) {
    LOG.debug("actionDeleteProfile(profileID={})", profileID);
    try {
      profileDao.removeProfile(profileID);
    }
    catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat profil", FacesUtils.getRootMessage(exc));
    }
  }


  public String actionActivateProfile() {
    LOG.trace("actionActivateProfile()");
    // TODO zkopírovat všechny položky do aktivní konfigurace
    return null;
  }
}
