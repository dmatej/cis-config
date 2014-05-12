package cz.i.cis.config.web.backing.item_key;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static final Logger LOG = LoggerFactory.getLogger(ItemKeyEditBean.class);

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
    LOG.debug("init()");
    allCategories = categoryDao.getCategoryMap();
    try {
      itemKey = itemKeyDao.getItemKey(id);
    } catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage("ItemKey id " + id + " is not valid", null);
    }
    if (itemKey != null) {
      key = itemKey.getKey();
      type = itemKey.getType();
      selectedCategory = itemKey.getCategory().getId().toString();
      description = itemKey.getDescription();
    } else {
      FacesMessagesUtils.addErrorMessage("Zvolený klíč nebyl nalezen v databázi - ID = " + id, null);
    }
  }


  public String actionUpdateItemKey() throws Exception {
    LOG.debug("actionUpdateItemKey()");
    String link = "";
    if (itemKey != null) {
      try {
        if (!allCategories.containsKey(selectedCategory)) {
          throw new IllegalArgumentException("Selected category is not valid.");
        }

        itemKey.setKey(key);
        itemKey.setType(type);
        itemKey.setCategory(allCategories.get(selectedCategory));
        itemKey.setDescription(description);

        itemKey = itemKeyDao.updateItemKey(itemKey);
        link = "list.xhtml#itemKey-" + itemKey.getId();
        FacesUtils.redirect(link);
      }
      catch (IOException exc) {
        FacesMessagesUtils.failedRedirectMessage(link, exc);
      }
      catch (IllegalArgumentException exc) {
        FacesMessagesUtils.addErrorMessage("form:category", exc.getMessage(), null);
      }
      catch (Exception exc) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(exc));
      }
    } else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující klíč, abyste mohli uložit jeho změny.", null);
    }
    return null; // stay on the same page to display the messages
  }


  public Type[] getAllTypes() {
    LOG.debug("getAllTypes()");
    return Type.values();
  }


  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }


  public String getKey() {
    LOG.trace("getKey()");
    return key;
  }


  public void setKey(String key) {
    LOG.debug("setKey(key={})", key);
    this.key = key;
  }


  public Type getType() {
    LOG.trace("getType()");
    return type;
  }


  public void setType(Type type) {
    LOG.debug("setType(type={})", type);
    this.type = type;
  }


  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }


  public void setSelectedCategory(String category) {
    LOG.debug("setSelectedCategory(category={})", category);
    this.selectedCategory = category;
  }


  public String getDescription() {
    LOG.trace("getDescription()");
    return description;
  }


  public void setDescription(String description) {
    LOG.debug("setDescription(description={})", description);
    this.description = description;
  }


  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.debug("getAllCategories()");
    return allCategories.values();
  }
}
