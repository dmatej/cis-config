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
 * Backing bean for profile items manipulation.
 */
@Named(value = "categoryEdit")
@ViewScoped
public class CategoryEditBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(CategoryEditBean.class);

  @EJB
  /**Data access object for item category manipulation.*/
  private ConfigurationCategoryDao categoryDao;

  /** ID of currently edited category. It is set via request parameter. */
  private Integer id;
  /** Currently edited profile. It is initialized in init() method with use of id field. */
  private ConfigurationItemCategory category;

  /** Name of edited category. */
  private String name;


  /**
   * Loads category with given ID.
   */
  public void init() {
    LOG.debug("init()");
    try {
      category = categoryDao.getCategory(id);
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to load category.", e);
      FacesMessagesUtils.addErrorMessage("ID kategorie není validní: ID = " + id, e);
      FacesUtils.redirectToOutcome("list");
      return;
    }

    if (category == null) {
      LOG.error("Profile not loaded while initializing, redirecting to list: ID = {}", id);
      FacesMessagesUtils.addErrorMessage("Zvolená kategorie nebyla nalezena v databázi - ID = " + id, "");
      FacesUtils.redirectToOutcome("list");
      return;
    }

    name = category.getName();
  }


  /**
   * Persists changes of edited category.
   *
   * @return Navigation outcome.
   */
  public void actionUpdateCategory() {
    LOG.debug("actionUpdateCategory()");
    if (category == null) {
      LOG.error("Cannot edit category which is null");
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující kategorii, abyste mohli uložit její změny.", "");

      return;
    }

    String link = "";
    try {
      category.setName(name);

      category = categoryDao.updateCategory(category);

      link = "list.xhtml#category-" + category.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException e) {
      LOG.error("Failed to update category: category = " + category, e);
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (Exception e) {
      LOG.error("Failed to update category: category = " + category, e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se uložit změny", e);
    }
  }


  /**
   * Returns edited category ID.
   *
   * @return Edited category ID.
   */
  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  /**
   * Sets edited category ID.
   *
   * @param id Edited category ID.
   */
  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }


  /**
   * Returns edited category name.
   *
   * @return Edited category name.
   */
  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  /**
   * Sets edited category name.
   *
   * @param name Edited category name.
   */
  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }
}
