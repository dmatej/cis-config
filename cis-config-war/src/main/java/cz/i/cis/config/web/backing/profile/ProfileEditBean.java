package cz.i.cis.config.web.backing.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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
  private String manipulationID;

  private String selectedCategory;
  private String selectedItemKey;
  private String profileItemValue;


  public void init() throws Exception {
    profile = profileDao.getProfile(id);

    if (profile == null) {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, null);
    }
    else {
      this.name = profile.getName();
    }

    selectedCategory = NONE_SELECTOR;
    selectedItemKey = NONE_SELECTOR;
    allCategories = categoryDao.getCategoryMap();
    refreshItemKeys();
    refreshItems();
  }

  private void refreshItemKeys() throws Exception {
    if (NONE_SELECTOR.equals(selectedCategory)) {
      filteredItemKeys = Collections.emptyMap();
    }
    else {
      if (!allCategories.containsKey(selectedCategory)) {
        throw new Exception("Selected category is not valid.");
      }

      ConfigurationItemCategory filter = allCategories.get(selectedCategory);
      List<ConfigurationItemKey> itemKeys = itemKeyDao.filterItemKeys(filter);

      filteredItemKeys = ConfigurationItemKeyDao.getItemKeyMap(itemKeys);
    }
  }

  private void refreshItems(){
    List<ConfigurationProfileItem> items= itemDao.listItems();
    profileItems = ConfigurationProfileItemDao.getItemMap(items);

    deletedProfileItems = new HashMap<>();
  }

  public String actionAddProfileItem() {
    Integer id = newItemID--;
    ConfigurationItemKey key = filteredItemKeys.get(selectedItemKey);
    String value = profileItemValue;


    if(key != null && value != null && !value.isEmpty()){
      ConfigurationProfileItem item = new ConfigurationProfileItem();
        item.setId(id);
        item.setKey(key);
        item.setProfile(profile);
        item.setValue(profileItemValue);

      profileItems.put(item.getId().toString(), item);
    }
    return null;
  }

  public String actionDeleteItem(){
    ConfigurationProfileItem deleteItem = profileItems.get(manipulationID);
    Integer id = deleteItem.getId();

    if(isDeletedItem(id)) return null; //nothing new to delete

    if(isNewItem(id)){
      //new items delete from cache right away
      profileItems.remove(id.toString());
    }
    else{
      //existing items mark for deletion
      deletedProfileItems.put(deleteItem.getId().toString(), deleteItem);
    }

    return null;
  }

  public String actionRestoreItem(){
    deletedProfileItems.remove(manipulationID);
    return null;
  }

  public String actionSaveChanges(){
    try {
      //TODO in one transaction?
      for (ConfigurationProfileItem item : profileItems.values()) {
        Integer id = item.getId();

        //delete
        if(isDeletedItem(id)){
// if(!isNewItem(id)){ //only existing items are in deleted list
            itemDao.removeItem(item);
// }
          profileItems.remove(id);
          deletedProfileItems.remove(id);
        }
        //insert
        else if(isNewItem(id)){
          itemDao.addItem(item);
          //assure to have item under new ID in map
          profileItems.remove(id);
          profileItems.put(item.getId().toString(), item);
        }
        //update
        else{
          itemDao.updateItem(item);
        }
      }


      newItemID = -1;
    }
    catch (UniqueProfileKeyException e) {
      FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny: " + FacesUtils.getRootMessage(e), null);
    }
    return null;
  }

  public boolean isDeletedItem(Integer id){
    return deletedProfileItems.containsKey(id.toString());
  }

  public boolean isNewItem(Integer id){
    return id.intValue() < 0;
  }





  public String getProfileItemValue() {
    return profileItemValue;
  }

  public void setProfileItemValue(String profileItemValue) {
    this.profileItemValue = profileItemValue;
  }

  public boolean isKeySelectorDisabled() {
    return NONE_SELECTOR.equals(selectedCategory);
  }

  public boolean isKeyValueDisabled() {
    return NONE_SELECTOR.equals(selectedItemKey);
  }

  public String getNoneSelector() {
    return NONE_SELECTOR;
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

  public Collection<ConfigurationProfileItem> getProfileItems() {
    return profileItems.values();
  }

  public Collection<ConfigurationItemKey> getFilteredItemKeys() throws Exception {
    refreshItemKeys();

    return filteredItemKeys.values();
  }

  public String getSelectedItemKey() {
    return selectedItemKey;
  }

  public void setSelectedItemKey(String selectedItemKey) {
    this.selectedItemKey = selectedItemKey;
  }

  public void setManipulationID(String manipulationID) {
    this.manipulationID = manipulationID;
  }

  public boolean refreshAddItemKeyForm() {
    if (NONE_SELECTOR.equals(selectedCategory))
      selectedItemKey = NONE_SELECTOR;
    return true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}