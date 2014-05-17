package cz.i.cis.config.web.backing.profile;

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
 * Backing bean for profile creation.
 */
@Named(value = "profileCreate")
@ViewScoped
public class ProfileCreateBean {
  /**Logger object used for logging.*/
  private static final Logger LOG = LoggerFactory.getLogger(ProfileCreateBean.class);

  @EJB
  /**Data access object for profile manipulation.*/
  private ConfigurationProfileDao profileDao;
  @EJB
  /**Data access object for user manipulation.*/
  private CisUserDao userDao;

  /**New profile name.*/
  private String name;
  /**New profile description.*/
  private String description;


  /**
   * Adds new profile to database.
   * @return Navigation outcome.
   */
  public String actionAddProfile() {
    LOG.debug("actionAddProfile()");
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

      ConfigurationProfile profile = new ConfigurationProfile();
        profile.setName(name);
        profile.setDescription(description);
        profile.setUser(editor);
        profile.setUpdate(new Date());

      profileDao.addProfile(profile);

      return "edit?faces-redirect=true&includeViewParams=true&id=" + profile.getId();
      // FacesUtils.redirect("list.xhtml#user-" + profile.getId());
    } catch (NullPointerException e) {
      FacesMessagesUtils.addErrorMessage(FacesMessagesUtils.getRootMessage(e), "");
    } catch (NoResultException e) {
      FacesMessagesUtils.addErrorMessage(FacesMessagesUtils.getRootMessage(e), "");
    } catch (Exception e) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se přidat nový profil", e);
    }
    return null;
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
