package cz.i.cis.config.web.backing.profile;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
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

@Named(value = "profileEditInfo")
@ViewScoped
public class ProfileEditInfoBean {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileEditInfoBean.class);

  @EJB
  private ConfigurationProfileDao profileDao;
  @EJB
  private CisUserDao userDao;

  private Integer id;

  private ConfigurationProfile profile;

  // profile metadata
  private String name;
  private String description;


  public void init() throws Exception {
    LOG.debug("init()");
    try {
      profile = profileDao.getProfile(id);
    } catch (IllegalArgumentException exp) {
      FacesMessagesUtils.addErrorMessage("Profile id " + id + " is not valid", null);
    }
    if (profile != null) {
      name = profile.getName();
      description = profile.getDescription();
    } else {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, null);
    }
  }


  public String actionUpdateProfile() {
    LOG.debug("actionUpdateProfile()");
    String link = "";
    if (profile != null) {
      try {
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
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
        FacesUtils.redirect("list.xhtml#profile-" + profile.getId());
      } catch (IOException exc) {
        FacesMessagesUtils.failedRedirectMessage(link, exc);
      } catch (NullPointerException exc) {
        FacesMessagesUtils.addErrorMessage(exc.getMessage(), null);
      } catch (NoResultException exc) {
        FacesMessagesUtils.addErrorMessage(exc.getMessage(), null);
      } catch (Exception e) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(e));
      }
    } else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující profil, abyste mohli uložit jeho změny", null);
    }
    return null;
  }


  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
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
