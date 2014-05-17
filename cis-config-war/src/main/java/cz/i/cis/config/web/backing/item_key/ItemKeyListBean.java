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

@Named(value = "itemKeyList")
@ViewScoped
public class ItemKeyListBean {

  private static final Logger LOG = LoggerFactory.getLogger(ItemKeyListBean.class);

  private static final String NONE_SELECTOR = "none";
  private static final String ALL_SELECTOR = "all";
  private static final String SESSION_NAME = "item-key-category";

  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;

  private String selectedCategory;
  private Map<String, ConfigurationItemCategory> allCategories;

  private Integer itemKeyID;


  @PostConstruct
  public void init() {
    LOG.debug("init()");
    allCategories = categoryDao.getCategoryMap();

    String category = (String) FacesUtils.getSession(SESSION_NAME);
    if (category == null) {
      selectedCategory = NONE_SELECTOR;
    } else {
      selectedCategory = category;
    }
  }


  public String actionDeleteItemKey() {
    LOG.debug("actionDeleteItemKey()");
    try {
      itemKeyDao.removeItemKey(itemKeyID);
      return "list?faces-redirect=true";
    } catch (ActiveItemKeyException exc) {
      FacesMessagesUtils.addErrorMessage("Klíč se používá v aktivní konfiguraci a proto nelze smazat",
          FacesMessagesUtils.getRootMessage(exc));
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat klíč", FacesMessagesUtils.getRootMessage(exc));
    }
    return null;
  }


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
    } catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage("form:category", exc.getMessage(), null);
    }
    ConfigurationItemCategory filter = allCategories.get(selectedCategory);

    return itemKeyDao.filterItemKeys(filter);
  }


  public Integer getItemKeyID() {
    LOG.debug("getItemKeyID()");
    return itemKeyID;
  }


  public void setItemKeyID(Integer itemKeyID) {
    LOG.debug("setItemKeyID(itemKeyID={})", itemKeyID);
    this.itemKeyID = itemKeyID;
  }


  public String getAllSelector() {
    LOG.debug("getAllSelector()");
    return ALL_SELECTOR;
  }


  public String getNoneSelector() {
    LOG.debug("getNoneSelector()");
    return NONE_SELECTOR;
  }


  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }


  public void setSelectedCategory(String selectedCategory) {
    LOG.debug("setSelectedCategory(selectedCategory={})", selectedCategory);
    FacesUtils.setSession(SESSION_NAME, selectedCategory);
    this.selectedCategory = selectedCategory;
  }


  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories.values();
  }
}
