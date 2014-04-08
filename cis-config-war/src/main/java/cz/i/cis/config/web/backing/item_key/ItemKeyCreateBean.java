package cz.i.cis.config.web.backing.item_key;

import java.io.IOException;
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
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "itemKeyCreate")
@ViewScoped
public class ItemKeyCreateBean {

  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;


  private List<ConfigurationItemCategory> allCategories;

  private String key;
  private Type type;
  private ConfigurationItemCategory category;
  private String description;


  @PostConstruct
  public void init(){
    allCategories = categoryDao.listCategories();
  }


  public String actionAddItemKey() throws IOException{
    ConfigurationItemKey newItemKey = new ConfigurationItemKey();
      newItemKey.setKey(key);
      newItemKey.setType(type);
      newItemKey.setCategory(category);
      newItemKey.setDescription(description);

    try{
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

  public List<ConfigurationItemCategory> getAllCategories(){
    return allCategories;
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

  public ConfigurationItemCategory getCategory() {
    return category;
  }

  public void setCategory(ConfigurationItemCategory category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
