package cz.i.cis.config.web.backing.category;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "categoryList")
@ViewScoped
public class CategoryListBean {

  private static final Logger LOG = LoggerFactory.getLogger(CategoryListBean.class);

  @EJB
  private ConfigurationCategoryDao categoryDao;

  private Integer categoryID;


  public List<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return categoryDao.listCategories();
  }


  public String actionDeleteCategory() {
    LOG.debug("actionDeleteCategory()");
    try {
      categoryDao.removeCategory(categoryID);

      return "list?faces-redirect=true";
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat kategorii", FacesMessagesUtils.getRootMessage(exc));
    }

    return null;
  }


  public Integer getCategoryID() {
    LOG.debug("getCategoryID()");
    return categoryID;
  }


  public void setCategoryID(Integer categoryID) {
    LOG.debug("setCategoryID(profileID={})", categoryID);
    this.categoryID = categoryID;
  }
}
