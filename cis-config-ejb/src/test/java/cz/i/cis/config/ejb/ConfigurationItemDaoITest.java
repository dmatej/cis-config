package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.exceptions.CisUserDaoException;
import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemDaoException;
import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemKeyDaoException;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemKeyTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemTestHelper;
import cz.i.cis.config.helpers.ConfigurationProfileItemTestHelper;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;
import cz.i.cis.config.jpa.ConfigurationProfileItem;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationItemDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemDao.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemDao")
  private ConfigurationItemDao configurationItemDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao cisUserDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemCategoryDao")
  private ConfigurationItemCategoryDao categoryDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao configurationItemKeyDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemTestHelper")
  private ConfigurationItemTestHelper configurationItemHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper userHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper categoryHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper keyHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationProfileItemTestHelper")
  private ConfigurationProfileItemTestHelper profileHelper;


  @Before
  public void init() {
  }


  @After
  public void cleanup() {
    configurationItemHelper.cleanup();
    profileHelper.cleanup();
    keyHelper.cleanup();
    categoryHelper.cleanup();
    userHelper.cleanup();
  }


  @Test
  public void creatNewConfigurationItem() throws CisUserDaoException, ConfigurationItemKeyDaoException, Exception {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category name");
    categoryDao.addCategory(category);
    categoryHelper.addToDelete(category);

    final ConfigurationItemKey key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("base configuration key");
    key.setKey("base");
    key.setType(ConfigurationItemKeyType.Text);
    configurationItemKeyDao.addItemKey(key);
    keyHelper.addToDelete(key);

    final CisUser user = new CisUser();
    user.setLastName("Jezek");
    user.setLogin("log");
    user.setFirstName("Karl");
    user.setBirthDate(new Date());
    cisUserDao.addUser(user);
    userHelper.addToDelete(user);

    final ConfigurationItem configuration = new ConfigurationItem();
    configuration.setUser(user);
    configuration.setKey(key);
    configuration.setValue("key-user value");
    configuration.setUpdate(new Date());
    configurationItemDao.addItem(configuration);
    configurationItemHelper.addToDelete(configuration);

    LOG.debug("configuration: {}", configuration);
    assertNotNull("configuration.id", configuration.getId());

    configuration.setValue("user-key value");
    configurationItemDao.updateItem(configuration);
    assertTrue(configurationItemDao.listItems().get(0).getValue().equals("user-key value"));
  }


  @Test(expected = ConfigurationItemDaoException.class)
  public void creatAlreadyExistingConfigurationItem() throws Exception {
    final ConfigurationItem configuration = configurationItemHelper.createConfigurationItem();
    final ConfigurationItem configurationCopy = new ConfigurationItem();
    configurationCopy.setKey(configuration.getKey());
    configurationCopy.setUpdate(configuration.getUpdate());
    configurationCopy.setUser(configuration.getUser());
    configurationCopy.setValue(configuration.getValue());
    configurationItemDao.addItem(configurationCopy);
  }


  @Test(expected = ConfigurationItemDaoException.class)
  public void testWrongUpdateItem() throws Exception {
    final ConfigurationItem configuration = configurationItemHelper.createConfigurationItem();
    configuration.setId(1);
    configurationItemDao.updateItem(configuration);
  }


  @Test
  public void testRemoveItem() throws Exception {
    final ConfigurationItem configuration = configurationItemHelper.createConfigurationItem();
    configurationItemDao.removeItem(configuration.getId());
    assertTrue(configurationItemDao.listItems().size() == 0);
  }


  @Test
  public void testGetItem() {
    final ConfigurationItem configuration0 = configurationItemHelper.createConfigurationItem();
    int idConfiguration = configuration0.getId();
    final ConfigurationItem configuration1 = configurationItemDao.getItem(idConfiguration);
    assertEquals(configuration0.hashCode(), configuration1.hashCode());

  }


  @Test
  public void testActivateProfile() throws Exception {
    final ConfigurationProfileItem profileItem = profileHelper.createConfigurationProfileItem();
    List<ConfigurationProfileItem> list = new LinkedList<ConfigurationProfileItem>();
    list.add(profileItem);
    final CisUser user = userHelper.createUser();
    configurationItemHelper.createConfigurationItem();
    configurationItemDao.activateProfile(list, user);
  }


  @Test
  public void listConfigurationItems() {
    final List<ConfigurationItem> listItems0 = configurationItemDao.listItems();
    LOG.debug("list configuration  listItems0: {}", listItems0);
    assertNotNull("configuration  listItems0", listItems0);
    assertTrue("items.empty", listItems0.isEmpty());
    ConfigurationItemCategory category = categoryHelper.createConfigurationCategory();
    final List<ConfigurationItem> listItems1 = configurationItemDao.listItems(category);
    assertNotNull("configuration  listItems1", listItems1);
    assertTrue(" listItems1.empty", listItems1.isEmpty());
  }


  @Test
  public void testComparationKeys() {
    final ConfigurationItemKey key = new ConfigurationItemKey();
    final CisUser user = new CisUser();

    final ConfigurationItem itemFirst = new ConfigurationItem();
    itemFirst.setId(1);
    itemFirst.setUser(user);
    itemFirst.setKey(key);
    itemFirst.setValue("key-user value");
    itemFirst.setUpdate(new Date());

    final ConfigurationItem itemSecond = new ConfigurationItem();
    itemSecond.setId(1);
    itemSecond.setUser(user);
    itemSecond.setKey(key);
    itemSecond.setValue("key-user value");
    itemSecond.setUpdate(new Date());

    assertEquals(itemFirst.hashCode(), itemSecond.hashCode());
    assertEquals(itemFirst, itemSecond);
    assertTrue(itemFirst.equals(itemSecond));
  }

}
