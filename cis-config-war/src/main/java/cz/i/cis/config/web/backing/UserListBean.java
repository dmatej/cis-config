package cz.i.cis.config.web.backing;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.UserDao;
import cz.i.cis.config.jpa.CisUser;


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
    userDao.removeUser(userID);
    return "user-list?faces-redirect=true";
  }



  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getUserID() {
    return userID;
  }
}
