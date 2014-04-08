package cz.i.cis.config.web.backing.item_key;

import java.util.Collections;
import java.util.List;

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
  private static final ConfigurationItemCategory NONE_SELECTOR = null;
  private static final ConfigurationItemCategory ALL_SELECTOR = new ConfigurationItemCategory();


  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;


  private Integer itemKeyID;

  private ConfigurationItemCategory selectedCategory;
  private List<ConfigurationItemCategory> allCategories;


  @PostConstruct
  public void init(){
    allCategories = categoryDao.listCategories();
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



  public List<ConfigurationItemKey> getFilteredItemKeys(){
    if(selectedCategory == NONE_SELECTOR){
      return Collections.emptyList();
    }
    if(selectedCategory == ALL_SELECTOR){
      return itemKeyDao.listItemKeys();
    }
    return itemKeyDao.filterItemKeys(selectedCategory);
  }

  public ConfigurationItemCategory getAllSelector(){
    return ALL_SELECTOR;
  }

  public ConfigurationItemCategory getNoneSelector(){
    return NONE_SELECTOR;
  }

  public Integer getItemKeyID() {
    return itemKeyID;
  }

  public void setItemKeyID(Integer itemKeyID) {
    this.itemKeyID = itemKeyID;
  }

  public ConfigurationItemCategory getSelectedCategory() {
    return selectedCategory;
  }

  public void setSelectedCategory(ConfigurationItemCategory selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  public List<ConfigurationItemCategory> getAllCategories() {
    return allCategories;
  }
}
