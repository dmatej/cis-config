package cz.i.cis.config.web.backing;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.UserDao;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "userList")
@ViewScoped
public class UserListBean {

  @EJB
  private UserDao userDao;

  private Integer userID;


  public List<CisUser> getAllUsers() {
    return userDao.listUsers();
  }


  public String actionDeleteUser(){
    try{
      userDao.removeUser(userID);
      return "user-list?faces-redirect=true";
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se smazat uživatele: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }



  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getUserID() {
    return userID;
  }
}
