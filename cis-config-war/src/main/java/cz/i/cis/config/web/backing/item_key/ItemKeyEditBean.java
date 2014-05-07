package cz.i.cis.config.web.backing.item_key;

import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;


@Named(value = "itemKeyEdit")
@ViewScoped
public class ItemKeyEditBean {

  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;

  private Map<String, ConfigurationItemCategory> allCategories;
  private ConfigurationItemKey itemKey;

  private Integer id;

  private String key;
  private Type type;
  private String selectedCategory;
  private String description;


  public void init() {
    allCategories = categoryDao.getCategoryMap();
    try {
      itemKey = itemKeyDao.getItemKey(id);
    }
    catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage("edit:key", exc.getMessage(), null);
    }
    if (itemKey != null) {
      key = itemKey.getKey();
      type = itemKey.getType();
      selectedCategory = itemKey.getCategory().getId().toString();
      description = itemKey.getDescription();
    }
    else {
      FacesMessagesUtils.addErrorMessage("Zvolený klíč nebyl nalezen v databázi - ID = " + id, null);
    }
  }


  public String actionUpdateItemKey() throws Exception {
    if (itemKey != null) {
      if (!allCategories.containsKey(selectedCategory)) {
        throw new Exception("Selected category is not valid.");
      }

      itemKey.setKey(key);
      itemKey.setType(type);
      itemKey.setCategory(allCategories.get(selectedCategory));
      itemKey.setDescription(description);

      try {
        itemKey = itemKeyDao.updateItemKey(itemKey);
        FacesUtils.redirect("list.xhtml#itemKey-" + itemKey.getId());
      }
      catch (Exception exc) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(exc));
      }
    }
    else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující klíč, abyste mohli uložit jeho změny.", null);
    }
    return null; // stay on the same page to display the messages
  }


  public Type[] getAllTypes() {
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

  public Collection<ConfigurationItemCategory> getAllCategories() {
    return allCategories.values();
  }
}
