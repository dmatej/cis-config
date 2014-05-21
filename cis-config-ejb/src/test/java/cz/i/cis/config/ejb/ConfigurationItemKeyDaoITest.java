package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
import cz.i.cis.config.ejb.dao.exceptions.ConfigurationItemKeyDaoException;
import cz.i.cis.config.helpers.ConfigurationCategoryTestHelper;
import cz.i.cis.config.helpers.ConfigurationItemKeyTestHelper;
import cz.i.cis.config.jpa.ConfigurationItemCategory;
import cz.i.cis.config.jpa.ConfigurationItemKey;
import cz.i.cis.config.jpa.ConfigurationItemKeyType;
import cz.i.cis.config.test.ArquillianITest;

public class ConfigurationItemKeyDaoITest extends ArquillianITest {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationItemKeyDao.class);

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyDao")
  private ConfigurationItemKeyDao configurationItemKeyDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemCategoryDao")
  private ConfigurationItemCategoryDao configurationCategoryDao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper categoryHelper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private ConfigurationItemKeyTestHelper configurationItemKeyHelper;


  @Before
  public void init() {
  }


  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    configurationItemKeyHelper.cleanup();
    categoryHelper.cleanup();
  }


  @Test
  public void creatNewConfigurationItemKey() throws ConfigurationItemKeyDaoException, Exception {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category name");
    configurationCategoryDao.addCategory(category);
    categoryHelper.addToDelete(category);

    final ConfigurationItemKey key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("base configuration key");
    key.setKey("base");
    key.setType(ConfigurationItemKeyType.Text);
    configurationItemKeyDao.addItemKey(key);
    configurationItemKeyHelper.addToDelete(key);
    LOG.debug("key: {}", key);
    assertNotNull("key.id", key.getId());

    final List<ConfigurationItemKey> listKeys = configurationItemKeyDao.filterItemKeys(category);
    assertFalse("listKeys.isEmpty", listKeys.isEmpty());
    assertEquals(listKeys.get(0), key);
    key.setType(ConfigurationItemKeyType.URL);
    configurationItemKeyDao.updateItemKey(key);
    assertTrue(configurationItemKeyDao.listItemKeys().get(0).getType().equals(ConfigurationItemKeyType.URL));
  }


  @Test(expected = ConfigurationItemKeyDaoException.class)
  public void creatAlreadyExistingConfigurationKey() throws Exception {
    final ConfigurationItemKey key = configurationItemKeyHelper.createConfigurationKey();
    final ConfigurationItemKey keyCopy = new ConfigurationItemKey();
    keyCopy.setCategory(key.getCategory());
    keyCopy.setDescription(key.getDescription());
    keyCopy.setType(key.getType());
    keyCopy.setKey(key.getKey());
    configurationItemKeyDao.addItemKey(keyCopy);
  }


  @Test(expected = ConfigurationItemKeyDaoException.class)
  public void testWrongUpdateConfigurationKey() throws Exception {
    final ConfigurationItemKey key = configurationItemKeyHelper.createConfigurationKey();
    key.setId(2);
    configurationItemKeyDao.updateItemKey(key);
  }


  @Test
  public void testGetItemKey() {
    final ConfigurationItemKey key0 = configurationItemKeyHelper.createConfigurationKey();
    int idKey = key0.getId();
    final ConfigurationItemKey key1 = configurationItemKeyDao.getItemKey(idKey);
    assertEquals(key0.hashCode(), key1.hashCode());
    String nameKey = key0.getKey();
    final ConfigurationItemKey key2 = configurationItemKeyDao.getItemKey(nameKey);
    assertEquals(key0.hashCode(), key2.hashCode());
  }


  @Test
  public void testGetItemKeyMap() {
    final List<ConfigurationItemKey> itemKeys0 = new ArrayList<ConfigurationItemKey>();
    final Map<String, ConfigurationItemKey> map0 = ConfigurationItemKeyDao.getItemKeyMap(itemKeys0);
    assertTrue("map0.isEmpty", map0.isEmpty());
    final ConfigurationItemKey key = configurationItemKeyHelper.createConfigurationKey();
    final List<ConfigurationItemKey> itemKeys1 = new ArrayList<ConfigurationItemKey>();
    itemKeys1.add(key);
    final Map<String, ConfigurationItemKey> map1 = ConfigurationItemKeyDao.getItemKeyMap(itemKeys1);
    assertFalse("map1.isEmpty", map1.isEmpty());
    assertTrue("map1.size == 1", map1.size() == 1);
  }


  @Test
  public void listConfigurationKeys() {
    final List<ConfigurationItemKey> keys = configurationItemKeyDao.listItemKeys();
    LOG.debug("list configuration item keys: {}", keys);
    assertNotNull("configuration item keys", keys);
    assertTrue("keys.empty", keys.isEmpty());
  }


  @Test
  public void testItemKey() {
    final ConfigurationItemKey key = configurationItemKeyHelper.createConfigurationKey();
    assertEquals(key.hashCode(), configurationItemKeyDao.getItemKey(key.getId()).hashCode());
    assertEquals(key.hashCode(), configurationItemKeyDao.getItemKey(key.getKey()).hashCode());
    String k = key.getKey() + "a";
    final ConfigurationItemKey keyNull = configurationItemKeyDao.getItemKey(k);
    assertNull(keyNull);
  }


  @Test
  public void testRemoveItemKey() throws Exception {
    final ConfigurationItemKey key = configurationItemKeyHelper.createConfigurationKey();
    configurationItemKeyDao.removeItemKey(key.getId());
    assertTrue(configurationItemKeyDao.listItemKeys().isEmpty());
  }


  @Test
  public void testComparationKeys() {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();

    final ConfigurationItemKey keyFirst = new ConfigurationItemKey();
    keyFirst.setId(1);
    keyFirst.setCategory(category);
    keyFirst.setDescription("base configuration key");
    keyFirst.setKey("base");
    keyFirst.setType(ConfigurationItemKeyType.Text);

    final ConfigurationItemKey keySecond = new ConfigurationItemKey();
    keySecond.setId(1);
    keySecond.setCategory(category);
    keySecond.setDescription("base configuration key");
    keySecond.setKey("base");
    keySecond.setType(ConfigurationItemKeyType.Text);

    assertEquals(keyFirst.hashCode(), keySecond.hashCode());
    assertEquals(keyFirst, keySecond);
    assertTrue(keyFirst.equals(keySecond));
  }

}
