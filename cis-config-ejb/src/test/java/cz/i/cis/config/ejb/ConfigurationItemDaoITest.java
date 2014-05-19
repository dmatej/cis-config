package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.exceptions.UniqueKeyException;
import cz.i.cis.config.ejb.dao.exceptions.UserAlreadyExistsException;
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
  private ConfigurationItemDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/CisUserDao")
  private CisUserDao dao_user;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao_category;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao dao_key;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemTestHelper")
  private ConfigurationItemTestHelper helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/UserTestHelper")
  private UserTestHelper user_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper category_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper key_helper;


  @Before
  public void init() {
  }


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
    user_helper.cleanup();
    category_helper.cleanup();
    key_helper.cleanup();
  }


  @Test
  public void creatNewConfigurationItem() throws UniqueKeyException, UserAlreadyExistsException {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category name");
    dao_category.addCategory(category);
    category_helper.addToDelete(category);

    final ConfigurationItemKey key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("base configuration key");
    key.setKey("base");
    key.setType(ConfigurationItemKeyType.Text);
    dao_key.addItemKey(key);
    key_helper.addToDelete(key);

    final CisUser user = new CisUser();
    user.setLastName("Jezek");
    user.setLogin("log");
    user.setFirstName("Karl");
    user.setBirthDate(new Date());
    dao_user.addUser(user);
    user_helper.addToDelete(user);

    final ConfigurationItem configuration = new ConfigurationItem();
    configuration.setUser(user);
    configuration.setKey(key);
    configuration.setValue("key-user value");
    configuration.setUpdate(new Date());
    dao.addItem(configuration);
    helper.addToDelete(configuration);

    LOG.debug("configuration: {}", configuration);
    assertNotNull("configuration.id", configuration.getId());

    configuration.setValue("user-key value");
    dao.updateItem(configuration);
    assertTrue(dao.listItems().get(0).getValue().equals("user-key value"));
  }


  @Test(expected = UniqueKeyException.class)
  public void creatAlreadyExistingConfigurationItem() throws UniqueKeyException {
    final ConfigurationItem configuration = helper.createConfigurationItem();
    final ConfigurationItem copy_configuration = new ConfigurationItem();
    copy_configuration.setKey(configuration.getKey());
    copy_configuration.setUpdate(configuration.getUpdate());
    copy_configuration.setUser(configuration.getUser());
    copy_configuration.setValue(configuration.getValue());
    dao.addItem(copy_configuration);
  }


  @Test
  public void listConfigurationItems() {
    final List<ConfigurationItem> items = dao.listItems();
    LOG.debug("list configuration items: {}", items);
    assertNotNull("configuration items", items);
    assertTrue("items.empty", items.isEmpty());
  }


  @Test
  public void testComparationKeys() {
    final ConfigurationItemKey key = new ConfigurationItemKey();
    final CisUser user = new CisUser();

    final ConfigurationItem item0 = new ConfigurationItem();
    item0.setId(1);
    item0.setUser(user);
    item0.setKey(key);
    item0.setValue("key-user value");
    item0.setUpdate(new Date());

    final ConfigurationItem item1 = new ConfigurationItem();
    item1.setId(1);
    item1.setUser(user);
    item1.setKey(key);
    item1.setValue("key-user value");
    item1.setUpdate(new Date());

    assertEquals(item0.hashCode(), item1.hashCode());
    assertEquals(item0, item1);
    assertTrue(item0.equals(item1));
  }

}
