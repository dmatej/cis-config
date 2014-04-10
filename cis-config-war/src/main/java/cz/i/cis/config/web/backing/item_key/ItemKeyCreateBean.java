package cz.i.cis.config.web.backing.item_key;

import java.util.Collection;
import java.util.HashMap;
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
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "itemKeyCreate")
@ViewScoped
public class ItemKeyCreateBean {

  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;


  private Map<String, ConfigurationItemCategory> allCategories;

  private String key;
  private Type type;
  private String selectedCategory;
  private String description;


  @PostConstruct
  public void init(){
    allCategories = new HashMap<>();
    for (ConfigurationItemCategory category : categoryDao.listCategories()) {
      allCategories.put(category.getId().toString(), category);
    }
  }


  public String actionAddItemKey(){
    try{
      if(!allCategories.containsKey(selectedCategory)){
        throw new Exception("Selected category is not valid.");
      }

      ConfigurationItemKey newItemKey = new ConfigurationItemKey();
        newItemKey.setKey(key);
        newItemKey.setType(type);
        newItemKey.setCategory(allCategories.get(selectedCategory));
        newItemKey.setDescription(description);

      itemKeyDao.addItemKey(newItemKey);
//    return "edit?faces-redirect=true&includeViewParams=true&id=" + newUser.getId();
      FacesUtils.redirect("list.xhtml#itemKey-" + newItemKey.getId());
    }
    catch(Exception e){
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se přidat nový klíč: " + FacesUtils.getRootMessage(e));
    }
    return null;
  }


  public Type[] getAllTypes(){
    return Type.values();
  }

  public Collection<ConfigurationItemCategory> getAllCategories(){
    return allCategories.values();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getSelectedCategory() {
    return selectedCategory;
  }

  public void setSelectedCategory(String category) {
    this.selectedCategory = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
