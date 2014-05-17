package cz.i.cis.config.web.backing.profile;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;


/**
 * Backing bean for profile metadata manipulation.
 */
@Named(value = "profileEditInfo")
@ViewScoped
public class ProfileEditInfoBean {
  /**Logger object used for logging.*/
  private static final Logger LOG = LoggerFactory.getLogger(ProfileEditInfoBean.class);

  @EJB
  /**Data access object for profile manipulation.*/
  private ConfigurationProfileDao profileDao;
  @EJB
  /**Data access object for user manipulation.*/
  private CisUserDao userDao;

  /**ID of currently edited profile. It is set via request parameter.*/
  private Integer id;
  /**Currently edited profile. It is initialized in init() method with use of id field.*/
  private ConfigurationProfile profile;

  /**Edited profile name.*/
  private String name;
  /**Edited profile description.*/
  private String description;


  /**
   * Loads selected profile.
   */
  public void init() throws Exception {
    LOG.debug("init()");
    try {
      profile = profileDao.getProfile(id);
    } catch (IllegalArgumentException e) {
      FacesMessagesUtils.addErrorMessage("Profil není validní - ID = " + id, e);
    }
    if (profile == null) {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, "");
      FacesUtils.redirectToOutcome("list");
      return;
    } else {
      name = profile.getName();
      description = profile.getDescription();
    }
  }


  /**
   * Updates profile with new input.
   * @return Navigation outcome.
   */
  public String actionUpdateProfile() {
    LOG.debug("actionUpdateProfile()");
    if (profile != null) {
      String link = "";
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

        profile.setName(name);
        profile.setDescription(description);
        profile.setUser(editor);
        profile.setUpdate(new Date());

        profile = profileDao.updateProfile(profile);
        FacesMessagesUtils.addInfoMessage("Změny byly uloženy.", null);
        link = "list.xhtml#profile-" + profile.getId();
        FacesUtils.redirectToURL(link);
      } catch (IOException exc) {
        FacesMessagesUtils.failedRedirectMessage(link, exc);
      } catch (NullPointerException exc) {
        FacesMessagesUtils.addErrorMessage(exc.getMessage(), "");
      } catch (NoResultException exc) {
        FacesMessagesUtils.addErrorMessage(exc.getMessage(), "");
      } catch (Exception e) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesMessagesUtils.getRootMessage(e));
      }
    } else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující profil, abyste mohli uložit jeho změny", "");
    }
    return null;
  }


  /**
   * Returns profile ID.
   * @return Profile ID.
   */
  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }

  /**
   * Sets profile ID.
   * @param id Profile ID.
   */
  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }

  /**
   * Returns profile name.
   * @return Profile name.
   */
  public String getName() {
    LOG.trace("getName()");
    return name;
  }

  /**
   * Sets profile name.
   * @param name Profile name.
   */
  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }

  /**
   * Returns profile description.
   * @return Profile description.
   */
  public String getDescription() {
    LOG.trace("getDescription()");
    return description;
  }

  /**
   * Sets profile description.
   * @param description Profile description.
   */
  public void setDescription(String description) {
    LOG.debug("setDescription(description={})", description);
    this.description = description;
  }
}
