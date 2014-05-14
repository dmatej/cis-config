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

@Named(value = "activeConfigList")
@ViewScoped
public class ActiveConfigListBean {

  private static final Logger LOG = LoggerFactory.getLogger(ActiveConfigListBean.class);

  private static final String NONE_SELECTOR = "none";
  private static final String ALL_SELECTOR = "all";
  private static final String SESSION_NAME = "active-config-category";

  @EJB
  private ConfigurationCategoryDao categoryDao;
  @EJB
  private ConfigurationItemDao configItemDao;

  private Map<String, ConfigurationItemCategory> allCategories;
  private List<ConfigurationItem> filteredActiveItems;

  private String selectedCategory;

  private Integer activeItemID;


  public void init() throws Exception {
    LOG.debug("init()");
    allCategories = categoryDao.getCategoryMap();

    String category = (String) FacesUtils.getSession(SESSION_NAME);
    if (category == null) {
      selectedCategory = NONE_SELECTOR;
    } else {
      selectedCategory = category;
    }

    refreshActiveItems();
  }


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
    } catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage(exc.getMessage(), null);
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se obnovit aktivní položky", FacesUtils.getRootMessage(exc));
    }

  }


  public void actionDeleteItem() {
    LOG.debug("actionDeleteItem()");
    try {
      configItemDao.removeItem(activeItemID);
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat položku", FacesUtils.getRootMessage(exc));
    }
  }


  public String getAllSelector() {
    LOG.debug("getAllSelector()");
    return ALL_SELECTOR;
  }


  public String getNoneSelector() {
    LOG.debug("getNoneSelector()");
    return NONE_SELECTOR;
  }


  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories.values();
  }


  public String getSelectedCategory() {
    LOG.debug("getSelectedCategory()");
    return selectedCategory;
  }


  public void setSelectedCategory(String selectedCategory) {
    LOG.debug("setSelectedCategory(selectedCategory={})", selectedCategory);
    FacesUtils.setSession(SESSION_NAME, selectedCategory);
    this.selectedCategory = selectedCategory;
  }


  public List<ConfigurationItem> getFilteredActiveItems() {
    LOG.debug("getFilteredActiveItems()");
    refreshActiveItems();
    return filteredActiveItems;
  }


  public void setFilteredActiveItems(List<ConfigurationItem> filteredActiveItems) {
    LOG.debug("setFilteredActiveItems(filteredActiveItems={})", filteredActiveItems);
    this.filteredActiveItems = filteredActiveItems;
  }


  public Integer getActiveItemID() {
    LOG.debug("getActiveItemID()");
    return activeItemID;
  }


  public void setActiveItemID(Integer activeItemID) {
    LOG.debug("setActiveItemID(activeConfigID={})", activeItemID);
    this.activeItemID = activeItemID;
  }

}
