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
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.exceptions.CisUserDaoException;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemKeyTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemTestHelper;
import cz.i.cis.config.helpers.UserTestHelper;
import cz.i.cis.config.jpa.CisUser;
import cz.i.cis.config.jpa.ConfigurationItem;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationItemDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemDao.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemDao")
  private ConfigurationItemDao configurationItemDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao cisUserDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
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


  @Before
  public void init() {
  }


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    configurationItemHelper.cleanup();
    userHelper.cleanup();
    categoryHelper.cleanup();
    keyHelper.cleanup();
  }


  @Test
  public void creatNewConfigurationItem() throws CisUserDaoException {
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


  @Test(expected = PersistenceException.class)
  public void creatAlreadyExistingConfigurationItem() throws PersistenceException {
    final ConfigurationItem configuration = configurationItemHelper.createConfigurationItem();
    final ConfigurationItem configurationCopy = new ConfigurationItem();
    configurationCopy.setKey(configuration.getKey());
    configurationCopy.setUpdate(configuration.getUpdate());
    configurationCopy.setUser(configuration.getUser());
    configurationCopy.setValue(configuration.getValue());
    configurationItemDao.addItem(configurationCopy);
  }


  @Test
  public void listConfigurationItems() {
    final List<ConfigurationItem> items = configurationItemDao.listItems();
    LOG.debug("list configuration items: {}", items);
    assertNotNull("configuration items", items);
    assertTrue("items.empty", items.isEmpty());
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
