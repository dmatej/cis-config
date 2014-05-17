package cz.i.cis.config.web.backing.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


/**
 * Backing bean for profile items manipulation.
 */
@Named(value = "profileEdit")
@ViewScoped
public class ProfileEditBean {

  /**Logger object used for logging.*/
  private static final Logger LOG = LoggerFactory.getLogger(ProfileEditBean.class);
  /**Selection placeholder for "no selection".*/
  private static final String NONE_SELECTOR = "none";

  @EJB
  /**Data access object for profile manipulation.*/
  private ConfigurationProfileDao profileDao;
  @EJB
  /**Data access object for profile item manipulation.*/
  private ConfigurationProfileItemDao itemDao;
  @EJB
  /**Data access object for item category manipulation.*/
  private ConfigurationCategoryDao categoryDao;
  @EJB
  /**Data access object for item key manipulation.*/
  private ConfigurationItemKeyDao itemKeyDao;

  /**ID of currently edited profile. It is set via request parameter.*/
  private Integer id;
  /**Currently edited profile. It is initialized in init() method with use of id field.*/
  private ConfigurationProfile profile;
  /**Collection of item keys filtered by selected category.*/
  private Map<String, ConfigurationItemKey> filteredItemKeys;
  /**Collection of available item categories.*/
  private Map<String, ConfigurationItemCategory> allCategories;

  /**Collection of items in current profile.*/
  private Map<String, ConfigurationProfileItem> profileItems;
  /**
   * ID counter for newly added items. Each item must have an ID so it could be stored in item map.
   * New items have negative ID to not collide with existing items and for their easy determination.
   */
  private int newItemID = -1;

  /**Category selected on page.*/
  private String selectedCategory;
  /**Item key selected on page for new or edited item.*/
  private String selectedItemKey;
  /**Item value entered on page for new or edited item.*/
  private String profileItemValue;

  /**Currently edited item when in editing mode.*/
  private ConfigurationProfileItem editItem;


  /**
   * Initializes backing bean before user manipulation.
   * Loads profile, its items and prepares select boxes.
   */
  public void init() {
    LOG.debug("init()");
    //load profile
    try {
      profile = profileDao.getProfile(id);
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to load profile: ID = " + id, e);
      FacesMessagesUtils.addErrorMessage("Profil není validní - ID = " + id, e);
    }

    if (profile == null) {
      LOG.error("Profile not loaded while initializing, redirecting to list: ID = {}", id);
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, "");
      FacesUtils.redirectToOutcome("list");
      return;
    }

