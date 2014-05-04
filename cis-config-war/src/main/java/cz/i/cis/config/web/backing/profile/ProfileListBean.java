package cz.i.cis.config.web.backing.profile;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "profileList")
@ViewScoped
public class ProfileListBean {

  @EJB
  private ConfigurationProfileDao profileDao;

  private Integer profileID;


  public List<ConfigurationProfile> getAllProfiles() {
    return profileDao.listProfiles();
  }


  public String actionDeleteProfile() {
    try {
      profileDao.removeProfile(profileID);

      return "list?faces-redirect=true";
    }
    catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat profil", FacesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String actionActivateProfile() {
    // TODO zkopírovat všechny položky do aktivní konfigurace
    return null;
  }


  public Integer getProfileID() {
    return profileID;
  }


  public void setProfileID(Integer profileID) {
    this.profileID = profileID;
  }
}
