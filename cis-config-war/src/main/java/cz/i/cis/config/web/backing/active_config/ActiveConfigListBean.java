package cz.i.cis.config.web.backing.active_config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "activeConfigList")
@ViewScoped
public class ActiveConfigListBean {

  private static final ConfigurationItemCategory NONE_SELECTOR = new ConfigurationItemCategory();
  private static final ConfigurationItemCategory ALL_SELECTOR = new ConfigurationItemCategory();

  @EJB
  private ConfigurationCategoryDao categoryDao;
  @EJB
  private ConfigurationItemDao configItemDao;

  private List<ConfigurationItemCategory> allCategories;
  private List<ConfigurationItem> filteredActiveItems;

  private ConfigurationItemCategory selectedCategory;


  public void init() throws Exception {
    allCategories = categoryDao.listCategories();
    selectedCategory = NONE_SELECTOR;

    refreshActiveItems();
  }


  private void refreshActiveItems() throws Exception {
    if (selectedCategory == NONE_SELECTOR) {
      filteredActiveItems = Collections.emptyList();
    } else if (selectedCategory == ALL_SELECTOR) {
      filteredActiveItems = configItemDao.listItems();
    } else {
      if (!allCategories.contains(selectedCategory)) {
        throw new Exception("Selected category is not valid.");
      }
      try {
        filteredActiveItems = configItemDao.listConfigurationItems(selectedCategory);
      } catch (IllegalArgumentException exc) {
        FacesMessagesUtils.addErrorMessage("list:item", exc.getMessage(), null);
      }

    }
  }


  public void actionDeleteItem(ConfigurationItem toDelete) {
    try {
      configItemDao.removeItem(toDelete);
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat profil", FacesUtils.getRootMessage(exc));
    }
  }


  public ConfigurationItemCategory getAllSelector() {
    return ALL_SELECTOR;
  }


  public ConfigurationItemCategory getNoneSelector() {
    return NONE_SELECTOR;
  }


  public Collection<ConfigurationItemCategory> getAllCategories() {
    return allCategories;
  }


  public ConfigurationItemCategory getSelectedCategory() {
    return selectedCategory;
  }


  public void setSelectedCategory(ConfigurationItemCategory selectedCategory) {
    this.selectedCategory = selectedCategory;
  }


  public List<ConfigurationItem> getFilteredActiveItems() throws Exception {
    refreshActiveItems();

    return filteredActiveItems;
  }


  public void setFilteredActiveItems(List<ConfigurationItem> filteredActiveItems) {
    this.filteredActiveItems = filteredActiveItems;
  }
}
