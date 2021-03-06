package cz.i.cis.config.web.backing.profile;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;
import cz.i.cis.config.web.exceptions.NonExistentCategoryException;
import cz.i.cis.config.web.exceptions.UserNotFoundException;

/**
 * Backing bean for profile creation.
 */
@Named(value = "profileCreate")
@ViewScoped
public class ProfileCreateBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(ProfileCreateBean.class);

  /** Data access object for profile manipulation. */
  @EJB
  private ConfigurationProfileDao profileDao;
  /** Data access object for user manipulation. */
  @EJB
  private CisUserDao userDao;

  /** New profile name. */
  private String name;
  /** New profile description. */
  private String description;


  /**
   * Adds new profile to database.
   *
   * @return Navigation outcome.
   */
  public String actionAddProfile() {
    LOG.debug("actionAddProfile()");
    try {
      String login = FacesUtils.getRemoteUser();
      if (login == null || login.isEmpty()) {
        throw new NonExistentCategoryException();
      }
      CisUser editor = userDao.getUser(login);
      if (editor == null) {
        throw new UserNotFoundException();
      }

      ConfigurationProfile newProfile = profileDao.getProfile(name);
      if (newProfile != null) {
        FacesMessagesUtils.addErrorMessage("form:name", "Profil se zadaným jménem již existuje", "");
        return null;
      }

      newProfile = new ConfigurationProfile();
      newProfile.setName(name);
      newProfile.setDescription(description);
      newProfile.setUser(editor);
      newProfile.setUpdate(new Date());

      profileDao.addProfile(newProfile);

      return "edit?faces-redirect=true&includeViewParams=true&id=" + newProfile.getId();
      // FacesUtils.redirect("list.xhtml#user-" + profile.getId());
    } catch (NonExistentCategoryException | UserNotFoundException e) { // only JRE7
      FacesMessagesUtils.addErrorMessage(FacesMessagesUtils.getRootMessage(e), "");
    } catch (Exception e) {
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se přidat nový profil", e);
    }

    return null;
  }


  /**
   * Returns profile name.
   *
   * @return Profile name.
   */
  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  /**
   * Sets profile name.
   *
   * @param name Profile name.
   */
  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }


  /**
   * Returns profile description.
   *
   * @return Profile description.
   */
  public String getDescription() {
    LOG.trace("getDescription()");
    return description;
  }


  /**
   * Sets profile description.
   *
   * @param description Profile description.
   */
  public void setDescription(String description) {
    LOG.debug("setDescription(description={})", description);
    this.description = description;
  }
}
