package cz.i.cis.config.web.backing.profile;

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


@Named(value = "profileEdit")
@ViewScoped
public class ProfileEditBean {

  @EJB
  private ConfigurationProfileDao profileDao;
  @EJB
  private CisUserDao userDao;

  private Long id;

  private ConfigurationProfile profile;
  //TODO seznam položek, přidávání, odebírání, uložení

  private String name;
  private String description;


  public void init(){
    profile = profileDao.getProfile(id);

    if(profile != null){
      name = profile.getName();
      description= profile.getDescription();
    }
    else{
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Zvolený profil nebyl nalezen v databázi - ID = " + id);
    }
  }

  public String actionUpdateProfileMetadata(){
    if(profile != null){  //TODO to ukládání je i u vytváření nového profilu, nejlíp sjednotit
      try{
      String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
      if(login == null || login.isEmpty()){
        throw new Exception("Somehow no user is not logged in and phantoms are not allowed to create configuration profiles.");
      }
      CisUser editor = userDao.getUser(login);
      if(editor == null){
        throw new Exception("Logged in user has not been found in the database.");
      }

      profile.setName(name);
      profile.setDescription(description);
      profile.setUser(editor);
      profile.setUpdate(new Date());

      profile = profileDao.updateProfile(profile);
        FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Změny byly uloženy.");
//        FacesUtils.redirect("list.xhtml#profile-" + profile.getId());
      }
      catch(Exception e){
        FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se uložit změny: " + FacesUtils.getRootMessage(e));
      }
    }
    else{
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Musíte editovat existující profil, abyste mohli uložit jeho změny.");
    }
    return null;  //stay on the same page to display the messages
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
