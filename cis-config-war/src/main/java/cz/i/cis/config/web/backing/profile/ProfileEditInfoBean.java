package cz.i.cis.config.web.backing.profile;

import java.io.IOException;
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
 * Backing bean for profile metadata manipulation.
 */
@Named(value = "profileEditInfo")
@ViewScoped
public class ProfileEditInfoBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(ProfileEditInfoBean.class);

  /** Data access object for profile manipulation. */
  @EJB
  private ConfigurationProfileDao profileDao;
  /** Data access object for user manipulation. */
  @EJB
  private CisUserDao userDao;

  /** ID of currently edited profile. It is set via request parameter. */
  private Integer id;
  /** Currently edited profile. It is initialized in init() method with use of id field. */
  private ConfigurationProfile profile;

  /** Edited profile name. */
  private String name;
  /** Edited profile description. */
  private String description;


  /**
   * Loads selected profile.
   */
  public void init() throws Exception {
    LOG.debug("init()");
    try {
      profile = profileDao.getProfile(id);
    } catch (IllegalArgumentException e) {
      FacesMessagesUtils.addErrorMessage("Profil není validní: ID = " + id, e);
      FacesUtils.redirectToOutcome("list");
      return;
    }

    if (profile == null) {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi: ID = " + id, "");
      FacesUtils.redirectToOutcome("list");
      return;
    }

    name = profile.getName();
    description = profile.getDescription();
  }


  /**
   * Updates profile with new input.
   *
   * @return Navigation outcome.
   */
  public String actionUpdateProfile() {
    LOG.debug("actionUpdateProfile()");
    if (profile == null) {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující profil, abyste mohli uložit jeho změny", "");
      return null;
    }

    String link = "";
    try {
      String login = FacesUtils.getRemoteUser();
      if (login == null || login.isEmpty()) {
        throw new NonExistentCategoryException();
      }
      CisUser editor = userDao.getUser(login);
      if (editor == null) {
        throw new UserNotFoundException();
      }

      ConfigurationProfile oldProfile = profileDao.getProfile(name);
      if (oldProfile != null && oldProfile.getId() != profile.getId()) {
        FacesMessagesUtils.addErrorMessage("form:name", "Profil se zadaným jménem již existuje", "");
        return null;
      }

      profile.setName(name);
      profile.setDescription(description);
      profile.setUser(editor);
      profile.setUpdate(new Date());

      profile = profileDao.updateProfile(profile);
      link = "list.xhtml#profile-" + profile.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException e) {
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (NonExistentCategoryException | UserNotFoundException e) { // only JRE7
      FacesMessagesUtils.addErrorMessage(FacesMessagesUtils.getRootMessage(e), "");
    } catch (Exception e) {
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se uložit změny", e);
    }

    return null;
  }


  /**
   * Returns profile ID.
   *
   * @return Profile ID.
   */
  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  /**
   * Sets profile ID.
   *
   * @param id Profile ID.
   */
  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
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
