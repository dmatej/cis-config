package cz.i.cis.config.web.backing.category;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
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


  public String actionDeleteCategory() {
    try {
      categoryDao.removeCategory(categoryID);

      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat kategorii", FacesUtils.getRootMessage(exc));
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
