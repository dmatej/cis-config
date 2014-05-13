package cz.i.cis.config.web.backing.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileItemDao;
import cz.i.cis.config.ejb.dao.exceptions.UniqueProfileKeyException;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;
import cz.i.cis.config.web.FacesMessagesUtils;
import cz.i.cis.config.web.FacesUtils;

@Named(value = "profileEdit")
@ViewScoped
public class ProfileEditBean {

  private static final Logger LOG = LoggerFactory.getLogger(ProfileEditBean.class);

  private static final String NONE_SELECTOR = "none";

  @EJB
  private ConfigurationProfileDao profileDao;
  @EJB
  private ConfigurationProfileItemDao itemDao;
  @EJB
  private ConfigurationCategoryDao categoryDao;
  @EJB
  private ConfigurationItemKeyDao itemKeyDao;
  @EJB
  private CisUserDao userDao;

  private Integer id;
  private String name;

  private ConfigurationProfile profile;
  private Map<String, ConfigurationItemKey> filteredItemKeys;
  private Map<String, ConfigurationItemCategory> allCategories;

  private Map<String, ConfigurationProfileItem> profileItems;
  private Map<String, ConfigurationProfileItem> deletedProfileItems;
  private int newItemID = -1;

  private String selectedCategory;
  private String selectedItemKey;
  private String profileItemValue;


  public void init() throws Exception {
    LOG.debug("init()");
    try {
      profile = profileDao.getProfile(id);
    } catch (IllegalArgumentException exp) {
      FacesMessagesUtils.addErrorMessage("Profile id " + id + " is not valid", null);
    }

    if (profile == null) {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, null);
      return;
    }

    selectedCategory = NONE_SELECTOR;
    selectedItemKey = NONE_SELECTOR;
    try {
      allCategories = categoryDao.getCategoryMap();
    } catch (IllegalArgumentException exc) {
      FacesMessagesUtils.addErrorMessage("Cannot select categories from database", null);
    }

    this.name = profile.getName();
    this.selectedCategory = NONE_SELECTOR;
    this.selectedItemKey = NONE_SELECTOR;
    this.allCategories = categoryDao.getCategoryMap();

