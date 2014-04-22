package cz.i.cis.config.web.backing.active_config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "activeConfigList")
@ViewScoped
public class ActiveConfigListBean {
  private static final String NONE_SELECTOR = "none";
  private static final String ALL_SELECTOR = "all";

  @EJB
  private ConfigurationCategoryDao categoryDao;
  @EJB
  private ConfigurationItemDao configItemDao;

  private Map<String, ConfigurationItemCategory> allCategories;
  private List<ConfigurationItem> filteredActiveItems;

  private Integer activeConfigID;
  private String selectedCategory;


  public void init() throws Exception{
    selectedCategory = NONE_SELECTOR;
    allCategories = categoryDao.getCategoryMap();
    refreshActiveItems();
  }

  private void refreshActiveItems() throws Exception{
    if(NONE_SELECTOR.equals(selectedCategory)){
      filteredActiveItems = Collections.emptyList();
    }
    else if(ALL_SELECTOR.equals(selectedCategory)){
      filteredActiveItems = configItemDao.listItems();
    }
    else{
      if(!allCategories.containsKey(selectedCategory)){
        throw new Exception("Selected category is not valid.");
      }

      ConfigurationItemCategory filter = allCategories.get(selectedCategory);

      filteredActiveItems = configItemDao.listConfigurationItems(filter);
    }
  }

  public String actionDeleteActiveConfig(){
    try{
      configItemDao.removeItem(activeConfigID);
      return "list?faces-redirect=true";
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se smazat profil: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }


  public String getAllSelector(){
    return ALL_SELECTOR;
  }

  public String getNoneSelector(){
    return NONE_SELECTOR;
  }

  public Collection<ConfigurationItemCategory> getAllCategories() {
    return allCategories.values();
  }

  public String getSelectedCategory() {
    return selectedCategory;
  }

  public void setSelectedCategory(String selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  public List<ConfigurationItem> getFilteredActiveItems() throws Exception {
    refreshActiveItems();

    return filteredActiveItems;
  }

  public void setFilteredActiveItems(List<ConfigurationItem> filteredActiveItems) {
    this.filteredActiveItems = filteredActiveItems;
  }

  public Integer getActiveConfigID() {
    return activeConfigID;
  }

  public void setActiveConfigID(Integer activeConfigID) {
    this.activeConfigID = activeConfigID;
  }
}
