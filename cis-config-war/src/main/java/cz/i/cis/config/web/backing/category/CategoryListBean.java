package cz.i.cis.config.web.backing.category;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "categoryList")
@ViewScoped
public class CategoryListBean {

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private Integer categoryID;


  public List<ConfigurationItemCategory> getAllCategories() {
    return categoryDao.listCategories();
  }


  public String actionDeleteCategory(){
    try{
      categoryDao.removeCategory(categoryID);
      return "list?faces-redirect=true";
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se smazat kategorii: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }


  public Integer getCategoryID() {
    return categoryID;
  }

  public void setCategoryID(Integer categoryID) {
    this.categoryID = categoryID;
  }
}
