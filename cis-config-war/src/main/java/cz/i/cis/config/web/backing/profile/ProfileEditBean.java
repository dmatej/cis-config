package cz.i.cis.config.web.backing.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileItemDao;
import cz.i.cis.config.jpa.CisUser;
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
  private static final String ALL_SELECTOR = "all";

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

  private ConfigurationProfile profile;
  @SuppressWarnings("unused")
  private List<ConfigurationProfileItem> profileItems;
  private Map<String, ConfigurationItemKey> filteredItemKeys;
  private Map<String, ConfigurationItemCategory> allCategories;
  // TODO seznam položek, přidávání, odebírání, uložení

  // profile metadata
  private String name;
  private String description;

  private String selectedCategory;
  private String selectedItemKey;


  public void init() throws Exception {
    profile = profileDao.getProfile(id);

    if (profile != null) {
      name = profile.getName();
      description = profile.getDescription();
    }
    else {
      FacesMessagesUtils.addErrorMessage("Zvolený profil nebyl nalezen v databázi - ID = " + id, null);
    }

    selectedCategory = NONE_SELECTOR;
    selectedItemKey = NONE_SELECTOR;
    allCategories = categoryDao.getCategoryMap();
    refreshItemKeys();
  }


  public String actionUpdateProfileMetadata() {
    if (profile != null) { // TODO to ukládání je i u vytváření nového profilu, nejlíp sjednotit
      try {
        String login = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        if (login == null || login.isEmpty()) {
          throw new Exception(
              "Somehow no user is not logged in and phantoms are not allowed to create configuration profiles.");
        }
        CisUser editor = userDao.getUser(login);
        if (editor == null) {
          throw new Exception("Logged in user has not been found in the database.");
        }

        profile.setName(name);
        profile.setDescription(description);
        profile.setUser(editor);
        profile.setUpdate(new Date());

        profile = profileDao.updateProfile(profile);
        FacesMessagesUtils.addInfoMessage("Změny byly uloženy.", null);
        // FacesUtils.redirect("list.xhtml#profile-" + profile.getId());
      }
      catch (Exception e) {
        FacesMessagesUtils.addErrorMessage("Nepodařilo se uložit změny", FacesUtils.getRootMessage(e));
      }
    }
    else {
      FacesMessagesUtils.addErrorMessage("Musíte editovat existující profil, abyste mohli uložit jeho změny", null);
    }
    return null; // stay on the same page to display the messages
  }


  public String actionAddProfileItem() {
    // TODO
    return null;
  }


  private void refreshItemKeys() throws Exception {
    if (NONE_SELECTOR.equals(selectedCategory)) {
      filteredItemKeys = Collections.emptyMap();
    }
    else if (ALL_SELECTOR.equals(selectedCategory)) {
      filteredItemKeys = ConfigurationItemKeyDao.getItemKeyMap(itemKeyDao.listItemKeys());
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


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public boolean isKeySelectorDisabled() {
    return NONE_SELECTOR.equals(selectedCategory);
  }


  public boolean isKeyValueDisabled() {
    return NONE_SELECTOR.equals(selectedItemKey);
  }


  public String getAllSelector() {
    return ALL_SELECTOR;
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


  public boolean refreshAddItemKeyForm() {
    if (NONE_SELECTOR.equals(selectedCategory))
      selectedItemKey = NONE_SELECTOR;
    return true;
  }
}