    refreshItemKeys();
    refreshItems();
  }


  private void refreshItemKeys() {
    LOG.debug("refreshItemKeys()");
    try {
      if (NONE_SELECTOR.equals(selectedCategory)) {
        filteredItemKeys = Collections.emptyMap();
        return;
      }
      if (!allCategories.containsKey(selectedCategory)) {
        FacesMessagesUtils.addErrorMessage("form:category", "Vybraná kategorie neexistuje", null);
        filteredItemKeys = Collections.emptyMap();
        return;
      }

      ConfigurationItemCategory filter = allCategories.get(selectedCategory);
      List<ConfigurationItemKey> itemKeys = itemKeyDao.filterItemKeys(filter);

      filteredItemKeys = ConfigurationItemKeyDao.getItemKeyMap(itemKeys);

      // for (ConfigurationProfileItem item : profileItems.values()) {
      // filteredItemKeys.remove(item.getKey().getId().toString());
      // }

    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo obnovit položky klíče", FacesUtils.getRootMessage(exc));
    }

    if (!allCategories.containsKey(selectedCategory)) {
      FacesMessagesUtils.addErrorMessage("form:category", "Vybraná kategorie neexistuje", null);
      filteredItemKeys = Collections.emptyMap();
      return;
    }

    ConfigurationItemCategory filter = allCategories.get(selectedCategory);
    List<ConfigurationItemKey> itemKeys = itemKeyDao.filterItemKeys(filter);

    filteredItemKeys = ConfigurationItemKeyDao.getItemKeyMap(itemKeys);

    // for (ConfigurationProfileItem item : profileItems.values()) {
    // filteredItemKeys.remove(item.getKey().getId().toString());
    // }

  }


  private void refreshItems() {
    LOG.debug("refreshItems()");
    try {
      List<ConfigurationProfileItem> items = itemDao.listItems();
      profileItems = ConfigurationProfileItemDao.getItemMap(items);
    } catch (Exception exc) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo obnovit položky", FacesUtils.getRootMessage(exc));
    }
    deletedProfileItems = new HashMap<>();
  }


  public void actionAddItem(ActionEvent event) {
    LOG.debug("actionAddItem(event={})", event);
    if (NONE_SELECTOR.equals(selectedCategory) || selectedCategory == null) {
      FacesMessagesUtils.addErrorMessage("form:category", "Nebyla zvolena kategorie", null);
      return;
    }

    ConfigurationItemKey key = filteredItemKeys.get(selectedItemKey);

    if (key == null || NONE_SELECTOR.equals(selectedItemKey) || selectedItemKey == null) {
      FacesMessagesUtils.addErrorMessage("form:key", "Nebyl zvolen klíč", null);
      return;
    }

    String value = profileItemValue;
    if (value == null || value.isEmpty()) {
      FacesMessagesUtils.addErrorMessage("form:value", "Nebyla vyplněna hodnota klíče", null);
      return;
    }

    final Integer itemId = newItemID--;
    ConfigurationProfileItem item = new ConfigurationProfileItem();
    item.setId(itemId);
    item.setKey(key);
    item.setProfile(profile);
    item.setValue(profileItemValue);

    profileItems.put(item.getId().toString(), item);
  }


  public void actionDeleteItem() {
    LOG.debug("actionDeleteItem()");
    final String itemIdStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
        .get("itemId");
    LOG.debug("itemIdStr={}, class={}", itemIdStr, itemIdStr.getClass());
    final Integer itemId = Integer.valueOf(itemIdStr);
    if (isDeletedItem(itemId)) {
      return;
    }
    final ConfigurationProfileItem deleteItem = profileItems.get(itemIdStr);
    if (isNewItem(itemId)) {
      // new items delete from cache right away
      profileItems.remove(itemIdStr);
    } else {
      // existing items mark for deletion
      deletedProfileItems.put(deleteItem.getId().toString(), deleteItem);
    }
  }


  public void actionRestoreItem() {
    LOG.debug("actionRestoreItem()");
    final String itemIdStr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
        .get("itemId");
    LOG.debug("itemIdStr={}, class={}", itemIdStr, itemIdStr.getClass());
    deletedProfileItems.remove(itemIdStr);
  }


  public void actionSaveChanges() {
    LOG.debug("actionSaveChanges()");
    try {
      // TODO in one transaction?
      for (ConfigurationProfileItem item : profileItems.values()) {
        Integer id = item.getId();

        // delete
        if (isDeletedItem(id)) {
          // only existing items are in deleted list
          itemDao.removeItem(item);
          profileItems.remove(id);
          deletedProfileItems.remove(id);
        }
        // insert
        else if (isNewItem(id)) {
          itemDao.addItem(item);
          // assure to have item under new ID in map
          profileItems.remove(id);
          profileItems.put(item.getId().toString(), item);
        }
        // update
        else {
          itemDao.updateItem(item);
        }
      }

      newItemID = -1;
    } catch (UniqueProfileKeyException e) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny: " + FacesUtils.getRootMessage(e), null);
    }
  }


  public boolean isDeletedItem(Integer id) {
    LOG.debug("isDeletedItem(id={})", id);
    return deletedProfileItems.containsKey(id.toString());
  }


  public boolean isNewItem(Integer id) {
    LOG.debug("isNewItem(id={})", id);
    return id.intValue() < 0;
  }


  public String getProfileItemValue() {
    LOG.trace("getProfileItemValue()");
    return profileItemValue;
  }


  public void setProfileItemValue(ValueChangeEvent event) {
    LOG.debug("setProfileItemValue(event={})", event);
    setProfileItemValue((String) event.getNewValue());
  }


  public void setProfileItemValue(String profileItemValue) {
    LOG.debug("setProfileItemValue(profileItemValue={})", profileItemValue);
    this.profileItemValue = profileItemValue;
    if (profileItemValue == null) {
      this.profileItemValue = "";
    }
  }


  public boolean isKeySelectorDisabled() {
    LOG.trace("isKeySelectorDisabled()");
    return NONE_SELECTOR.equals(selectedCategory);
  }


  public boolean isKeyValueDisabled() {
    LOG.trace("isKeyValueDisabled()");
    return NONE_SELECTOR.equals(selectedItemKey);
  }


  public String getNoneSelector() {
    LOG.trace("getNoneSelector()");
    return NONE_SELECTOR;
  }


  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }


  public void setSelectedCategory(ValueChangeEvent event) {
    LOG.debug("setSelectedCategory(event={})", event);
    setSelectedCategory((String) event.getNewValue());
  }


  public void setSelectedCategory(String selectedCategory) {
    LOG.debug("setSelectedCategory(selectedCategory={})", selectedCategory);
    this.selectedCategory = selectedCategory;
    if (selectedCategory == null || selectedCategory.isEmpty()) {
      this.selectedCategory = NONE_SELECTOR;
    }
    if (this.selectedCategory == NONE_SELECTOR) {
      setSelectedItemKey(NONE_SELECTOR);
      refreshItemKeys();
    }
  }


  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.trace("getAllCategories()");
    return allCategories.values();
  }


  public Collection<ConfigurationProfileItem> getProfileItems() {
    LOG.trace("getProfileItems()");
    return profileItems.values();
  }


  public Collection<ConfigurationItemKey> getFilteredItemKeys() {
    LOG.trace("getFilteredItemKeys()");
    refreshItemKeys();
    return filteredItemKeys.values();
  }


  public String getSelectedItemKey() {
    LOG.trace("getSelectedItemKey()");
    return selectedItemKey;
  }


  public void setSelectedItemKey(ValueChangeEvent event) {
    LOG.debug("setSelectedItemKey(event={})", event);
    setSelectedItemKey((String) event.getNewValue());
  }


  public void setSelectedItemKey(String selectedItemKey) {
    LOG.debug("setSelectedItemKey(selectedItemKey={})", selectedItemKey);
    this.selectedItemKey = selectedItemKey;
    if (selectedItemKey == null || selectedItemKey.isEmpty()) {
      this.selectedItemKey = NONE_SELECTOR;
    }
    if (this.selectedItemKey == NONE_SELECTOR) {
      setProfileItemValue("");
    }
  }


  public boolean refreshAddItemKeyForm() {
    LOG.debug("refreshAddItemKeyForm()");
    if (NONE_SELECTOR.equals(selectedCategory)) {
      this.selectedItemKey = NONE_SELECTOR;
    }
    return true;
  }


  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }


  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }


  public String getName() {
    LOG.trace("getName()");
    return name;
  }


  public void setName(String name) {
    LOG.debug("setName(name={})", name);
    this.name = name;
  }
}
