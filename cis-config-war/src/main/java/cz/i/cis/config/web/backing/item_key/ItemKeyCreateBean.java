package cz.i.cis.config.web.backing.item_key;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;
import cz.i.cis.config.web.exceptions.NonExistentCategoryException;

/**
 * Backing bean for item key creation.
 */
@Named(value = "itemKeyCreate")
@ViewScoped
public class ItemKeyCreateBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(ItemKeyCreateBean.class);

  /** Data access object for item key manipulation. */
  @EJB
  private ConfigurationItemKeyDao itemKeyDao;

  /** Data access object for item category manipulation. */
  @EJB
  private ConfigurationCategoryDao categoryDao;

  /** Collection of all item categories. */
  private Map<String, ConfigurationItemCategory> allCategories;

  /** New item key name. */
  private String key;
  /** New item key type. */
  private ConfigurationItemKeyType type;
  /** New item key category. */
  private String selectedCategory;
  /** New item key description. */
  private String description;


  /**
   * Loads available categories.
   */
  @PostConstruct
  public void init() {
    LOG.debug("init()");
    allCategories = categoryDao.getCategoryMap();
    selectedCategory = (String) FacesUtils.getSession(ItemKeyListBean.SESSION_NAME);
  }


  /**
   * Adds new item key to database.
   *
   * @return Navigation outcome.
   */
  public String actionAddItemKey() {
    LOG.debug("actionAddItemKey()");
    String link = "";
    try {
      if (!allCategories.containsKey(selectedCategory)) {
        throw new NonExistentCategoryException();
      }

      ConfigurationItemKey newItemKey = itemKeyDao.getItemKey(key);
      if (newItemKey != null) {
        FacesMessagesUtils.addErrorMessage("form:key", "Klíč pro konfigurační položky se zadaným jménem již existuje", "");
        return null;
      }

      newItemKey = new ConfigurationItemKey();
      newItemKey.setKey(key);
      newItemKey.setType(type);
      newItemKey.setCategory(allCategories.get(selectedCategory));
      newItemKey.setDescription(description);

      itemKeyDao.addItemKey(newItemKey);

      // return "edit?faces-redirect=true&includeViewParams=true&id=" + newUser.getId();
      link = "list.xhtml#itemKey-" + newItemKey.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException e) {
      LOG.error("Failed to add item key: failed to redirect.", e);
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (NonExistentCategoryException e) {
      LOG.error("Failed to add item key: nonexistent category", e);
      FacesMessagesUtils.addErrorMessage("form:category", e.getMessage(), "");
    } catch (Exception e) {
      LOG.error("Failed to add item key.", e);
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se přidat nový klíč pro konfigurační položky", e);
    }

    return null;
  }


  /**
   * Returns collection of available item key types.
   *
   * @return Collection of available item key types.
   */
  public ConfigurationItemKeyType[] getAllTypes() {
    LOG.debug("getAllTypes()");
    return ConfigurationItemKeyType.values();
  }


  /**
   * Returns collection of available categories.
   *
   * @return Collection of available categories.
   */
  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories.values();
  }


  /**
   * Returns item key name.
   *
   * @return Item key name.
   */
  public String getKey() {
    LOG.trace("getKey()");
    return key;
  }


  /**
   * Sets item key name.
   *
   * @param key Sets item key name.
   */
  public void setKey(String key) {
    LOG.debug("setKey(key={})", key);
    this.key = key;
  }


  /**
   * Returns item key type.
   *
   * @return Item key type.
   */
  public ConfigurationItemKeyType getType() {
    LOG.trace("getType()");
    return type;
  }


  /**
   * Sets item key type.
   *
   * @param type Item key type.
   */
  public void setType(ConfigurationItemKeyType type) {
    LOG.debug("setType(type={})", type);
    this.type = type;
  }


  /**
   * Returns selected category.
   *
   * @return Selected category.
   */
  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }


  /**
   * Sets selected category.
   *
   * @param category Selected category.
   */
  public void setSelectedCategory(String category) {
    LOG.debug("setSelectedCategory(category={})", category);
    this.selectedCategory = category;
  }


  /**
   * Returns item key description.
   *
   * @return Item key description.
   */
  public String getDescription() {
    LOG.trace("getDescription()");
    return description;
  }


  /**
   * Sets item key description.
   *
   * @param description Item key description.
   */
  public void setDescription(String description) {
    LOG.debug("setDescription(description={})", description);
    this.description = description;
  }
}
