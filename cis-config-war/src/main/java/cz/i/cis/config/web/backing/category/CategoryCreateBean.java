package cz.i.cis.config.web.backing.category;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

/**
 * Backing bean for category creation.
 */
@Named(value = "categoryCreate")
@ViewScoped
public class CategoryCreateBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(CategoryCreateBean.class);

  /** Data access object for item category manipulation. */
  @EJB
  private ConfigurationCategoryDao categoryDao;

  /** Name of new category. */
  private String name;


  /**
   * Adds new item category to database.
   *
   * @return Navigation outcome.
   */
  public String actionAddCategory() {
    LOG.debug("actionAddCategory()");
    String link = "";
    try {
      ConfigurationItemCategory category = categoryDao.getCategory(name);
      if (category != null) {
        FacesMessagesUtils.addErrorMessage("form:name", "Kategorie konfiguračních položek se zadaným jménem již existuje", "");
        return null;
      }

      category = new ConfigurationItemCategory();
      category.setName(name);

      categoryDao.addCategory(category);

      // return "edit?faces-redirect=true&includeViewParams=true&id=" + category.getId();
      link = "list.xhtml#category-" + category.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException e) {
      LOG.error("Failed to create category: cannot redirect", e);
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (Exception e) {
      LOG.error("Failed to create category: cannot create category", e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se přidat novou kategorii konfiguračních položek", e);
    }

    return null;
  }


  /**
   * Returns category name.
   *
   * @return Category name.
   */
  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  /**
   * Sets category name.
   *
   * @param name Category name.
   */
  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }
}
