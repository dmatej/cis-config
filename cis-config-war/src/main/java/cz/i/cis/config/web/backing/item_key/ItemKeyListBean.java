package cz.i.cis.config.web.backing.item_key;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "itemKeyList")
@ViewScoped
public class ItemKeyListBean {
  private static final String NONE_SELECTOR = "none";
  private static final String ALL_SELECTOR = "all";


  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;


  private Integer itemKeyID;

  private String selectedCategory;
  private Map<String, ConfigurationItemCategory> allCategories;


  @PostConstruct
  public void init(){
    allCategories = categoryDao.getCategoryMap();
    selectedCategory = NONE_SELECTOR;
  }



  public String actionDeleteItemKey(){
    try{
      itemKeyDao.removeItemKey(itemKeyID);;
      return "list?faces-redirect=true";
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se smazat uživatele: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }



  public List<ConfigurationItemKey> getFilteredItemKeys() throws Exception{
    if(NONE_SELECTOR.equals(selectedCategory)){
      return Collections.emptyList();
    }
    if(ALL_SELECTOR.equals(selectedCategory)){
      return itemKeyDao.listItemKeys();
    }

    if(!allCategories.containsKey(selectedCategory)){
      throw new Exception("Selected category is not valid.");
    }

    ConfigurationItemCategory filter = allCategories.get(selectedCategory);
    return itemKeyDao.filterItemKeys(filter);
  }

  public String getAllSelector(){
    return ALL_SELECTOR;
  }

  public String getNoneSelector(){
    return NONE_SELECTOR;
  }

  public Integer getItemKeyID() {
    return itemKeyID;
  }

  public void setItemKeyID(Integer itemKeyID) {
    this.itemKeyID = itemKeyID;
  }

  public String getSelectedCategory() {
    return selectedCategory;
  }

  public void setSelectedCategory(String selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  public Collection<ConfigurationItemCategory> getAllCategories() {
    return allCategories.values();
  }
}
