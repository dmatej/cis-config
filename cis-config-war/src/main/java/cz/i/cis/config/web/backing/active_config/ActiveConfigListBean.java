package cz.i.cis.config.web.backing.active_config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

//TODO asi to nebude fungovat...
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
    LOG.debug("init()");
    allCategories = categoryDao.listCategories();
    selectedCategory = NONE_SELECTOR;

    refreshActiveItems();
  }


  private void refreshActiveItems() throws Exception {
    LOG.trace("refreshActiveItems()");
    if (selectedCategory == NONE_SELECTOR) {
      filteredActiveItems = Collections.emptyList();
    }
    else if (selectedCategory == ALL_SELECTOR) {
      filteredActiveItems = configItemDao.listItems();
    }
    else {
      if (!allCategories.contains(selectedCategory)) {
        throw new Exception("Selected category is not valid.");
      }
      try {
        filteredActiveItems = configItemDao.listConfigurationItems(selectedCategory);
      }
      catch (IllegalArgumentException exc) {
        FacesMessagesUtils.addErrorMessage("list:item", exc.getMessage(), null);
      }

    }
  }


  public void actionDeleteItem(ConfigurationItem toDelete) {
    LOG.debug("actionDeleteItem(toDelete={})", toDelete);
    try {
      configItemDao.removeItem(toDelete);
    }
    catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se smazat profil", FacesUtils.getRootMessage(exc));
    }
  }


  public ConfigurationItemCategory getAllSelector() {
    LOG.debug("getAllSelector()");
    return ALL_SELECTOR;
  }

  public ConfigurationItemCategory getNoneSelector() {
    LOG.debug("getNoneSelector()");
    return NONE_SELECTOR;
  }

  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories;
  }

  public ConfigurationItemCategory getSelectedCategory() {
    LOG.debug("getSelectedCategory()");
    return selectedCategory;
  }

  public void setSelectedCategory(ConfigurationItemCategory selectedCategory) {
    LOG.debug("setSelectedCategory(selectedCategory={})", selectedCategory);
    this.selectedCategory = selectedCategory;
  }

  public List<ConfigurationItem> getFilteredActiveItems() throws Exception {
    LOG.debug("getFilteredActiveItems()");
    refreshActiveItems();
    return filteredActiveItems;
  }

  public void setFilteredActiveItems(List<ConfigurationItem> filteredActiveItems) {
    LOG.debug("setFilteredActiveItems(filteredActiveItems={})", filteredActiveItems);
    this.filteredActiveItems = filteredActiveItems;
  }
}
