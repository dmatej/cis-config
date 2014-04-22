package cz.i.cis.config.web.backing.profile;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "profileCreate")
@ViewScoped
public class ProfileCreateBean {

  @EJB
  private ConfigurationProfileDao profileDao;
  @EJB
  private CisUserDao userDao;

  private String name;
  private String description;


  public String actionAddProfile() throws IOException{
    try{
      String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
      if(login == null || login.isEmpty()){
        throw new Exception("Somehow no user is not logged in and phantoms are not allowed to create configuration profiles.");
      }
      CisUser editor = userDao.getUser(login);
      if(editor == null){
        throw new Exception("Logged in user has not been found in the database.");
      }

      ConfigurationProfile profile = new ConfigurationProfile();
        profile.setName(name);
        profile.setDescription(description);
        profile.setUser(editor);
        profile.setUpdate(new Date(System.currentTimeMillis()));

      profileDao.addProfile(profile);
      return "edit?faces-redirect=true&includeViewParams=true&id=" + profile.getId();
//      FacesUtils.redirect("list.xhtml#user-" + profile.getId());
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se přidat nový profil: " + FacesUtils.getRootMessage(e));
    }
    return null;
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