package cz.i.cis.config.web.backing.category;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;

/**
 * Backing bean for profile items manipulation.
 */
@Named(value = "categoryList")
@ViewScoped
public class CategoryListBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(CategoryListBean.class);

  /** Data access object for item category manipulation. */
  @EJB
  private ConfigurationItemCategoryDao categoryDao;


  /**
   * Returns collection of available categories.
   *
   * @return Collection of available categories.
   */
  public List<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return categoryDao.listCategories();
  }


  /**
   * Deletes selected category.
   *
   * @param id ID of category to delete.
   */
  public void actionDeleteCategory(String id) {
    LOG.debug("actionDeleteCategory()");
    try {
      Integer categoryID = Integer.valueOf(id);
      categoryDao.removeCategory(categoryID);
      FacesMessagesUtils.addInfoMessage("form", "Kategorie konfiguračních položek byla smazána", "");
    } catch (Exception e) {
      LOG.error("Failed to remove category: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se smazat kategorii konfiguračních položek", e);
    }
  }
}
