package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileItemDao;
import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;
import cz.i.cis.config.jpa.Type;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationProfileItemDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileItemDaoITest.class);
  private ConfigurationItemKey key;
  private ConfigurationItemCategory category;
  private ConfigurationProfile profile;
  private CisUser user;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileItemDao")
  private ConfigurationProfileItemDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao dao_key;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao_category;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileDao")
  private ConfigurationProfileDao dao_profile;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao dao_user;


  @Before
  public void createEnvirenments() throws UserAlreadyExistsException {
    LOG.debug("DEBUG ConfigurationProfileItemDao");
    category = new ConfigurationItemCategory();
    category.setName("base");
    dao_category.addCategory(category);
    LOG.debug("category: {}", category);

    key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("some key");
    key.setKey("base");
    key.setType(Type.Text);
    dao_key.addItemKey(key);
    LOG.debug("created key: {}", key);

    user = new CisUser();
    user.setFirstName("andrej");
    user.setLastName("minin");
    user.setLogin("pirat");
    Date birthday = new Date();
    user.setBirthDate(birthday);
    dao_user.addUser(user);
    LOG.debug("created user: {}", user);

    profile = new ConfigurationProfile();
    profile.setName("base configurat");
    profile.setDescription("domain configuration");
    Date current_date = new Date();
    profile.setUpdate(current_date);
    profile.setUser(user);
    dao_profile.addProfile(profile);
    LOG.debug("created profile: {}", profile);
  }


  @After
  public void removeEnvirenments() {
    List<ConfigurationProfileItem> profiles = dao.listItems();
    dao.removeItem(profiles.get(0));
    List<ConfigurationProfileItem> list_profiles = dao.listItems();
    assertTrue("profiles.isEmpty", list_profiles.isEmpty());
    LOG.debug("list_profiles: {}", list_profiles);
  }


  @Test
  public void createConfigurationProfileItem() {
    ConfigurationProfileItem profile_item = new ConfigurationProfileItem();
    profile_item.setKey(key);
    profile_item.setProfile(profile);
    profile_item.setValue("key-profile");
    dao.addItem(profile_item);
    LOG.debug("profile_item: {}", profile_item);
    profile_item.setValue("profile-key");
    dao.updateItem(profile_item);
    LOG.debug("update profile_item: {}", profile_item);
    assertNotNull("profile_item.id", profile_item.getId());

    List<ConfigurationProfileItem> profiles = dao.listItems();
    LOG.debug("profiles: {}", profiles);
    assertNotNull("profiles", profiles);
    assertEquals("profiles.size", 1, profiles.size());
    ConfigurationProfileItem profile = profiles.get(0);
    assertNotNull("profile", profile);
    assertEquals("profiles.hashCode", profile.hashCode(), profile_item.hashCode());
    assertEquals("profile is not same os original", profile, profile_item);
    assertTrue("profile.equals", profile.equals(profiles.get(0)));
  }
}
