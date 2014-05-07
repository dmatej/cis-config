package cz.i.cis.config.web.backing.item_key;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.exceptions.UniqueKeyException;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.web.FacesMessagesUtils;
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
  public void init() {
    allCategories = categoryDao.getCategoryMap();
  }


  public String actionAddItemKey() {
    String link = "";
    try {
      if (!allCategories.containsKey(selectedCategory)) {
        throw new IllegalArgumentException("Selected category is not valid.");
      }

      ConfigurationItemKey newItemKey = new ConfigurationItemKey();
      newItemKey.setKey(key);
      newItemKey.setType(type);
      newItemKey.setCategory(allCategories.get(selectedCategory));
      newItemKey.setDescription(description);

      itemKeyDao.addItemKey(newItemKey);

      // return "edit?faces-redirect=true&includeViewParams=true&id=" + newUser.getId();
      link = "list.xhtml#itemKey-" + newItemKey.getId();
      FacesUtils.redirect(link);
    }
    catch (UniqueKeyException exc) {
      FacesMessagesUtils.addErrorMessage("form:key", "Nepodařilo se přidat nový klíč", FacesUtils.getRootMessage(exc));
    }
    catch (IOException exc) {
      FacesMessagesUtils.failedRedirectMessage(link, exc);
    }
    catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage("form:category", exc.getMessage(), null);
    }
    catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se přidat nový klíč", FacesUtils.getRootMessage(exc));
    }
    return null;
  }


  public Type[] getAllTypes() {
    return Type.values();
  }

  public Collection<ConfigurationItemCategory> getAllCategories() {
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