    //prepare select boxes
    try {
      this.selectedCategory = NONE_SELECTOR;
      this.selectedItemKey = NONE_SELECTOR;
      this.allCategories = categoryDao.getCategoryMap();
    } catch (IllegalArgumentException e) {
      LOG.error("Failed to load categories.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo se načíst kategorie z databáze.", e);
    }
    refreshItemKeys();
    refreshItems();
  }


  /**
   * Refresh item keys filtered by selected category.
   */
  private void refreshItemKeys() {
    LOG.debug("refreshItemKeys()");
    try {
      if (NONE_SELECTOR.equals(selectedCategory)) {
        filteredItemKeys = Collections.emptyMap();
        return;
      }
      if (!allCategories.containsKey(selectedCategory)) {
        LOG.warn("Unknown category requested: {}", selectedCategory);
        FacesMessagesUtils.addErrorMessage("form:category", "Vybraná kategorie neexistuje", null);
        filteredItemKeys = Collections.emptyMap();
        return;
      }

      ConfigurationItemCategory filter = allCategories.get(selectedCategory);
      List<ConfigurationItemKey> itemKeys = itemKeyDao.filterItemKeys(filter);

      filteredItemKeys = ConfigurationItemKeyDao.getItemKeyMap(itemKeys);

      //don't offer item keys already used in profile
      for (ConfigurationProfileItem item : profileItems.values()) {
        if(item == editItem) {
          continue;
        }

        filteredItemKeys.remove(item.getKey().getId() + "");
      }
    } catch (Exception e) {
      LOG.error("Failed to refresh item keys.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo obnovit klíče položek", FacesMessagesUtils.getRootMessage(e));
    }
  }

  /**
   * Reloads items of current profile.
   */
  private void refreshItems() {
    LOG.debug("refreshItems()");
    try {
      List<ConfigurationProfileItem> items = itemDao.listItems(profile.getId());
      profileItems = ConfigurationProfileItemDao.getItemMap(items);
    } catch (Exception e) {
      LOG.error("Failed to retrieve items.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo obnovit položky", FacesMessagesUtils.getRootMessage(e));
    }
  }

  /**
   * Validates current item. This is either new item or currently edited item.
   * @return Validated item values or null if validation fails.
   */
  protected ProfileItemValues validateCurrentItem() {
    if (NONE_SELECTOR.equals(selectedCategory) || selectedCategory == null) {
      FacesMessagesUtils.addErrorMessage("form:category", "Není zvolena kategorie", null);
      return null;
    }

    ConfigurationItemKey key = filteredItemKeys.get(selectedItemKey);

    if (key == null || NONE_SELECTOR.equals(selectedItemKey) || selectedItemKey == null) {
      FacesMessagesUtils.addErrorMessage("form:key", "Není zvolen klíč", null);
      return null;
    }

    String value = profileItemValue;
    if (value == null || value.isEmpty()) {
      FacesMessagesUtils.addErrorMessage("form:value", "Není vyplněna hodnota klíče", null);
      return null;
    }

    return new ProfileItemValues(key, value);
  }

  /**
   * Adds new item with currently filled values to current profile.
   * @param event Description of this event.
   */
  public void actionAddItem(ActionEvent event) {
    LOG.debug("actionAddItem(event={})", event);

    ProfileItemValues values = this.validateCurrentItem();
    if(values == null) {
      return;
    }

    final Integer itemId = newItemID--;
    ConfigurationProfileItem item = new ConfigurationProfileItem();
      item.setId(itemId);
      item.setProfile(profile);
      item.setKey(values.getKey());
      item.setValue(values.getValue());

    profileItems.put(itemId.toString(), item);

    this.setSelectedItemKey(NONE_SELECTOR);
  }

  /**
   * Sets entered values to edited item.
   * @param event Description of this event.
   */
  public void actionEditItem(ActionEvent event) {
    LOG.debug("actionEditItem(event={})", event);

    if(this.editItem == null) {
      return;
    }

    ProfileItemValues values = this.validateCurrentItem();
    if(values == null) {
      return;
    }

    this.editItem.setKey(values.getKey());
    this.editItem.setValue(values.getValue());
    this.editItem = null;

    this.setSelectedItemKey(NONE_SELECTOR);
  }

  /**
   * Cancels item editing.
   * @param event Description of this event.
   */
  public void actionStornoEditItem(ActionEvent event) {
    LOG.debug("actionStornoEditItem(event={})", event);

    editItem = null;
    this.setSelectedCategory(NONE_SELECTOR);
  }

  /**
   * Set selected item for editing. Fills form with item values.
   * @param itemId Selected item ID.
   */
  public void actionSetEditItem(String itemId) {
    LOG.debug("actionDeleteItem(itemId={})", itemId);

    editItem = profileItems.get(itemId);
    if(editItem == null) {
      LOG.warn("Requested item not found: ID = {}", itemId);
    } else{
      ConfigurationItemKey key = editItem.getKey();
      ConfigurationItemCategory category = key.getCategory();

      this.setSelectedCategory(category.getId().toString());
      this.setSelectedItemKey(key.getId().toString());
      this.setProfileItemValue(editItem.getValue());
    }
  }

  /**
   * Deletes selected item. If item has not been persisted yet, it is removed right away.
   * If item has been persisted before, it is marked as deleted (allowing to cancel deletion)
   * and deleted when changes are saved.
   * @param itemId Selected item ID.
   */
  public void actionDeleteItem(String itemId) {
    LOG.debug("actionDeleteItem(itemId={})", itemId);

    Integer itemIdInt;
    try{
      itemIdInt = Integer.valueOf(itemId);
    }
    catch(NumberFormatException e){
      LOG.warn("Requested ID is not valid: ID = {}", itemId);
      return;
    }

    if (isDeletedItem(itemIdInt)) {
      LOG.debug("Item already marked for deletion: ID = {}", itemId);
      return;
    }

    final ConfigurationProfileItem deleteItem = profileItems.get(itemId);
    if(deleteItem == null){
      LOG.warn("Requested item not found: ID = {}", itemId);
    }else{
      if (isNewItem(itemIdInt)) {
        // new items delete from cache right away
        profileItems.remove(itemId);
        LOG.debug("New item removed: {}", deleteItem);
      } else {
        // existing items mark for deletion
        deleteItem.setDeleted(true);
        LOG.debug("Existing item marked for deletion: {}", deleteItem);
      }
    }
  }

  /**
   * Cancels deletion of item marked for deletion.
   * @param itemId Selected item.
   */
  public void actionRestoreItem(String itemId) {
    LOG.debug("actionRestoreItem(itemId={})", itemId);

    final ConfigurationProfileItem restoreItem = profileItems.get(itemId.toString());
    if(restoreItem == null) {
      LOG.warn("Requested item not found: ID = {}", itemId);
    }else{
      restoreItem.setDeleted(false);
    }
  }

  /**
   * Persists changes made in profile. Adds new items, deletes persisted items marked for deletion
   * and merges existing items.
   * @param event Description of this event.
   */
  public void actionSaveChanges(ActionEvent event) {
    LOG.debug("actionSaveChanges(event={})", event);
    try {
      List<ConfigurationProfileItem> updatedItems = itemDao.saveChanges(profileItems);
      profileItems = ConfigurationProfileItemDao.getItemMap(updatedItems);

      newItemID = -1;
    } catch (UniqueProfileKeyException e) {
      LOG.error("Failed to save changes.", e);
      FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny: " + FacesMessagesUtils.getRootMessage(e), "");
    }
  }


  /**
   * Tests if item with this ID is marked for deletion.
   * @param id ID of item to test.
   * @return If item is marked for deletion. False if id is null.
   */
  public boolean isDeletedItem(Integer id) {
    LOG.debug("isDeletedItem(id={})", id);
    ConfigurationProfileItem item = profileItems.get(String.valueOf(id));
    if(item == null) {
      LOG.warn("Requested item not found: ID = {}", id);
      return false;
    }

    return item.isDeleted();
  }

  /**
   * Tests if item with this ID is a new item.
   * @param id ID of item to test.
   * @return If item has not been persisted yet. False if id is null.
   */
  public boolean isNewItem(Integer id) {
    LOG.debug("isNewItem(id={})", id);

    if(id == null){
      LOG.warn("Request to test null ID.");
      return false;
    }

    return id.intValue() < 0;
  }

  /**
   * Tests editing state.
   * @return If some item is being edited.
   */
  public boolean isEditingItem() {
    return (editItem != null);
  }

  /**
   * Tests editing state of given item.
   * @param id ID of item to test.
   * @return If item with given ID is being edited.
   */
  public boolean isEditingItem(Integer id) {
    LOG.debug("isEditingItem(id={})", id);
    if(editItem == null) {
      return false;
    }

    return (editItem.getId() == id);
  }

  /**
   * Returns value of new or edited item.
   * @return Value of new or edited item.
   */
  public String getProfileItemValue() {
    LOG.trace("getProfileItemValue()");
    return profileItemValue;
  }

  /**
   * Sets value of new or edited item.
   * @param event Description of this event.
   */
  public void setProfileItemValue(ValueChangeEvent event) {
    LOG.debug("setProfileItemValue(event={})", event);
    setProfileItemValue((String) event.getNewValue());
  }

  /**
   * Sets value of new or edited item.
   * @param profileItemValue New value for item.
   */
  public void setProfileItemValue(String profileItemValue) {
    LOG.debug("setProfileItemValue(profileItemValue={})", profileItemValue);
    this.profileItemValue = (profileItemValue != null) ? profileItemValue : "";
  }

  /**
   * Tests if key selection should be disabled.
   * @return If key selection should be disabled.
   */
  public boolean isKeySelectorDisabled() {
    LOG.trace("isKeySelectorDisabled()");
    return NONE_SELECTOR.equals(selectedCategory);
  }

  /**
   * Tests if item value input should be disabled.
   * @return If item value input should be disabled.
   */
  public boolean isItemValueDisabled() {
    LOG.trace("isKeyValueDisabled()");
    return NONE_SELECTOR.equals(selectedItemKey) || !this.filteredItemKeys.containsKey(selectedItemKey);
  }

  /**
   * Returns representation for "no selection".
   * @return Representation for "no selection".
   */
  public String getNoneSelector() {
    LOG.trace("getNoneSelector()");
    return NONE_SELECTOR;
  }

  /**
   * Returns selected category.
   * @return Selected category.
   */
  public String getSelectedCategory() {
    LOG.trace("getSelectedCategory()");
    return selectedCategory;
  }

  /**
   * Sets new selected category.
   * @param event Description of this event.
   */
  public void setSelectedCategory(ValueChangeEvent event) {
    LOG.debug("setSelectedCategory(event={})", event);
    setSelectedCategory((String) event.getNewValue());
  }

  /**
   * Sets new selected category.
   * @param selectedCategory ID of category or NONE_SELECTOR.
   */
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

  /**
   * Returns collection of all item categories.
   * @return Collection of all item categories.
   */
  public Collection<ConfigurationItemCategory> getAllCategories() {
    LOG.trace("getAllCategories()");
    return allCategories.values();
  }

  /**
   * Returns collection of profile items.
   * @return Collection of profile items.
   */
  public Collection<ConfigurationProfileItem> getProfileItems() {
    LOG.trace("getProfileItems()");
    return profileItems.values();
  }

  /**
   * Returns collection of available item keys.
   * @return Collection of available item keys.
   */
  public Collection<ConfigurationItemKey> getFilteredItemKeys() {
    LOG.trace("getFilteredItemKeys()");
    refreshItemKeys();

    return filteredItemKeys.values();
  }

  /**
   * Returns selected item key.
   * @return Selected item key.
   */
  public String getSelectedItemKey() {
    LOG.trace("getSelectedItemKey()");
    return selectedItemKey;
  }

  /**
   * Sets new selected item key.
   * @param event Description of this event.
   */
  public void setSelectedItemKey(ValueChangeEvent event) {
    LOG.debug("setSelectedItemKey(event={})", event);
    setSelectedItemKey((String) event.getNewValue());
  }

  /**
   * Sets new selected item key.
   * @param selectedItemKey New selected item key or NONE_SELECTOR.
   */
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

  /**
   * Returns current profile ID.
   * @return Current profile ID.
   */
  public Integer getId() {
    LOG.trace("getId()");
    return id;
  }

  /**
   * Sets current profile ID.
   * @param id Current profile ID.
   */
  public void setId(Integer id) {
    LOG.debug("setId(id={})", id);
    this.id = id;
  }

  /**
   * Returns current profile name.
   * @return Current profile name.
   */
  public String getProfileName() {
    LOG.trace("getProfileName()");
    return profile.getName();
  }



  /**
   * Helper java bean for item values.
   */
  private static class ProfileItemValues {
    /**Item key.*/
    private ConfigurationItemKey key;
    /**Item value.*/
    private String value;

    /**
     * Prepares new instance.
     * @param key Item key.
     * @param value Item value.
     */
    public ProfileItemValues(ConfigurationItemKey key, String value) {
      this.key = key;
      this.value = value;
    }

    /**
     * Returns item key.
     * @return Item key.
     */
    public ConfigurationItemKey getKey() {
      return key;
    }

    /**
     * Returns item value.
     * @return Item value.
     */
    public String getValue() {
      return value;
    }
  }
}
