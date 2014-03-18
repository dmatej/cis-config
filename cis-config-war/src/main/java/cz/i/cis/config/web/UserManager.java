package cz.i.cis.config.web;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cz.i.cis.config.ejb.UserDao;
import cz.i.cis.config.jpa.CisUser;

@Named(value = "userManager")
@RequestScoped
public class UserManager {

  @EJB
  private UserDao userDao;


  public List<CisUser> getAllUsers() {
    return userDao.listUsers();
  }


  public String getJsfImplementation() {
    return FacesContext.class.getPackage().getImplementationTitle();
  }


  public String getJsfVersion() {
    return FacesContext.class.getPackage().getImplementationVersion();
  }
}
