package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileDao;
import cz.i.cis.config.ejb.dao.ConfigurationProfileItemDao;
import cz.i.cis.config.ejb.dao.exceptions.CisUserDaoException;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemKeyTestHelper;
import cz.i.cis.config.helpers.ConfigurationProfileItemTestHelper;
import cz.i.cis.config.helpers.ConfigurationProfileTestHelper;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;
import cz.i.cis.config.jpa.ConfigurationProfile;
import cz.i.cis.config.jpa.ConfigurationProfileItem;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationProfileItemDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProfileItemDaoITest.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileItemDao")
  private ConfigurationProfileItemDao profileItemDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao configItemkeyDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationItemCategoryDao categoryDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileDao")
  private ConfigurationProfileDao profileDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao userDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileItemTestHelper")
  private ConfigurationProfileItemTestHelper profileItemHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper configItemKeyHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper categoryHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileTestHelper")
  private ConfigurationProfileTestHelper profileHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper userHelper;


  @Before
  public void init() {
  }


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    profileItemHelper.cleanup();
    userHelper.cleanup();
    profileHelper.cleanup();
    categoryHelper.cleanup();
    configItemKeyHelper.cleanup();
  }


  @Test
  public void creatNewConfigurationProfileItem() throws CisUserDaoException {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category name");
    categoryDao.addCategory(category);
    categoryHelper.addToDelete(category);

    final ConfigurationItemKey key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("base configuration key");
    key.setKey("base");
    key.setType(ConfigurationItemKeyType.Text);
    configItemkeyDao.addItemKey(key);
    configItemKeyHelper.addToDelete(key);

    final CisUser user = new CisUser();
    user.setLastName("Jezek");
    user.setLogin("log");
    user.setFirstName("Karl");
    user.setBirthDate(new Date());
    userDao.addUser(user);
    userHelper.addToDelete(user);

    final ConfigurationProfile profile = new ConfigurationProfile();
    profile.setName("base configuration");
    profile.setDescription("domain configuration");
    profile.setUpdate(new Date());
    profile.setUser(user);
    profileDao.addProfile(profile);
    profileHelper.addToDelete(profile);

    final ConfigurationProfileItem profileItem = new ConfigurationProfileItem();
    profileItem.setKey(key);
    profileItem.setProfile(profile);
    profileItem.setValue("key-profile");
    profileItemDao.addItem(profileItem);
    profileItemHelper.addToDelete(profileItem);
    LOG.debug("profileItem: {}", profileItem);
    assertNotNull("profileItem.id", profileItem.getId());

    profileItem.setValue("profile-key");
    profileItemDao.updateItem(profileItem);
    assertTrue(profileItemDao.listItems().get(0).getValue().equals("profile-key"));
  }


  @Test(expected = PersistenceException.class)
  public void creatAlreadyExistingConfigurationProfileItem() throws PersistenceException {
    final ConfigurationProfileItem profileItem = profileItemHelper.createConfigurationProfileItem();
    final ConfigurationProfileItem profileItemCopy = new ConfigurationProfileItem();
    profileItemCopy.setProfile(profileItem.getProfile());
    profileItemCopy.setKey(profileItem.getKey());
    profileItemCopy.setValue(profileItem.getValue());
    profileItemDao.addItem(profileItemCopy);
  }


  @Test
  public void listConfigurationProfileItems() {
    final List<ConfigurationProfileItem> items = profileItemDao.listItems();
    LOG.debug("list configuration profile items: {}", items);
    assertNotNull("configuration profile items", items);
    assertTrue("items.empty", items.isEmpty());
  }


  @Test
  public void testComparationConfigurationProfileItems() {
    final ConfigurationItemKey key = new ConfigurationItemKey();
    final ConfigurationProfile profile = new ConfigurationProfile();

    final ConfigurationProfileItem itemFirst = new ConfigurationProfileItem();
    itemFirst.setId(1);
    itemFirst.setKey(key);
    itemFirst.setProfile(profile);
    itemFirst.setValue("key-profile");

    final ConfigurationProfileItem itemSecond = new ConfigurationProfileItem();
    itemSecond.setId(1);
    itemSecond.setKey(key);
    itemSecond.setProfile(profile);
    itemSecond.setValue("key-profile");

    assertEquals(itemFirst.hashCode(), itemSecond.hashCode());
    assertEquals(itemFirst, itemSecond);
    assertTrue(itemFirst.equals(itemSecond));
  }

}
