package cz.i.cis.config.web.backing.active_config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

/**
 * Backing bean for active configuration listing.
 */
@Named(value = "activeConfigList")
@ViewScoped
public class ActiveConfigListBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(ActiveConfigListBean.class);

  /** Selection placeholder for "no selection". */
  private static final String NONE_SELECTOR = "none";
  /** Selection placeholder for "all". */
  private static final String ALL_SELECTOR = "all";
  /** Key of session item for category selection. */
  private static final String SESSION_NAME = "active-config-category";

  @EJB
  /**Data access object for item category manipulation.*/
  private ConfigurationCategoryDao categoryDao;
  @EJB
  /**Data access object for active configuration manipulation.*/
  private ConfigurationItemDao configItemDao;

  /** Collection of all item categories. */
  private Map<String, ConfigurationItemCategory> allCategories;
  /** Collection of active items filtered by selected category. */
  private List<ConfigurationItem> filteredActiveItems;

  /** Category selected on page. */
  private String selectedCategory;


  /**
   * Initializes backing bean before user manipulation.
   */
  public void init() {
    LOG.debug("init()");
    try {
      allCategories = categoryDao.getCategoryMap();

      String category = (String) FacesUtils.getSession(SESSION_NAME);
      if (category == null) {
        selectedCategory = NONE_SELECTOR;
      } else {
        selectedCategory = category;
      }

      refreshActiveItems();
    } catch (Exception e) {
      LOG.error("Error during initialization.", e);
      FacesMessagesUtils.addErrorMessage("Nastala chyba při inicializaci.", e);
    }
  }


  /**
   * Refresh active items filtered by selected category.
   */
  private void refreshActiveItems() {
    LOG.trace("refreshActiveItems()");
    try {
      if (NONE_SELECTOR.equals(selectedCategory)) {
        filteredActiveItems = Collections.emptyList();
      } else if (ALL_SELECTOR.equals(selectedCategory)) {
        filteredActiveItems = configItemDao.listItems();
      } else {
        if (!allCategories.containsKey(selectedCategory)) {
          throw new IllegalArgumentException("Vybraná kategorie neexistuje");
        }

        ConfigurationItemCategory filter = allCategories.get(selectedCategory);
        filteredActiveItems = configItemDao.listConfigurationItems(filter);
      }
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to refresh active items.", e);
      FacesMessagesUtils.addErrorMessage(e.getMessage(), "");
    } catch (Exception e) {
      LOG.error("Failed to refresh active items.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo se obnovit aktivní položky", e);
    }
  }


  /**
   * Deletes selected item.
   * @param id ID of item to delete.
   */
  public void actionDeleteItem(String id) {
    LOG.debug("actionDeleteItem(id={})", id);
    try {
      Integer activeItemID = Integer.valueOf(id);
      configItemDao.removeItem(activeItemID);
    } catch (Exception e) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat položku", e);
    }
  }


  /**
   * Returns representation for "all" selection.
   * @return Representation for "all" selection.
   */
  public String getAllSelector() {
    LOG.debug("getAllSelector()");
    return ALL_SELECTOR;
  }

  /**
   * Returns representation for "no selection".
   * @return Representation for "no selection".
   */
  public String getNoneSelector() {
    LOG.debug("getNoneSelector()");
    return NONE_SELECTOR;
  }

  /**
   * Returns collection of available categories.
   * @return Collection of available categories.
   */
  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories.values();
  }

  /**
   * Returns selected category.
   * @return Selected category.
   */
  public String getSelectedCategory() {
    LOG.debug("getSelectedCategory()");
    return selectedCategory;
  }

  /**
   * Sets selected category.
   * @param selectedCategory New selected category.
   */
  public void setSelectedCategory(String selectedCategory) {
    LOG.debug("setSelectedCategory(selectedCategory={})", selectedCategory);
    FacesUtils.setSession(SESSION_NAME, selectedCategory);
    this.selectedCategory = selectedCategory;
  }

  /**
   * Returns collection of active items filtered by selected category.
   * @return Collection of active items filtered by selected category.
   */
  public List<ConfigurationItem> getFilteredActiveItems() {
    LOG.debug("getFilteredActiveItems()");
    refreshActiveItems();
    return filteredActiveItems;
  }
}
