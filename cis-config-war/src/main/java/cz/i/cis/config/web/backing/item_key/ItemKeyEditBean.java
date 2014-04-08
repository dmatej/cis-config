package cz.i.cis.config.web.backing.item_key;

import java.util.List;

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


@Named(value = "itemKeyEdit")
@ViewScoped
public class ItemKeyEditBean {

  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;


  private List<ConfigurationItemCategory> allCategories;
  private ConfigurationItemKey itemKey;

  private Integer id;

  private String key;
  private Type type;
  private ConfigurationItemCategory category;
  private String description;


  public void init(){
    allCategories = categoryDao.listCategories();

    itemKey = itemKeyDao.getItemKey(id);
    if(itemKey != null){
      key = itemKey.getKey();
      type = itemKey.getType();
      category = itemKey.getCategory();
      description = itemKey.getDescription();
    }
    else{
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Zvolený klíč nebyl nalezen v databázi - ID = " + id);
    }
  }


  public String actionUpdateItemKey(){
    if(itemKey != null){
      itemKey.setKey(key);
      itemKey.setType(type);
      itemKey.setCategory(category);
      itemKey.setDescription(description);

      try{
        itemKey = itemKeyDao.updateItemKey(itemKey);
  //      FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Změny byly uloženy.");
        FacesUtils.redirect("list.xhtml#itemKey-" + itemKey.getId());
      }
      catch(Exception e){
        FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Nepodařilo se uložit změny: " + FacesUtils.getRootMessage(e));
      }
    }
    else{
      FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Musíte editovat existující klíč, abyste mohli uložit jeho změny.");
    }
    return null;  //stay on the same page to display the messages
  }



  public Type[] getAllTypes(){
    return Type.values();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public List<ConfigurationItemCategory> getAllCategories() {
    return allCategories;
  }
}
