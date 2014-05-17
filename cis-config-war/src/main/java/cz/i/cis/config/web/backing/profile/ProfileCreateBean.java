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

@Named(value = "profileCreate")
@ViewScoped
public class ProfileCreateBean {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileCreateBean.class);

  @EJB
  private ConfigurationProfileDao profileDao;
  @EJB
  private CisUserDao userDao;

  private String name;
  private String description;


  public String actionAddProfile() throws IOException {
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
      profile.setUpdate(new Date(System.currentTimeMillis()));

      profileDao.addProfile(profile);

      return "edit?faces-redirect=true&includeViewParams=true&id=" + profile.getId();
      // FacesUtils.redirect("list.xhtml#user-" + profile.getId());
    } catch (NullPointerException exc) {
      FacesMessagesUtils.addErrorMessage(exc.getMessage(), "");
    } catch (NoResultException exc) {
      FacesMessagesUtils.addErrorMessage(exc.getMessage(), "");
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se přidat nový profil", FacesMessagesUtils.getRootMessage(exc));
    }
    return null;
  }


  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }


  public String getDescription() {
    LOG.trace("getDescription()");
    return description;
  }


  public void setDescription(String description) {
    LOG.debug("setDescription(description={})", description);
    this.description = description;
  }
}
