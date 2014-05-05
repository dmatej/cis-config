package cz.i.cis.config.web.backing.profile;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "profileEditInfo")
@ViewScoped
public class ProfileEditInfoBean {

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
    profile = profileDao.getProfile(id);

    if (profile != null) {
      name = profile.getName();
      description = profile.getDescription();
    }
    else {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, null);
    }
  }

  public String actionUpdateProfile() {
    if (profile != null) {
      try {
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        if (login == null || login.isEmpty()) {
          throw new Exception("Somehow no user is not logged in and phantoms are not allowed to create configuration profiles.");
        }
        CisUser editor = userDao.getUser(login);
        if (editor == null) {
          throw new Exception("Logged in user has not been found in the database.");
        }

        profile.setName(name);
        profile.setDescription(description);
        profile.setUser(editor);
        profile.setUpdate(new Date());

        profile = profileDao.updateProfile(profile);
        FacesMessagesUtils.addInfoMessage("Změny byly uloženy.", null);
        FacesUtils.redirect("list.xhtml#profile-" + profile.getId());
      }
      catch (Exception e) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(e));
      }
    }
    else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující profil, abyste mohli uložit jeho změny", null);
    }
    return null;
  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
