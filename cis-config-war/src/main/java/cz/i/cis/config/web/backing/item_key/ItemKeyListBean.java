package cz.i.cis.config.web.backing.item_key;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.exceptions.ActiveItemKeyException;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;


/**
 * Backing bean for item key listing.
 */
@Named(value = "itemKeyList")
@ViewScoped
public class ItemKeyListBean {

  /**Logger object used for logging.*/
  private static final Logger LOG = LoggerFactory.getLogger(ItemKeyListBean.class);

  /**Selection placeholder for "no selection".*/
  private static final String NONE_SELECTOR = "none";
  /**Selection placeholder for "all" selection.*/
  private static final String ALL_SELECTOR = "all";
  /**Session key for selected category.*/
  private static final String SESSION_NAME = "item-key-category";

  @EJB
  /**Data access object for item key manipulation.*/
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  /**Data access object for item category manipulation.*/
  private ConfigurationCategoryDao categoryDao;

  /**Currently selected item key category.*/
  private String selectedCategory;
  /**Collection of available categories.*/
  private Map<String, ConfigurationItemCategory> allCategories;


  /**
   * Loads available categories.
   */
  @PostConstruct
  public void init() {
    LOG.debug("init()");
    allCategories = categoryDao.getCategoryMap();

    String category =  (String) FacesUtils.getSession(SESSION_NAME);
    selectedCategory = (category == null) ? NONE_SELECTOR : category;
  }


  /**
   * Deletes selected item key.
   * @param id ID of item key to delete.
   */
  public void actionDeleteItemKey(String id) {
    LOG.debug("actionDeleteItemKey()");
    try {
      Integer itemKeyID = Integer.valueOf(id);
      itemKeyDao.removeItemKey(itemKeyID);
    } catch (ActiveItemKeyException e) {
      LOG.warn("Try to remove item key used in active configuration: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("Klíč se používá v aktivní konfiguraci a proto nelze smazat", e);
    } catch (Exception e) {
      LOG.error("Failed to remove item key: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat klíč", e);
    }
  }


  /**
   * Returns collection of item keys filtered by selected category.
   * @return Collection of item keys filtered by selected category.
   */
  public List<ConfigurationItemKey> getFilteredItemKeys() {
    LOG.debug("getFilteredItemKeys()");
    try {
      if (NONE_SELECTOR.equals(selectedCategory)) {
        return Collections.emptyList();
      }

      if (ALL_SELECTOR.equals(selectedCategory)) {
        return itemKeyDao.listItemKeys();
      }

      if (!allCategories.containsKey(selectedCategory)) {
        throw new IllegalArgumentException("Selected category is not valid.");
      }

      ConfigurationItemCategory filter = allCategories.get(selectedCategory);
      return itemKeyDao.filterItemKeys(filter);
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to filter item keys.", e);
      FacesMessagesUtils.addErrorMessage("form:category", e.getMessage(), null);
    } catch(Exception e){
      LOG.error("Failed to filter item keys.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo se načíst klíče položek.", e);
    }
    return Collections.emptyList();
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
   * Returns currently selected category.
   * @return Currently selected category.
   */
  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }

  /**
   * Sets currently selected category.
   * @param selectedCategory Currently selected category.
   */
  public void setSelectedCategory(String selectedCategory) {
    LOG.debug("setSelectedCategory(selectedCategory={})", selectedCategory);
    FacesUtils.setSession(SESSION_NAME, selectedCategory);
    this.selectedCategory = selectedCategory;
  }

  /**
   * Returns collection of available categories.
   * @return Collection of available categories.
   */
  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories.values();
  }
}
