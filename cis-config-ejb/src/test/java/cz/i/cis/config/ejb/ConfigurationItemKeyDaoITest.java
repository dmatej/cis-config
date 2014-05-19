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

import cz.i.cis.config.ejb.dao.ConfigurationCategoryDao;
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
  private ConfigurationItemKeyDao dao;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryDao")
  private ConfigurationCategoryDao dao_category;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationCategoryTestHelper")
  private ConfigurationCategoryTestHelper category_helper;

  @EJB(mappedName = "java:global/cis-config-test/cis-config-test-ejb/ConfigurationItemKeyTestHelper")
  private   ConfigurationItemKeyTestHelper helper;

  @Before
  public void init() {
  }

  @After
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void cleanup() {
    helper.cleanup();
    category_helper.cleanup();
  }

  @Test
  public void creatNewConfigurationItemKey() {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();
    category.setName("some category name");
    dao_category.addCategory(category);
    category_helper.addToDelete(category);

    final ConfigurationItemKey key = new ConfigurationItemKey();
    key.setCategory(category);
    key.setDescription("base configuration key");
    key.setKey("base");
    key.setType(ConfigurationItemKeyType.Text);
    dao.addItemKey(key);
    helper.addToDelete(key);
    LOG.debug("key: {}", key);
    assertNotNull("key.id", key.getId());

    final List<ConfigurationItemKey> listKeys = dao.filterItemKeys(category);
    assertFalse("listKeys.isEmpty", listKeys.isEmpty());
    assertEquals(listKeys.get(0), key);
    key.setType(ConfigurationItemKeyType.URL);
    dao.updateItemKey(key);
    assertTrue(dao.listItemKeys().get(0).getType().equals(ConfigurationItemKeyType.URL));
    }

  @Test(expected = PersistenceException.class)
  public void creatAlreadyExistingConfigurationKey() throws PersistenceException {
    final ConfigurationItemKey key = helper.createConfigurationKey();
    final ConfigurationItemKey copy_key = new ConfigurationItemKey();
    copy_key.setCategory(key.getCategory());
    copy_key.setDescription(key.getDescription());
    copy_key.setType(key.getType());
    copy_key.setKey(key.getKey());
    dao.addItemKey(copy_key);
    }

  @Test
  public void listConfigurationKeys() {
    final List<ConfigurationItemKey> keys = dao.listItemKeys();
    LOG.debug("list configuration item keys: {}", keys);
    assertNotNull("configuration item keys", keys);
    assertTrue("keys.empty",keys.isEmpty());
  }



  @Test
  public void testComparationKeys() {
    final ConfigurationItemCategory category = new ConfigurationItemCategory();

    final ConfigurationItemKey key0 = new ConfigurationItemKey();
    key0.setId(1);
    key0.setCategory(category);
    key0.setDescription("base configuration key");
    key0.setKey("base");
    key0.setType(ConfigurationItemKeyType.Text);

    final ConfigurationItemKey key1 = new ConfigurationItemKey();
    key1.setId(1);
    key1.setCategory(category);
    key1.setDescription("base configuration key");
    key1.setKey("base");
    key1.setType(ConfigurationItemKeyType.Text);

   assertEquals(key0.hashCode(), key1.hashCode());
   assertEquals(key0, key1);
   assertTrue(key0.equals(key1));
  }

}
