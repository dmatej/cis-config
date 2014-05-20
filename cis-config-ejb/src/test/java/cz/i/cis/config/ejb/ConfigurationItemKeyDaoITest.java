package cz.i.cis.config.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import cz.i.cis.config.ejb.dao.ConfigurationItemCategoryDao;
import cz.i.cis.config.ejb.dao.ConfigurationItemKeyDao;
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

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
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
  public void creatNewConfigurationItemKey() {
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

  @Test(expected = PersistenceException.class)
  public void creatAlreadyExistingConfigurationKey() throws PersistenceException {
    final ConfigurationItemKey key = configurationItemKeyHelper.createConfigurationKey();
    final ConfigurationItemKey keyCopy = new ConfigurationItemKey();
    keyCopy.setCategory(key.getCategory());
    keyCopy.setDescription(key.getDescription());
    keyCopy.setType(key.getType());
    keyCopy.setKey(key.getKey());
    configurationItemKeyDao.addItemKey(keyCopy);
    }

  @Test
  public void listConfigurationKeys() {
    final List<ConfigurationItemKey> keys = configurationItemKeyDao.listItemKeys();
    LOG.debug("list configuration item keys: {}", keys);
    assertNotNull("configuration item keys", keys);
    assertTrue("keys.empty",keys.isEmpty());
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
