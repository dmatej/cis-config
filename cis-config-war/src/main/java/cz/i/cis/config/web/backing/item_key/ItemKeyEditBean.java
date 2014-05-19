package cz.i.cis.config.web.backing.item_key;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

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
 * Backing bean for category editing.
 */
@Named(value = "itemKeyEdit")
@ViewScoped
public class ItemKeyEditBean {

  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(ItemKeyEditBean.class);

  @EJB
  /**Data access object for item key manipulation.*/
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  /**Data access object for item category manipulation.*/
  private ConfigurationCategoryDao categoryDao;

  /** Collection of available item categories. */
  private Map<String, ConfigurationItemCategory> allCategories;

  /** ID of currently edited item key. It is set via request parameter. */
  private Integer id;
  /** Currently edited item key. It is initialized in init() method with use of id field. */
  private ConfigurationItemKey itemKey;

  /** Edited item key name. */
  private String key;
  /** Edited item key type. */
  private ConfigurationItemKeyType type;
  /** Edited item key category. */
  private String selectedCategory;
  /** Edited item key description. */
  private String description;


  /**
   * Loads categories and edited item key from database.
   */
  public void init() {
    LOG.debug("init()");

    try {
      allCategories = categoryDao.getCategoryMap();
    } catch (Exception e) {
      LOG.error("Failed to load categories.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo se načíst kategorie klíčů", e);
      FacesUtils.redirectToOutcome("list");
      // allCategories = Collections.emptyMap(); Why will you show form for nothing?
      return; // for sure.
    }

    try {
      itemKey = itemKeyDao.getItemKey(id);
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to load item key.", e);
      FacesMessagesUtils.addErrorMessage("ID klíče není validní: ID = " + id, e);
      FacesUtils.redirectToOutcome("list"); // if you don't do this, you will replace message.
      return; // for sure.
    }

    if (itemKey == null) {
      LOG.error("Profile not loaded while initializing, redirecting to list: ID = {}", id);
      FacesMessagesUtils.addErrorMessage("Zvolený klíč nebyl nalezen v databázi - ID = " + id, "");
      FacesUtils.redirectToOutcome("list");
      return; // for sure.
    }

    key = itemKey.getKey();
    type = itemKey.getType();
    selectedCategory = itemKey.getCategory().getId().toString();
    description = itemKey.getDescription();
  }


  /**
   * Persist changes of edited item key.
   *
   * @return Navigation outcome.
   */
  public void actionUpdateItemKey() {
    LOG.debug("actionUpdateItemKey()");
    if (itemKey == null) {
      LOG.error("Cannot edit itemKey which is null");
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující klíč, abyste mohli uložit jeho změny.", "");
      return;
    }

    String link = "";
    try {
      if (!allCategories.containsKey(selectedCategory)) {
        throw new NonExistentCategoryException();
      }

      itemKey.setKey(key);
      itemKey.setType(type);
      itemKey.setCategory(allCategories.get(selectedCategory));
      itemKey.setDescription(description);

      itemKey = itemKeyDao.updateItemKey(itemKey);

      link = "list.xhtml#itemKey-" + itemKey.getId();
      FacesUtils.redirectToURL(link);
    } catch (IOException e) {
      LOG.error("Failed to redirect: link = {}", link);
      FacesMessagesUtils.failedRedirectMessage(link, e);
    } catch (NonExistentCategoryException e) {
      LOG.error("Failed to update item key.");
      FacesMessagesUtils.addErrorMessage("form:category", e.getMessage(), "");
    } catch (Exception e) {
      LOG.error("Failed to update item key.");
      FacesMessagesUtils.addErrorMessage("form", "Nepodařilo se uložit změny", e);
    }

    return;
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
   * Returns ID of edited category.
   *
   * @return ID of edited category.
   */
  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  /**
   * Sets ID of edited item key.
   *
   * @param id ID of edited item key.
   */
  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }


  /**
   * Returns name of edited item key.
   *
   * @return Name of edited item key.
   */
  public String getKey() {
    LOG.trace("getKey()");
    return key;
  }


  /**
   * Sets name of edited item key.
   *
   * @param key Name of edited item key.
   */
  public void setKey(String key) {
    LOG.debug("setKey(key={})", key);
    this.key = key;
  }


  /**
   * Returns type of edited item key.
   *
   * @returnType of edited item key.
   */
  public ConfigurationItemKeyType getType() {
    LOG.trace("getType()");
    return type;
  }


  /**
   * Sets type of edited item key.
   *
   * @param typeType of edited item key.
   */
  public void setType(ConfigurationItemKeyType type) {
    LOG.debug("setType(type={})", type);
    this.type = type;
  }


  /**
   * Returns category of edited item key.
   *
   * @return Category of edited item key.
   */
  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }


  /**
   * Sets category of edited item key.
   *
   * @param category Category of edited item key.
   */
  public void setSelectedCategory(String category) {
    LOG.debug("setSelectedCategory(category={})", category);
    this.selectedCategory = category;
  }


  /**
   * Returns description of edited item key.
   *
   * @return Description of edited item key.
   */
  public String getDescription() {
    LOG.trace("getDescription()");
    return description;
  }


  /**
   * Sets description of edited item key.
   *
   * @param description Description of edited item key.
   */
  public void setDescription(String description) {
    LOG.debug("setDescription(description={})", description);
    this.description = description;
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
}
